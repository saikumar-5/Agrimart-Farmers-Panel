package com.example.agrimart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy product data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Fresh Tomatoes", "1 kg", "₹40/kg", R.drawable.tomatoes));
        productList.add(new Product("Organic Tomatoes", "1 kg", "₹60/kg", R.drawable.tomatoes));
        productList.add(new Product("Red Tomatoes", "1 kg", "₹50/kg", R.drawable.tomatoes));

        // Adapter with click listener
        // When a product is clicked, open ProductDetailsFragment
        // Replace your existing click handler with this code
        ProductAdapter adapter = new ProductAdapter(productList, product -> {
            android.util.Log.d("AgriMart", "Product clicked: " + product.getName());

            try {
                // Test if we can create the fragment at all
                ProductDetailsFragment detailsFragment = new ProductDetailsFragment();

                // Create bundle with the data
                Bundle bundle = new Bundle();
                bundle.putString("product_name", product.getName());
                bundle.putString("product_weight", product.getQuantity());
                bundle.putString("product_price", product.getPrice());
                bundle.putInt("product_image", product.getImageResId());
                detailsFragment.setArguments(bundle);

                // Get the fragment manager and see if it exists
                android.util.Log.d("AgriMart", "Fragment manager: " +
                        (requireActivity().getSupportFragmentManager() != null));

                // Try to perform the transaction
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailsFragment)
                        .commit();

                android.util.Log.d("AgriMart", "Transaction attempted");
            } catch (Exception e) {
                // Log any exceptions that occur
                android.util.Log.e("AgriMart", "Error in fragment transaction", e);
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}

