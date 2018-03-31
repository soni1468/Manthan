package com.example.pranshu.hackathonproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String JSON_STRING, image;
    AutoCompleteTextView autoCompleteTextView;
    LinearLayout linearLayout;
    boolean flagg = true;
    ArrayAdapter<String> adapter;
    boolean wasOpened = false;
    GetSetLatLang getsetLatLAng;
    Bitmap bitmap;
    static int rand;
    Random random = new Random();
    String resultofimagesearch;
    ProgressDialog progress1, progress2;
    int PICK_IMAGE_REQUEST = 1468, count = 0;
    String KEY_IMAGE = "image", KEY_NAME = "name", name = "abc";
    String UPLOAD_URL = "http://192.168.43.191/rahul/vollyupload.php";
    String nameList[] = {"1. Quercus Suber", "2. Salix Atrocinerea", "3. Populus Nigra", "4. Alnus Sp", "5. Quercus Robur", "6. Crataegus Monogyna", "7. Ilex Aquifolium",
            "8. Nerium Oleander", "10. Tilia Tomentosa", "11. Acer Palmaturu", "12. Celtis Sp", "13. Corylus Avellana", "14. Castanea Sativa",
            "15. Populus Alba", "16. Acer Negundo", "18. Papaver Sp", "19. Polypodium Vulgare", "20. Pinus Sp"};
    ArrayList<String> listLat, listLong, listPlace;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout) findViewById(R.id.relativeLay);
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_up1);
        //  linearLayout.setAnimation(anim);
        Toolbar toolBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolBar);
        //   nameList = new ArrayList<String>();
        getsetLatLAng = new GetSetLatLang();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_name);
        autoCompleteTextView.setDropDownBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorText)));

        //   pran();

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, nameList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
    }

    //   private void pran() {
    //       new BackgroundTaskAuto().execute();
    //  }

    //WHAT HAPPENS WHEN BACK PRESSED IS PRESSED
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            if (flag)
                super.onBackPressed();
            flag = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag = false;
                }
            }, 3000);
        }
    }

    // FOR OPENING CAMERA
    public void openMapp(View v) {
        String ssName = autoCompleteTextView.getText().toString().toUpperCase().trim();
        //  Toast.makeText(this,ssName,Toast.LENGTH_SHORT).show();
        if (ssName.length() > 0 && ssName != "")
            new BackgroundTaskMap().execute(ssName);
        else {
            Snackbar snackbar = Snackbar.make(linearLayout, "Not a valid name!!!", Snackbar.LENGTH_SHORT).setAction("Action", null);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            textView.setTextSize(16f);
            snackbar.show();
        }
    }

    //FOR SHOWING DETAILS ABOUT SPECIES
    public void showresult(View v) {
        String sName = autoCompleteTextView.getText().toString().toLowerCase().trim();
        if (sName.length() > 0 && sName != "") {
            new BackgroundTask().execute(sName);

        } else {
            Snackbar snackbar = Snackbar.make(linearLayout, "Not a valid name!!!", Snackbar.LENGTH_SHORT).setAction("Action", null);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            textView.setTextSize(16f);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_menu) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ayush) {
            Intent ayush = new Intent(this, WebActivity.class);
            startActivity(ayush);

        } else if (id == R.id.dataset) {
            Intent xx = new Intent(this, DataSetActivity.class);
            startActivity(xx);

        } else if (id == R.id.rate) {
            Intent h = new Intent(this, RateUsActivity.class);
            startActivity(h);

        } else if (id == R.id.about) {
            Intent z = new Intent(this, AboutUsActivity.class);
            startActivity(z);

        } else if (id == R.id.nav_commonlist) {
            Intent a = new Intent(this, CommaonListActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_detailed_search) {
            Intent b = new Intent(this, PlantsInfoActivity.class);
            startActivity(b);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //FOR CAPTURING IMAGE
    public void openCamera(View v) {
        rand = random.nextInt(2000);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //RESULT OF IMAGE CAPTURED
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1468 && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage() {
        count++;
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        // Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        resultofimagesearch = s;
                        new BackgroundTask().execute(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(MainActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();


                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, "abcde");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //FETCHING USES OR DISCRIPTION

    private void sendingData(String result) {

        if (result.equals("Null")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder.setMessage("Data Not Available!!!         ");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialogBuilder.show();
        } else {

            Intent i = new Intent(this, PlantDescription.class);
            i.putExtra("Json Data", result);
            startActivity(i);
        }
    }

    public class BackgroundTask extends AsyncTask<String, Void, String> {
        String json_url;


        @Override
        protected void onPreExecute() {
            json_url = "https://onlinehost.000webhostapp.com/hackathon/getdatahackathon.php";
            progress1 = ProgressDialog.show(MainActivity.this, "Fetching data", "Please wait...", false, false);

        }

        @Override
        protected String doInBackground(String... params) {
            String sName = params[0].toLowerCase().trim();
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(sName, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progress1.dismiss();
            sendingData(result);

        }
    }

 /*   public class BackgroundTaskAuto extends AsyncTask<Void, Void, String> {
        String json_url;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            json_url = "https://onlinehost.000webhostapp.com/hackathon/getDataForAutocomplete.php";
            progress = new ProgressDialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            progress.setMessage("Please wait...           ");
            progress.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            JSONObject jsonObject;
            JSONArray jsonArray;
            String name;
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;

                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    name = jo.getString("Name");
                    //   nameList.add(name);
                    count++;
                }
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, nameList);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }  */

    public class BackgroundTaskMap extends AsyncTask<String, Void, String> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            // Toast.makeText(MainActivity.this,"Inside preExecute",Toast.LENGTH_SHORT).show();
            progress2 = ProgressDialog.show(MainActivity.this, "Loading data", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String ssName = params[0].toLowerCase().trim();
            String j_string;
            String latlangUrl = "https://onlinehost.000webhostapp.com/dummylatlang.php";
            try {
                URL url = new URL(latlangUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(ssName, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                while ((j_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(j_string + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progress2.dismiss();
            if (result != null) {
                listLat = new ArrayList<String>();
                listLong = new ArrayList<String>();
                listPlace = new ArrayList<String>();

                JSONObject jsonObject;
                JSONArray jsonArray;
                String nameLat, nameLong, namePlace;
                int i;
                try {
                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;

                    while (count < jsonArray.length()) {
                        JSONObject jo = jsonArray.getJSONObject(count);
                        nameLat = jo.getString("Latitude");
                        nameLong = jo.getString("Longitude");
                        namePlace = jo.getString("Place");
                        listLat.add(nameLat);
                        listLong.add(nameLong);
                        listPlace.add(namePlace);
                        count++;
                    }
                    Intent mapp = new Intent(MainActivity.this, MapsActivity.class);
                    mapp.putExtra("resultLat", listLat);
                    mapp.putExtra("resultLong", listLong);
                    mapp.putExtra("resultPlace", listPlace);
                    startActivity(mapp);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        autoCompleteTextView.setText(" ".trim());
    }
}
