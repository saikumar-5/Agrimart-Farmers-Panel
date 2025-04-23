package com.example.FarmerAdminAgrimart;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.net.Uri;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    private static final String PRODUCTS_COLLECTION = "products";

    private FirebaseFirestore db;
    private StorageReference storageRef;

    public interface DataStatus {
        void DataIsLoaded(List<AgricultureProduct> products);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
        void DataOperationFailed(String message);
    }

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    // Get all products
    public void getAllProducts(final DataStatus dataStatus) {
        CollectionReference productsRef = db.collection(PRODUCTS_COLLECTION);

        productsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AgricultureProduct> products = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        AgricultureProduct product = document.toObject(AgricultureProduct.class);
                        product.setId(document.getId());
                        products.add(product);
                    }

                    dataStatus.DataIsLoaded(products);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading products: " + e.getMessage());
                    dataStatus.DataOperationFailed(e.getMessage());
                });
    }

    // Add a new product
    public void addProduct(AgricultureProduct product, final DataStatus dataStatus) {
        db.collection(PRODUCTS_COLLECTION)
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    product.setId(documentReference.getId());
                    Log.d(TAG, "Product added with ID: " + documentReference.getId());
                    dataStatus.DataIsInserted();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding product: " + e.getMessage());
                    dataStatus.DataOperationFailed(e.getMessage());
                });
    }

    // Upload product image and add product
    public void uploadImageAndAddProduct(Uri imageUri, AgricultureProduct product, final DataStatus dataStatus) {
        if (imageUri == null) {
            // No image to upload, just add the product
            addProduct(product, dataStatus);
            return;
        }

        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("product_images/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Set image URL and add product
                        product.setImageUrl(uri.toString());
                        addProduct(product, dataStatus);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Image upload failed: " + e.getMessage());
                    dataStatus.DataOperationFailed("Image upload failed: " + e.getMessage());
                });
    }

    // Get product by ID
    public void getProductById(String productId, final DataStatus dataStatus) {
        db.collection(PRODUCTS_COLLECTION).document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AgricultureProduct product = documentSnapshot.toObject(AgricultureProduct.class);
                        product.setId(documentSnapshot.getId());

                        List<AgricultureProduct> products = new ArrayList<>();
                        products.add(product);
                        dataStatus.DataIsLoaded(products);
                    } else {
                        dataStatus.DataOperationFailed("Product not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting product: " + e.getMessage());
                    dataStatus.DataOperationFailed(e.getMessage());
                });
    }

    // Update product
    public void updateProduct(String productId, AgricultureProduct product, final DataStatus dataStatus) {
        db.collection(PRODUCTS_COLLECTION).document(productId)
                .set(product)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Product updated successfully");
                    dataStatus.DataIsUpdated();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating product: " + e.getMessage());
                    dataStatus.DataOperationFailed(e.getMessage());
                });
    }

    // Delete product
    public void deleteProduct(String productId, final DataStatus dataStatus) {
        db.collection(PRODUCTS_COLLECTION).document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Product deleted successfully");
                    dataStatus.DataIsDeleted();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting product: " + e.getMessage());
                    dataStatus.DataOperationFailed(e.getMessage());
                });
    }

    // Set up a real-time listener for products
    public void listenForProductChanges(final DataStatus dataStatus) {
        db.collection(PRODUCTS_COLLECTION)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Listen failed: " + error);
                        dataStatus.DataOperationFailed(error.getMessage());
                        return;
                    }

                    if (value != null) {
                        List<AgricultureProduct> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            AgricultureProduct product = document.toObject(AgricultureProduct.class);
                            product.setId(document.getId());
                            products.add(product);
                        }
                        dataStatus.DataIsLoaded(products);
                    }
                });
    }
}