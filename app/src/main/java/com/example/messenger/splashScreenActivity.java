package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class splashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 2700;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh);
        ImageView logo = findViewById(R.id.image_logo);
        TextView text = findViewById(R.id.starting_app_name);
        text.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the ImageView after 0.9 seconds
                logo.setVisibility(View.VISIBLE);
            }
        }, 1200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the ImageView after 0.9 seconds
                text.setVisibility(View.VISIBLE);
            }
        }, 800);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app's main activity
                Intent intent = new Intent(splashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                // Close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
