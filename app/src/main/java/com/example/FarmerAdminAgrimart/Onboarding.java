package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class Onboarding extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    ImageButton nextBtn;
    Button startBtn,skipBtn;
    LinearLayout dotsLayout,skipAndNextBtn;
    View[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);

        viewPager2 = findViewById(R.id.viewpager);
        nextBtn = findViewById(R.id.nextbtn);
        startBtn = findViewById(R.id.startBtn);
        skipBtn = findViewById(R.id.skipBtn);
        dotsLayout = findViewById(R.id.dotsLayout);
        skipAndNextBtn = findViewById(R.id.skipAndNextBtn);

        int[] images = {R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3, R.drawable.onboarding4, R.drawable.onboarding5};
        String[] heading = {
                getString(R.string.onboarding1_heading),
                getString(R.string.onboarding2_heading),
                getString(R.string.onboarding3_heading),
                getString(R.string.onboarding4_heading),
                getString(R.string.onboarding5_heading),

        };
        String[] desc = {
                getString(R.string.onboarding1_desc),
                getString(R.string.onboarding2_desc),
                getString(R.string.onboarding3_desc),
                getString(R.string.onboarding4_desc),
                getString(R.string.onboarding5_desc),
        };
        viewPagerItemArrayList = new ArrayList<>();

        for (int i=0;i<images.length;i++){
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i],heading[i],desc[i]);
            viewPagerItemArrayList.add(viewPagerItem);
        }
        VPAdaptor vpAdaptor = new VPAdaptor(viewPagerItemArrayList);
        viewPager2.setAdapter(vpAdaptor);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        // Initialize dots
        addDotsIndicator(0);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                addDotsIndicator(position);
            }
        });

        nextBtn.setOnClickListener(v->{
            int currentItem = viewPager2.getCurrentItem();
            if(currentItem < viewPagerItemArrayList.size()-1){
                viewPager2.setCurrentItem(currentItem+1, true);
            }
        });

        skipBtn.setOnClickListener(v->{
            viewPager2.setCurrentItem(viewPagerItemArrayList.size() - 1, true);
        });

        startBtn.setOnClickListener(v->{
            startActivity(new Intent(Onboarding.this, MainActivity.class));
            finish();
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == viewPagerItemArrayList.size() - 1) {
                    // Last page: Hide ImageButton, Show Button
                    nextBtn.setVisibility(View.GONE);
                    startBtn.setVisibility(View.VISIBLE);
                    skipBtn.setVisibility(View.GONE);
                    skipAndNextBtn.setVisibility(View.GONE);
                } else {
                    // Other pages: Show ImageButton, Hide Button
                    nextBtn.setVisibility(View.VISIBLE);
                    startBtn.setVisibility(View.GONE);
                    skipBtn.setVisibility(View.VISIBLE);
                    skipAndNextBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void addDotsIndicator(int position) {
        dotsLayout.removeAllViews();
        dots = new View[viewPagerItemArrayList.size()]; // Now it's View[]

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new View(this);
            LinearLayout.LayoutParams params;

            if (i == position) {
                // Active dot (wider)
                params = new LinearLayout.LayoutParams(60, 24); // Wider active dot
                dots[i].setBackground(ContextCompat.getDrawable(this, R.drawable.active_dot));
            } else {
                // Inactive dots (smaller)
                params = new LinearLayout.LayoutParams(24, 24); // Circular small dots
                dots[i].setBackground(ContextCompat.getDrawable(this, R.drawable.inactive_dot));
            }

            params.setMargins(6, 0, 6, 0); // Spacing between dots
            dots[i].setLayoutParams(params);
            dotsLayout.addView(dots[i]);
        }
    }

}