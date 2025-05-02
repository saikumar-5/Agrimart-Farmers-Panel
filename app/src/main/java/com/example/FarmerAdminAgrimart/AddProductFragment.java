package com.example.FarmerAdminAgrimart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "AddProductFragment";
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
    private TextInputEditText productImageURL, productName, productCategory, productPrice, productOfferPrice,
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
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in to add products", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listing_prod_entries, container, false);

        addButtonFrame = view.findViewById(R.id.addButtonFrame);
        imagesContainer = view.findViewById(R.id.imagesContainer);
        addProductbtn = view.findViewById(R.id.addProductBtn);
        progressBar = view.findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        productImageURL = view.findViewById(R.id.editProductImageURL);
        productName = view.findViewById(R.id.editProductName);
        productCategory = view.findViewById(R.id.editProductCategory);
        productPrice = view.findViewById(R.id.editProductPrice);
        productOfferPrice = view.findViewById(R.id.editProductOfferPrice);
        productDescription = view.findViewById(R.id.editProductDescription);
        productInstock = view.findViewById(R.id.editProductInstock);
        productMinOrder = view.findViewById(R.id.editProductMinQuantity);
        productPackaging = view.findViewById(R.id.editproductPackaging);

        View btnAddImage = view.findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(v -> openImagePicker());

        String[] packagingItems = new String[]{"Box", "Gunny Bag", "Plastic Crate", "Loose"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                packagingItems
        );
        productPackaging.setAdapter(adapter);
        productPackaging.setOnClickListener(v -> productPackaging.showDropDown());

        addProductbtn.setOnClickListener(v -> validateAndUploadProduct());

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

        int index = imagesContainer.indexOfChild(addButtonFrame);
        imagesContainer.addView(imageView, index);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void validateAndUploadProduct() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Please log in to add products", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "No internet connection available", Toast.LENGTH_SHORT).show();
            return;
        }

        user.getIdToken(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        proceedWithValidation();
                    } else {
                        Toast.makeText(getContext(), "Authentication error. Please log in again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void proceedWithValidation() {
        if (productName.getText().toString().trim().isEmpty()) {
            productName.setError("Product name is required");
            return;
        }

        if (productPrice.getText().toString().trim().isEmpty()) {
            productPrice.setError("Price is required");
            return;
        }

        // Removed imageUriList.isEmpty() check

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        addProductbtn.setEnabled(false);

        uploadProductWithImages();
    }

    private void uploadProductWithImages() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> productData = new HashMap<>();
        productData.put("ImageURL", productImageURL.getText().toString().trim());
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

        firestore.collection("Farmers")
                .document(uid)
                .collection("products")
                .add(productData)
                .addOnSuccessListener(documentReference -> {
                    String productId = documentReference.getId();

                    firestore.collection("products")
                            .document(productId)
                            .set(productData)
                            .addOnSuccessListener(aVoid -> {
                                // If there are images, upload them, else just finish
                                if (!imageUriList.isEmpty()) {
                                    uploadImagesToFirebase(uid, productId);
                                } else {
                                    // No images to upload, update product with empty imageUrls if needed
                                    updateProductWithImageUrls(uid, productId, new ArrayList<>());
                                    if (progressBar != null) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    addProductbtn.setEnabled(true);
                                    Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error creating global product", e);
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                addProductbtn.setEnabled(true);
                                Toast.makeText(getContext(), "Error creating global product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating product", e);
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
        final AtomicInteger failedCount = new AtomicInteger(0);
        final int totalImages = imageUriList.size();

        for (int i = 0; i < imageUriList.size(); i++) {
            Uri imageUri = imageUriList.get(i);
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference imageRef = storageRef.child("products/" + productId + "/" + fileName);

            uploadImage(imageRef, imageUri, 0, new OnImageUploadListener() {
                @Override
                public void onSuccess(String downloadUrl) {
                    imageUrls.add(downloadUrl);

                    int completed = uploadedCount.incrementAndGet();
                    Log.d(TAG, "Image upload success: " + completed + "/" + totalImages);

                    if (completed + failedCount.get() == totalImages) {
                        updateProductWithImageUrls(uid, productId, imageUrls);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    int failed = failedCount.incrementAndGet();
                    int completed = uploadedCount.get();

                    Log.e(TAG, "Image upload failed: " + failed + "/" + totalImages, e);

                    if (failed <= 3) {
                        Toast.makeText(getContext(), "Failed to upload an image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (completed + failed == totalImages) {
                        updateProductWithImageUrls(uid, productId, imageUrls);
                    }
                }
            });
        }
    }

    private void updateProductWithImageUrls(String uid, String productId, List<String> imageUrls) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> update = new HashMap<>();
        update.put("imageUrls", imageUrls);

        firestore.collection("Farmers")
                .document(uid)
                .collection("products")
                .document(productId)
                .update(update);

        firestore.collection("products")
                .document(productId)
                .update(update);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        addProductbtn.setEnabled(true);
        Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
    }

    private interface OnImageUploadListener {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
    }

    private void uploadImage(StorageReference imageRef, Uri imageUri, int retryCount, OnImageUploadListener listener) {
        final int MAX_RETRIES = 3;

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        listener.onSuccess(downloadUrl);
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to get download URL", e);
                        handleUploadFailure(imageRef, imageUri, retryCount, listener, e);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to upload image", e);
                    handleUploadFailure(imageRef, imageUri, retryCount, listener, e);
                });
    }

    private void handleUploadFailure(StorageReference imageRef, Uri imageUri, int retryCount, OnImageUploadListener listener, Exception e) {
        final int MAX_RETRIES = 3;

        if (retryCount < MAX_RETRIES) {
            String newFileName = UUID.randomUUID().toString();
            StorageReference newRef = imageRef.getParent().child(newFileName);

            Log.d(TAG, "Retrying upload (" + (retryCount + 1) + "/" + MAX_RETRIES + ")");
            uploadImage(newRef, imageUri, retryCount + 1, listener);
        } else {
            listener.onFailure(e);
        }
    }
}
