package com.example.pranshu.hackathonproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CommaonListActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> nameList;
    ArrayAdapter<String> adapter1;
    String JSON_STRING;
    String sName;
    LinearLayout linear;
    Animation anim;
    String listName[] = {"1. Quercus Suber","5. Quercus Robur","8. Nerium Oleander","10. Tilia Tomentosa","11. Acer Palmaturu","14. Castanea Sativa"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commaon_list);
        lv = (ListView) findViewById(R.id.commonlist);
        linear = (LinearLayout) findViewById(R.id.linearList);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameList = new ArrayList<String>();


        anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        linear.setAnimation(anim);
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listName);
        lv.setAdapter(adapter1);


       // pran();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sName = lv.getItemAtPosition(position).toString();
                new BackgroundTaskListSend().execute(sName);
            }
        });
    }

  /*  private void pran() {
        new BackgroundTaskList().execute();
    }  */


  /*  public class BackgroundTaskList extends AsyncTask<Void, Void, String> {
        String json_url;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            json_url = "https://onlinehost.000webhostapp.com/hackathon/getDataForAutocomplete.php";
            progress = new ProgressDialog(CommaonListActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            progress.setMessage("Please wait...loading data         ");
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
                    nameList.add(name);
                    count++;
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nameList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                lv.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }  */

    public class BackgroundTaskListSend extends AsyncTask<String, Void, String> {
        String json_url;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            json_url = "https://onlinehost.000webhostapp.com/hackathon/getdatahackathon.php";
            progress = new ProgressDialog(CommaonListActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            progress.setMessage("Fetching Data...       ");
            progress.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String sName = params[0];
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
            progress.dismiss();
            sendingData(result);

        }
    }


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
}
