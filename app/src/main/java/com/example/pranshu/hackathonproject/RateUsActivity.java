package com.example.pranshu.hackathonproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RateUsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    RelativeLayout relativeLayout;
    Button rateBut;
    TextView tvRate;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeRate);
        rateBut = (Button) findViewById(R.id.rateBut);
        tvRate = (TextView) findViewById(R.id.textViewRate);


        anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        relativeLayout.setAnimation(anim);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRate.setText("" + rating);
            }
        });


        rateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RateUsActivity.this, "You rated this app.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
