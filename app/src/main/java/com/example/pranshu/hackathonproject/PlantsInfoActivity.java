package com.example.pranshu.hackathonproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

import fr.ganfra.materialspinner.MaterialSpinner;


public class PlantsInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String Arrangement[] = {"Opposite", "Alternate", "Whorled", "Rosulate"};
    String Edge[] = {"Entire", "Ciliate", "Crenate", "Dentate", "Denticulate", "Doubly serrate", "Serrate", "Serrulate", "Sinuate", "Lobate", "Undulate", "Spiny or pungent"};
    String Veins[] = {"Arcuate", "Dichotomous", "Longitudinal", "Parallel", "Pinnate", "Reticulate", "Rotate", "Transverse"};
    String Apex[] = {"Acuminate", "Acute", "Cuspidate", "Emarginate", "Mucronate", "Mucronulate", "Obcordate", "Obtuse", "Truncate"};
    // String Size[] = {"Megaphyll", "Macrophyll", "Mesophyll", "Notophyll", "Microphyll", "Nanophyll", "Leptophyll "};
    String Structure[] = {"Bifoliolate", "Geminate", "Jugate", "Bigeminate", "Bipinnate", "Biternate", "Imparipinnate", "Odd-Pinnate",
            "Paripinnate", "Even-Pinnate", "Palmately Compound", "Pinnately Compound", "Simple", "Ternate", "Trifoliate", "trifoliolate", "Tripinnate"};
    String Shape[] = {"Acicular", "Acuminate", "acute", "Apiculate", "Aristate", "Attenuate", "Auriculate", "Asymmetrical", "Caudate",
            "Cordate", "Cuneate", "Cuspidate", "Deltoidor Deltate", "Digitate", "Elliptic", "Ensiform", "Emarginate", "Falcate",
            "Fenestrate", "Filiform", "Flabellate", "Hastate", "Laciniate", "Lanceolate", "Laminar", "Linear", "Lobed", "Lorate",
            "Lyrate", "Mucronate", "Multifid", "Obcordate", "Oblanceolate", "Oblique", "Oblong", "Obovate", "Obtrullate",
            "Obtuse", "Orbicular", "Ovate", "Palmate", "Palmately lobed", "Palmatifid", "Palmatipartite", "Palmisect",
            "Pandurate", "Pedate", "Peltate", "Perfoliate", "Perforate", "Pinnately lobed", "Pinnatifid", "Pinnatipartite",
            "Pinnatisect", "Plicate", "Reniform", "Retuse", "Rhomboid or Rhombic", "Rounded", "Semiterete", "Template:Sinuate",
            "Sagittate", "Spatulate", "Spear-Shaped", "Subobtuse", "Subulate", "Terete", "Trullate", "Truncate", "Undulate", "Unifoliate"};
    ArrayAdapter<String> adapterArrang, adapterEdge, adapterVeins, adapterApex, adapterSize, adapterStructure, adapterShape;
    MaterialSpinner spin1, spin2, spin3, spin4, spin5, spin6, spin7;
    String s1, s2, s3, s4, s5, s6, s7;
    String JSON_STRING;
    ArrayList<String> listName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_info);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        spin1 = (MaterialSpinner) findViewById(R.id.spinnerArrang);
        spin2 = (MaterialSpinner) findViewById(R.id.spinnerEdge);
        spin3 = (MaterialSpinner) findViewById(R.id.spinnerVeins);
        spin4 = (MaterialSpinner) findViewById(R.id.spinnerApex);
        //  spin5 = (MaterialSpinner) findViewById(R.id.spinnerSize);
        spin6 = (MaterialSpinner) findViewById(R.id.spinnerStructure);
        spin7 = (MaterialSpinner) findViewById(R.id.spinnerShape);


        adapterArrang = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Arrangement);
        adapterEdge = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Edge);
        adapterVeins = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Veins);
        adapterApex = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Apex);
        //  adapterSize = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Size);
        adapterStructure = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Structure);
        adapterShape = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Shape);
        mSpin2();
    }

    private void mSpin2() {
        spin1.setAdapter(adapterArrang);
        spin1.setPaddingSafe(0, 0, 0, 0);
        spin2.setAdapter(adapterEdge);
        spin2.setPaddingSafe(0, 0, 0, 0);
        spin3.setAdapter(adapterVeins);
        spin3.setPaddingSafe(0, 0, 0, 0);
        spin4.setAdapter(adapterApex);
        spin4.setPaddingSafe(0, 0, 0, 0);
        //   spin5.setAdapter(adapterSize);
        // spin5.setPaddingSafe(0, 0, 0, 0);
        spin6.setAdapter(adapterStructure);
        spin6.setPaddingSafe(0, 0, 0, 0);
        spin7.setAdapter(adapterShape);
        spin7.setPaddingSafe(0, 0, 0, 0);

    }

    public void buttonSubmit(View v) {
      /*  if (spin1 != null && spin1.getSelectedItem().toString() != null && spin2 != null && spin2.getSelectedItem().toString() != null
                && spin3 != null && spin3.getSelectedItem().toString() != null && spin4 != null && spin4.getSelectedItem().toString() != null
                && spin6 != null && spin6.getSelectedItem().toString() != null && spin7 != null && spin7.getSelectedItem().toString() != null) {*/
        if (spin1.getCount() != 0 && spin2.getCount() != 0 && spin3.getCount() != 0 && spin4.getCount() != 0 && spin6.getCount() != 0 && spin7.getCount() != 0) {
            s1 = spin1.getSelectedItem().toString().toLowerCase();
            s2 = spin2.getSelectedItem().toString().toLowerCase();
            s3 = spin3.getSelectedItem().toString().toLowerCase();
            s4 = spin4.getSelectedItem().toString().toLowerCase();
            //  s5 = spin5.getSelectedItem().toString().toLowerCase();
            s6 = spin6.getSelectedItem().toString().toLowerCase();
            s7 = spin7.getSelectedItem().toString().toLowerCase();

            new BackGroundTaskDetail().execute(s1, s2, s3, s4, s6, s7);
        } else
            Toast.makeText(this, "Enter all feilds!!!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String name = spin1.getSelectedItem().toString().trim();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class BackGroundTaskDetail extends AsyncTask<String, Void, String> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(PlantsInfoActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            progress.setMessage("Please wait...           ");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String sArran, sEdge, sVein, sApex, sStruct, sShape;
            sArran = params[0].toLowerCase().trim();
            sEdge = params[1].toLowerCase().trim();
            sVein = params[2].toLowerCase().trim();
            sApex = params[3].toLowerCase().trim();
            sStruct = params[4].toLowerCase().trim();
            sShape = params[5].toLowerCase().trim();
            String sUrl = "https://onlinehost.000webhostapp.com/hackathon/plantsdetails.php";

            try {
                URL url = new URL(sUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Arrangement", "UTF-8") + "=" + URLEncoder.encode(sArran, "UTF-8") + "&" +
                        URLEncoder.encode("Edge", "UTF-8") + "=" + URLEncoder.encode(sEdge, "UTF-8") + "&" + URLEncoder.encode("Veins", "UTF-8") + "=" + URLEncoder.encode(sVein, "UTF-8") + "&" +
                        URLEncoder.encode("Apex", "UTF-8") + "=" + URLEncoder.encode(sApex, "UTF-8") + "&" + URLEncoder.encode("Structure", "UTF-8") + "=" + URLEncoder.encode(sStruct, "UTF-8") + "&" +
                        URLEncoder.encode("Shape", "UTF-8") + "=" + URLEncoder.encode(sShape, "UTF-8");
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

                return stringBuilder.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();

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
          //  Toast.makeText(PlantsInfoActivity.this,"inside post",Toast.LENGTH_SHORT).show();

            listName = new ArrayList<String>();
          //  if (result != null) {
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
                        listName.add(name);
                        count++;
                    }
                    Toast.makeText(PlantsInfoActivity.this,"inside if",Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(PlantsInfoActivity.this, DetailResultActivity.class);
                    k.putStringArrayListExtra("listt", listName);
                    startActivity(k);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


          //  }

        }
    }
}
