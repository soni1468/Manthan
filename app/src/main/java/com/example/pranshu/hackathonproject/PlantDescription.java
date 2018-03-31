package com.example.pranshu.hackathonproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class PlantDescription extends AppCompatActivity {
    TextView tvName, tvBotanical, tvParts, tvUse;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String name, botanical, parts, imageUrl, medicinal;
    ImageView iv;
    ProgressDialog progress2;
    ArrayList<String> listLat, listLong, listPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvName = (TextView) findViewById(R.id.textName);
        tvBotanical = (TextView) findViewById(R.id.textBotanical);
        tvParts = (TextView) findViewById(R.id.textParts);
        tvUse = (TextView) findViewById(R.id.textUse);
        iv = (ImageView) findViewById(R.id.imageView);
        Intent i = getIntent();
        String json_string = i.getStringExtra("Json Data");

        try {
            jsonObject = new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("server_response");

            JSONObject jo = jsonArray.getJSONObject(0);
            name = jo.getString("Name");
            botanical = jo.getString("Botanical Name");
            parts = jo.getString("Parts Used");
            medicinal = jo.getString("Medicinal Use");
            imageUrl = jo.getString("Image");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Toast.makeText(this,name+"    "+botanical+"    "+parts+"    "+medicinal+"    "+imageUrl,Toast.LENGTH_LONG).show();
        tvName.setText(name);
        tvBotanical.setText(botanical);
        tvParts.setText(parts);
        tvUse.setText(medicinal);

        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.pranshu)   // optional
                .error(R.drawable.pranshu)      // optional
                .resize(500, 500)                        // optional
                .into(iv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_locate,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_loc){


            new BackgroundTaskMap().execute();

        }
        return true;
    }

    public class BackgroundTaskMap extends AsyncTask<String, Void, String> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            // Toast.makeText(MainActivity.this,"Inside preExecute",Toast.LENGTH_SHORT).show();
            progress2 = ProgressDialog.show(PlantDescription.this, "Loading data", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String ssName = botanical.toLowerCase().trim();
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
                    Intent mapp = new Intent(PlantDescription.this, MapsActivity.class);
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


}
