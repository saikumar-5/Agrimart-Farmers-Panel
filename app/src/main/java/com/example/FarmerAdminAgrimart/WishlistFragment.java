package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class WishlistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        final MaterialButton weeklyButton = view.findViewById(R.id.weeklyButton);
        final MaterialButton monthlyButton = view.findViewById(R.id.monthlyButton);
        final MaterialButton yearlyButton = view.findViewById(R.id.yearlyButton);
        final ImageView chartImage = view.findViewById(R.id.chartImage);

        // Set initial image (optional)
        chartImage.setImageResource(R.drawable.graph);

        // Button click listener
        View.OnClickListener listener = v -> {
            // Reset all buttons to default style
            resetButtons(weeklyButton, monthlyButton, yearlyButton);

            // Highlight the clicked button
            MaterialButton clicked = (MaterialButton) v;
            clicked.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.AgrimartGreen));
            clicked.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

            // Change image based on button
            if (clicked == weeklyButton) {
                chartImage.setImageResource(R.drawable.graph);
            } else if (clicked == monthlyButton) {
                chartImage.setImageResource(R.drawable.graph_week);
            } else if (clicked == yearlyButton) {
                chartImage.setImageResource(R.drawable.graph_yearly);
            }
        };

        weeklyButton.setOnClickListener(listener);
        monthlyButton.setOnClickListener(listener);
        yearlyButton.setOnClickListener(listener);
    }

    private void resetButtons(MaterialButton... buttons) {
        for (MaterialButton button : buttons) {
            button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.white));
            button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
        }
    }
}
