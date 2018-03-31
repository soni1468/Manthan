package com.example.pranshu.hackathonproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class AboutUsActivity extends AppCompatActivity {
    RelativeLayout relative;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        relative = (RelativeLayout) findViewById(R.id.relativeAbout);

        anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        relative.setAnimation(anim);
    }
}
