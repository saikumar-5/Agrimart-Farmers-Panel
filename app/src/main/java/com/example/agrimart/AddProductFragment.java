package com.example.agrimart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.List;

public class AddProductFragment extends Fragment {

    private static final String TAG = "AddProductFragment";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etProductName, etProductCategory, etProductPrice;
    private EditText etProductLocation, etProductInstock, etProductPackagingType, etProductShippingType;
    private FrameLayout flAddImage;
    private ImageView ivAddImage;
    private Button btnSubmitProduct;
    private ImageButton btnBack;

    private Uri imageUri = null;
    private FirestoreHelper firestoreHelper;

    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listing_prod_entries, container, false);

        // Initialize Firestore helper
        firestoreHelper = new FirestoreHelper();

        // Initialize views
        initViews(view);

        // Set click listeners
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        etProductName = view.findViewById(R.id.etProductName);
        etProductCategory = view.findViewById(R.id.etProductCategory);
        etProductPrice = view.findViewById(R.id.etProductPrice);
        etProductLocation = view.findViewById(R.id.etProductLocation);
        etProductInstock = view.findViewById(R.id.etProductInstock);
        etProductPackagingType = view.findViewById(R.id.etProductPackagingType);
        etProductShippingType = view.findViewById(R.id.etProductShippingType);
        flAddImage = view.findViewById(R.id.flAddImage);
        ivAddImage = view.findViewById(R.id.ivAddImage);
        btnSubmitProduct = view.findViewById(R.id.btnSubmitProduct);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            // Navigate back to previous fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        flAddImage.setOnClickListener(v -> {
            // Open gallery to pick image
            openImagePicker();
        });

        btnSubmitProduct.setOnClickListener(v -> {
            // Validate input fields
            if (validateInputs()) {
                // Show loading indicator or disable button
                btnSubmitProduct.setEnabled(false);
                btnSubmitProduct.setText("Adding product...");

                // Create product data
                AgricultureProduct product = createProductFromInputs();

                // Upload image and save product to Firestore
                uploadAndSaveProduct(product);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Display the selected image
            ivAddImage.setImageDrawable(null); // Clear the plus icon

            Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(ivAddImage);
        }
    }

    private boolean validateInputs() {
        // Check if required fields are filled
        if (etProductName.getText().toString().trim().isEmpty()) {
            etProductName.setError("Product name is required");
            return false;
        }

        if (etProductCategory.getText().toString().trim().isEmpty()) {
            etProductCategory.setError("Category is required");
            return false;
        }

        if (etProductPrice.getText().toString().trim().isEmpty()) {
            etProductPrice.setError("Price is required");
            return false;
        }

        if (etProductInstock.getText().toString().trim().isEmpty()) {
            etProductInstock.setError("Stock quantity is required");
            return false;
        }

        // Add more validation as needed
        return true;
    }

    private AgricultureProduct createProductFromInputs() {
        // Get values with proper default handling
        String name = etProductName.getText().toString().trim();
        String category = etProductCategory.getText().toString().trim();
        double price = 0.0;
        try {
            price = Double.parseDouble(etProductPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Use default value if parsing fails
        }

        String location = etProductLocation.getText().toString().trim();

        int instock = 0;
        try {
            instock = Integer.parseInt(etProductInstock.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Use default value if parsing fails
        }

        String packagingType = etProductPackagingType.getText().toString().trim();
        String shippingType = etProductShippingType.getText().toString().trim();

        // Create product object
        return new AgricultureProduct(
                name,
                category,
                price,
                location,
                instock,
                packagingType,
                shippingType
        );
    }

    private void uploadAndSaveProduct(AgricultureProduct product) {
        firestoreHelper.uploadImageAndAddProduct(imageUri, product, new FirestoreHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<AgricultureProduct> products) {
                // Add proper implementation here
                Log.d(TAG, "Data loaded successfully");
            }

            @Override
            public void DataIsInserted() {
                // Enable button
                if (isAdded()) {
                    btnSubmitProduct.setEnabled(true);
                    btnSubmitProduct.setText("Save Product");

                    // Show success message
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();

                    // Navigate back to product list
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
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
                if (isAdded()) {
                    // Enable button
                    btnSubmitProduct.setEnabled(true);
                    btnSubmitProduct.setText("Save Product");

                    // Show error message
                    Toast.makeText(getContext(), "Failed to add product: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}