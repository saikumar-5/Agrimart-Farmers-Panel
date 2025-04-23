package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AddProductFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private FrameLayout addButtonFrame;
    private ViewGroup imagesContainer;
    private Button addProductbtn;
    private List<Uri> imageUriList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextInputEditText productName, productCategory, productPrice, productOfferPrice,
            productInstock, productDescription, productMinOrder;
    private AutoCompleteTextView productPackaging;

    // Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User not logged in, redirect to login screen
            Toast.makeText(getContext(), "Please log in to add products", Toast.LENGTH_SHORT).show();
            // You can redirect to login activity here if needed
            // startActivity(new Intent(getActivity(), LoginActivity.class));
            // getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listing_prod_entries, container, false);

        // Initialize UI components
        addButtonFrame = view.findViewById(R.id.addButtonFrame);
        imagesContainer = view.findViewById(R.id.imagesContainer);
        addProductbtn = view.findViewById(R.id.addProductBtn);
        progressBar = view.findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        // Initialize form fields
        productName = view.findViewById(R.id.editProductName);
        productCategory = view.findViewById(R.id.editProductCategory);
        productPrice = view.findViewById(R.id.editProductPrice);
        productOfferPrice = view.findViewById(R.id.editProductOfferPrice);
        productDescription = view.findViewById(R.id.editProductDescription);
        productInstock = view.findViewById(R.id.editProductInstock);
        productMinOrder = view.findViewById(R.id.editProductMinQuantity);
        productPackaging = view.findViewById(R.id.editproductPackaging);

        // Add button click for images
        View btnAddImage = view.findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(v -> openImagePicker());

        // Set up packaging dropdown
        String[] packagingItems = new String[]{"Box", "Gunny Bag", "Plastic Crate", "Loose"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                packagingItems
        );
        productPackaging.setAdapter(adapter);
        productPackaging.setOnClickListener(v -> productPackaging.showDropDown());

//        // Set up add product button click
//        addProductbtn.setOnClickListener(v -> validateAndUploadProduct());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUriList.add(imageUri);
                        addImageView(imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    imageUriList.add(imageUri);
                    addImageView(imageUri);
                }
            }
        }
    }

    private void addImageView(Uri imageUri) {
        ImageView imageView = new ImageView(getContext());

        int sizeInDp = 100;
        int sizeInPx = (int) (sizeInDp * getResources().getDisplayMetrics().density);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(sizeInPx, sizeInPx);
        layoutParams.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(this).load(imageUri).into(imageView);

        // Insert image before the add button
        int index = imagesContainer.indexOfChild(addButtonFrame);
        imagesContainer.addView(imageView, index);
    }

    private void validateAndUploadProduct() {
        // Check if user is logged in
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in to add products", Toast.LENGTH_SHORT).show();
            return;
        }

        // Basic validation
        if (productName.getText().toString().trim().isEmpty()) {
            productName.setError("Product name is required");
            return;
        }

        if (productPrice.getText().toString().trim().isEmpty()) {
            productPrice.setError("Price is required");
            return;
        }

        if (imageUriList.isEmpty()) {
            Toast.makeText(getContext(), "Please add at least one image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress and disable button
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        addProductbtn.setEnabled(false);

        // Start the upload process
        uploadProductWithImages();
    }

    private void uploadProductWithImages() {
        // Get current user ID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // First create the product in Firestore to get an ID
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Create product data map
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", productName.getText().toString().trim());
        productData.put("category", productCategory.getText().toString().trim());
        productData.put("price", productPrice.getText().toString().trim());
        productData.put("offerPrice", productOfferPrice.getText().toString().trim());
        productData.put("description", productDescription.getText().toString().trim());
        productData.put("inStock", productInstock.getText().toString().trim());
        productData.put("minOrder", productMinOrder.getText().toString().trim());
        productData.put("packaging", productPackaging.getText().toString().trim());
        productData.put("imageUrls", new ArrayList<String>());
        productData.put("timestamp", System.currentTimeMillis());
        productData.put("sellerId", uid);
        productData.put("sellerEmail", currentUser.getEmail());

        // Add to user's products collection
        firestore.collection("Farmers")
                .document(uid)
                .collection("products")
                .add(productData)
                .addOnSuccessListener(documentReference -> {
                    String productId = documentReference.getId();

                    // Also add to main products collection with same ID for global listing
                    firestore.collection("products")
                            .document(productId)
                            .set(productData)
                            .addOnSuccessListener(aVoid -> {
                                uploadImagesToFirebase(uid, productId);
                            })
                            .addOnFailureListener(e -> {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                addProductbtn.setEnabled(true);
                                Toast.makeText(getContext(), "Error creating global product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    addProductbtn.setEnabled(true);
                    Toast.makeText(getContext(), "Error creating product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImagesToFirebase(String uid, String productId) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final List<String> imageUrls = new ArrayList<>();
        final AtomicInteger uploadedCount = new AtomicInteger(0);
        final int totalImages = imageUriList.size();

        for (Uri imageUri : imageUriList) {
            String fileName = UUID.randomUUID().toString();
            // Store in user-specific path
            StorageReference imageRef = storageRef.child("users/" + uid + "/products/" + productId + "/" + fileName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            imageUrls.add(downloadUrl);

                            // Check if all uploads are complete
                            int completed = uploadedCount.incrementAndGet();
                            if (completed == totalImages) {
                                // Update the product with all image URLs
                                updateProductWithImageUrls(uid, productId, imageUrls);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Still count failed uploads to avoid blocking the process
                        int completed = uploadedCount.incrementAndGet();
                        Toast.makeText(getContext(), "Failed to upload an image: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        if (completed == totalImages) {
                            updateProductWithImageUrls(uid, productId, imageUrls);
                        }
                    });
        }
    }

    private void updateProductWithImageUrls(String uid, String productId, List<String> imageUrls) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Update the user's product document with image URLs
        firestore.collection("Farmers")
                .document(uid)
                .collection("products")
                .document(productId)
                .update("imageUrls", imageUrls)
                .addOnSuccessListener(aVoid -> {
                    // Also update in global products collection
                    firestore.collection("products")
                            .document(productId)
                            .update("imageUrls", imageUrls)
                            .addOnSuccessListener(aVoid2 -> {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                addProductbtn.setEnabled(true);
                                Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                                clearForm();
                            })
                            .addOnFailureListener(e -> {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                addProductbtn.setEnabled(true);
                                Toast.makeText(getContext(), "Error updating global product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    addProductbtn.setEnabled(true);
                    Toast.makeText(getContext(), "Error updating product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearForm() {
        // Clear all form fields
        productName.setText("");
        productCategory.setText("");
        productPrice.setText("");
        productOfferPrice.setText("");
        productDescription.setText("");
        productInstock.setText("");
        productMinOrder.setText("");
        productPackaging.setText("");

        // Clear images
        imageUriList.clear();

        // Remove all image views except the add button
        int childCount = imagesContainer.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View childView = imagesContainer.getChildAt(i);
            if (childView != addButtonFrame) {
                imagesContainer.removeViewAt(i);
            }
        }
    }
}