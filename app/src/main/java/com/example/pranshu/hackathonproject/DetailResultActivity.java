package com.example.pranshu.hackathonproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class DetailResultActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> listName;
    ArrayAdapter<String> adapter;
    MainActivity act;
    String sss,JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_result);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        lv = (ListView)findViewById(R.id.listShowResult);
        listName = new ArrayList<String>();
        listName = getIntent().getStringArrayListExtra("listt");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listName);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               sss= lv.getItemAtPosition(position).toString().toUpperCase().trim();
                new BackGround().execute(sss);
            }
        });
    }

    public class BackGround extends AsyncTask<String, Void, String> {
        String json_url;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            json_url = "https://onlinehost.000webhostapp.com/hackathon/getdatahackathon.php";
            progress = new ProgressDialog(DetailResultActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
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
