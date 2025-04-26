package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";
    private Button btnListProducts;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<AgricultureProduct> productList;
    private FirestoreHelper firestoreHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firestore helper
        firestoreHelper = new FirestoreHelper();

        // Initialize product list
        productList = new ArrayList<>();

        // Initialize the adapter
        adapter = new ProductAdapter(productList, this::navigateToProductDetails);
        recyclerView.setAdapter(adapter);

        // Initialize the "List your product" button
        btnListProducts = view.findViewById(R.id.btnListProducts);

        // Set click listener for the button
        setupClickListeners();

        // Load products from Firestore
        loadProductsFromFirestore();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh products when coming back to this fragment
        loadProductsFromFirestore();
    }

    private void loadProductsFromFirestore() {
        firestoreHelper.getAllProducts(new FirestoreHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<AgricultureProduct> products) {
                productList.clear();
                productList.addAll(products);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Loaded " + products.size() + " products from Firestore");
            }

            @Override
            public void DataIsInserted() {
                // Not used here
            }

            @Override
            public void DataIsUpdated() {
                // Not used here
            }

            @Override
            public void DataIsDeleted() {
                // Not used here
            }

            @Override
            public void DataOperationFailed(String message) {
                Toast.makeText(getContext(), "Failed to load products: " + message, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load products: " + message);
            }
        });

        // Optional: Set up real-time listener for product changes
        // setupRealtimeUpdates();
    }

    private void setupRealtimeUpdates() {
        firestoreHelper.listenForProductChanges(new FirestoreHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<AgricultureProduct> products) {
                productList.clear();
                productList.addAll(products);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Real-time update: " + products.size() + " products");
            }

            @Override
            public void DataIsInserted() {
                // Not used here
            }

            @Override
            public void DataIsUpdated() {
                // Not used here
            }

            @Override
            public void DataIsDeleted() {
                // Not used here
            }

            @Override
            public void DataOperationFailed(String message) {
                Log.e(TAG, "Real-time update failed: " + message);
            }
        });
    }

    private void setupClickListeners() {
        btnListProducts.setOnClickListener(v -> {
            try {
                // Create AddProductFragment
                AddProductFragment addProductFragment = new AddProductFragment();

                // Navigate to AddProductFragment
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, addProductFragment)
                        .addToBackStack(null)
                        .commit();

            } catch (Exception e) {
                // Log any exceptions
                Log.e(TAG, "Error opening AddProductFragment", e);
            }
        });
    }

    // Method to navigate to product details
    private void navigateToProductDetails(AgricultureProduct product) {
        try {
            // Create ProductDetailsFragment
            ProductDetailsFragment detailsFragment = new ProductDetailsFragment();

            // Create bundle with the data
            Bundle bundle = new Bundle();
            bundle.putString("product_id", product.getId());
            bundle.putString("product_name", product.getName());
            bundle.putString("product_category", product.getCategory());
            bundle.putString("product_price", product.getPrice());
            bundle.putString("product_location", product.getLocation());
            bundle.putInt("product_instock", product.getInstock());
            bundle.putString("product_packaging", product.getPackagingType());
            bundle.putString("product_shipping", product.getShippingType());
            bundle.putString("product_image", product.getImageUrl());

            detailsFragment.setArguments(bundle);

            // Navigate to details fragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            // Log any exceptions that occur
            Log.e(TAG, "Error in fragment transaction", e);
            Toast.makeText(getContext(), "Error opening product details", Toast.LENGTH_SHORT).show();
        }
    }
}