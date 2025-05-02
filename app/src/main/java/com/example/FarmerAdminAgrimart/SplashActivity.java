package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.FarmerAdminAgrimart.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // UI Elements
        ImageView tree = findViewById(R.id.tree);
        TextView agrText = findViewById(R.id.agr_text);
        TextView martText = findViewById(R.id.mart_text);

        // Load animations
        Animation treeFall = AnimationUtils.loadAnimation(this, R.anim.tree_fall);
        Animation agrAppear = AnimationUtils.loadAnimation(this, R.anim.agr_appear);
        Animation martAppear = AnimationUtils.loadAnimation(this, R.anim.mart_appear);
        Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.tree_zoom);
        tree.startAnimation(zoomAnimation);

        // Start tree falling
        tree.startAnimation(treeFall);

        // After tree falls, show "AGR" text
        new Handler().postDelayed(() -> {
            agrText.setVisibility(TextView.VISIBLE);
            agrText.startAnimation(agrAppear);
        }, 1500);

        // Show "Mart" text after "AGR"
        new Handler().postDelayed(() -> {
            martText.setVisibility(TextView.VISIBLE);
            martText.startAnimation(martAppear);
        }, 2300);

        // Reverse animation (AGR & Mart disappear)
        new Handler().postDelayed(() -> {
            agrText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.agr_retract));
            martText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.mart_retract));

            // Make them invisible after animation
            new Handler().postDelayed(() -> {
                agrText.setVisibility(TextView.INVISIBLE);
                martText.setVisibility(TextView.INVISIBLE);
            }, 800);
        }, 4000);


        // Zoom tree to full screen
        new Handler().postDelayed(() -> {
            tree.startAnimation(zoomAnimation);
        }, 5000);

        // Move to MainActivity after animation
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 5700);
    }

}