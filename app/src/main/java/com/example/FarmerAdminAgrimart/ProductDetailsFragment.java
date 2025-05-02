package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ProductDetailsFragment extends Fragment {

    private static final String ARG_PRODUCT_NAME = "product_name";
    private static final String ARG_PRODUCT_WEIGHT = "product_weight";
    private static final String ARG_PRODUCT_PRICE = "product_price";
    private static final String ARG_PRODUCT_IMAGE = "product_image";

    // New constructor that accepts an image URL string instead of resource ID
    public static ProductDetailsFragment newInstance(String name, String weight, String price, String imageUrl) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_NAME, name);
        args.putString(ARG_PRODUCT_WEIGHT, weight);
        args.putString(ARG_PRODUCT_PRICE, price);
        args.putString(ARG_PRODUCT_IMAGE, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    // Keep the old constructor for backward compatibility
    public static ProductDetailsFragment newInstance(String name, String weight, String price, int imageResId) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_NAME, name);
        args.putString(ARG_PRODUCT_WEIGHT, weight);
        args.putString(ARG_PRODUCT_PRICE, price);
        args.putInt(ARG_PRODUCT_IMAGE, imageResId);
        args.putBoolean("isResourceId", true);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        TextView name = view.findViewById(R.id.tvProductName);
        TextView weight = view.findViewById(R.id.tvProductQuantity);
        TextView price = view.findViewById(R.id.tvProductPrice);
        ImageView image = view.findViewById(R.id.ivProductImage);

        if (getArguments() != null) {
            name.setText(getArguments().getString(ARG_PRODUCT_NAME));
            weight.setText(getArguments().getString(ARG_PRODUCT_WEIGHT));
            price.setText(getArguments().getString(ARG_PRODUCT_PRICE));

            // Check if we're using a resource ID or URL for the image
            if (getArguments().getBoolean("isResourceId", false)) {
                // Using resource ID (original way)
                image.setImageResource(getArguments().getInt(ARG_PRODUCT_IMAGE));
            } else {
                // Using URL (new way)
                String imageUrl = getArguments().getString(ARG_PRODUCT_IMAGE);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    // Load image with Glide
                    Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.tomatoes)
                            .error(R.drawable.tomatoes)
                            .into(image);
                } else {
                    // Fallback to default image
                    image.setImageResource(R.drawable.tomatoes);
                }
            }
        }

        return view;
    }
}