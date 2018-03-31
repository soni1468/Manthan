package com.example.pranshu.hackathonproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView splashIcon;
    boolean check = true;
    RelativeLayout relativeLayout;
    private boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        splashIcon = (ImageView) findViewById(R.id.imageViewSplash);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
        //asign the animation to ImageView
        splashIcon.setAnimation(anim);
        //define what happens when animation completes
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                anim.reset();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("Action", null);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.GREEN);
                    textView.setTextSize(18f);
                    snackbar.show();
                    Thread t = new Thread() {
                        public void run() {
                            while (status) {
                                ConnectivityManager cm =
                                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean IsConnected = activeNetwork != null &&
                                        activeNetwork.isConnectedOrConnecting();
                                if (IsConnected) {
                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    status = false;
                                }
                            }
                        }
                    };
                    t.start();


                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });
    }


 /*   private void ThreadClassFirst() {
        Thread f = new Thread(){
            public void run(){
                try {
                    while(check) {
                        sleep(2500);
                        Intent j = new Intent(SplashScreenActivity.this,MainActivity.class);
                        startActivity(j);
                        check = false;
                        ThreadClassSecond();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }; f.start();
    }

    private void ThreadClassSecond() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Snackbar snackbar = Snackbar.make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("Action", null);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            snackbar.show();
            Thread t = new Thread() {
                public void run() {
                    while (status) {
                        ConnectivityManager cm =
                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        boolean IsConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();
                        if (IsConnected) {
                            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(i);
                            status = false;
                        }
                    }
                }
            };
            t.start();
        }
    } */

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
