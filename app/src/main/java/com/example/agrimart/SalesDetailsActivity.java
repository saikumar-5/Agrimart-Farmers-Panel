package com.example.agrimart;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class SalesDetailsActivity extends AppCompatActivity {

    private MaterialButton weeklyButton, monthlyButton, yearlyButton;
    private FrameLayout chartContainer;
    private ImageView chartImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ISSUE 1: Fix incorrect layout reference
        // Change from R.layout.fragment_wishlist to R.layout.activity_sales_details
        setContentView(R.layout.activity_sales_details);

        // Initialize views
        weeklyButton = findViewById(R.id.weeklyButton);
        monthlyButton = findViewById(R.id.monthlyButton);
        yearlyButton = findViewById(R.id.yearlyButton);
        chartContainer = findViewById(R.id.chartContainer);

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Create ImageView for chart
        chartImageView = new ImageView(this);
        chartImageView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        chartImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // Add padding to the image for better visualization
        chartImageView.setPadding(16, 16, 16, 16);

        // Add the image view to the container
        chartContainer.addView(chartImageView);

        // Set weekly graph as default
        showWeeklyGraph();

        // Set up button click listeners
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        weeklyButton.setOnClickListener(v -> {
            updateButtonStyles(weeklyButton);
            showWeeklyGraph();
        });

        monthlyButton.setOnClickListener(v -> {
            updateButtonStyles(monthlyButton);
            showMonthlyGraph();
        });

        yearlyButton.setOnClickListener(v -> {
            updateButtonStyles(yearlyButton);
            showYearlyGraph();
        });
    }

    private void updateButtonStyles(MaterialButton selectedButton) {
        // Reset all buttons to default style
        weeklyButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        monthlyButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        yearlyButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        weeklyButton.setElevation(2f);
        monthlyButton.setElevation(2f);
        yearlyButton.setElevation(2f);

        // Set selected button style
        int agrimartGreenColor = getResources().getColor(R.color.AgrimartGreen);
        selectedButton.setBackgroundTintList(ColorStateList.valueOf(agrimartGreenColor));
        selectedButton.setElevation(4f);
    }

    private void showWeeklyGraph() {
        // ISSUE 2: Using correct graph resource for weekly
        chartImageView.setImageResource(R.drawable.graph);

        // Optional: Add animation
        animateGraphChange();
    }

    private void showMonthlyGraph() {
        // ISSUE 3: Using correct graph resource for monthly instead of duplicate
        chartImageView.setImageResource(R.drawable.circle_green);

        // Optional: Add animation
        animateGraphChange();
    }

    private void showYearlyGraph() {
        // ISSUE 4: Using consistent naming convention for yearly graph
        chartImageView.setImageResource(R.drawable.weekgraph);

        // Optional: Add animation
        animateGraphChange();
    }

    private void animateGraphChange() {
        // Create fade out animation
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(150);

        // Create fade in animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(150);

        // Set animation listener to start fade in after fade out completes
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                chartImageView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start fade out animation
        chartImageView.startAnimation(fadeOut);
    }
}