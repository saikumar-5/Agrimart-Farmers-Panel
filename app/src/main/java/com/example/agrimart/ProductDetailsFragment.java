package com.example.agrimart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductDetailsFragment extends Fragment {

    private static final String ARG_PRODUCT_NAME = "product_name";
    private static final String ARG_PRODUCT_WEIGHT = "product_weight";
    private static final String ARG_PRODUCT_PRICE = "product_price";
    private static final String ARG_PRODUCT_IMAGE = "product_image";

    public static ProductDetailsFragment newInstance(String name, String weight, String price, int imageResId) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_NAME, name);
        args.putString(ARG_PRODUCT_WEIGHT, weight);
        args.putString(ARG_PRODUCT_PRICE, price);
        args.putInt(ARG_PRODUCT_IMAGE, imageResId);
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
            image.setImageResource(getArguments().getInt(ARG_PRODUCT_IMAGE));
        }

        return view;
    }
}