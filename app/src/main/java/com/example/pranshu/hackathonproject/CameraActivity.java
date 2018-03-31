package com.example.pranshu.hackathonproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {
    ImageView iv;
    Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
     /*   iv = (ImageView) findViewById(R.id.imageViewCamera);
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            path = (Uri) extra.get("imageUrl");
        }


        iv.setImageURI(path);  */
    }
}
