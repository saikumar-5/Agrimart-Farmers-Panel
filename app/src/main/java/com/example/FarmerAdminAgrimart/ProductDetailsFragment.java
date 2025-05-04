package com.example.FarmerAdminAgrimart;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ProductDetailsFragment extends Fragment {

    private TextView tvName, tvPrice, tvCategory, tvLocation, tvPackaging, tvShipping, tvInStock;
    private ImageView ivProductImage;
    private int currentStock = 0;
    private String productId = "";
    private String imageUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Find views by ID
        tvName = view.findViewById(R.id.prdtitle);
        tvPrice = view.findViewById(R.id.prcperkg);
        tvCategory = view.findViewById(R.id.catetxt);
        tvLocation = view.findViewById(R.id.sellloc);
        tvPackaging = view.findViewById(R.id.catetxt);    // Consider using a different ID for packaging
        tvShipping = view.findViewById(R.id.sellname);     // Consider using a different ID for shipping
        tvInStock = view.findViewById(R.id.stcqnt);
        ivProductImage = view.findViewById(R.id.prdimg);

        // Back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Get arguments and set data
        Bundle args = getArguments();
        if (args != null) {
            productId = args.getString("product_id", "");
            imageUrl = args.getString("product_image", "");
            tvName.setText(args.getString("product_name", "No Name"));
            tvPrice.setText(String.format("₹ %s", args.getString("product_price", "0.00")));
            tvCategory.setText(args.getString("product_category", "No Category"));
            tvLocation.setText(args.getString("product_location", "Bengaluru"));
            tvPackaging.setText(String.format("Packing Type : %s", args.getString("product_packaging", "Gunny Bags")));
            tvShipping.setText(args.getString("product_shipping", "Abhiram"));
            currentStock = args.getInt("product_instock", 0);
            tvInStock.setText("In stock: " + currentStock);

            Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(ivProductImage);
        }

        // Add Stock button
        Button addStockBtn = view.findViewById(R.id.addstckbtn);
        addStockBtn.setOnClickListener(v -> showAddStockDialog());

        // Edit Product button
        Button editProductBtn = view.findViewById(R.id.editprdbtn);
        editProductBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("product_id", productId);
            bundle.putString("product_name", tvName.getText().toString());
            bundle.putString("product_price", tvPrice.getText().toString().replace("₹", "").trim());
            bundle.putString("product_category", tvCategory.getText().toString());
            bundle.putString("product_instock", String.valueOf(currentStock));
            bundle.putString("product_packaging", tvPackaging.getText().toString());
            bundle.putString("product_image", imageUrl);

            AddProductFragment editFragment = new AddProductFragment();
            editFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void showAddStockDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_stock);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText quantityInput = dialog.findViewById(R.id.quantityInput);
        TextView totalStockValue = dialog.findViewById(R.id.totalStockValue);
        Button addButton = dialog.findViewById(R.id.addButton);
        Button removeButton = dialog.findViewById(R.id.removeButton);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        int[] qty = {1};
        quantityInput.setText(qty[0] + " Kg's");
        totalStockValue.setText((currentStock + qty[0]) + " Kg's");

        addButton.setOnClickListener(v -> {
            qty[0]++;
            quantityInput.setText(qty[0] + " Kg's");
            totalStockValue.setText((currentStock + qty[0]) + " Kg's");
        });

        removeButton.setOnClickListener(v -> {
            if (qty[0] > 1) qty[0]--;
            quantityInput.setText(qty[0] + " Kg's");
            totalStockValue.setText((currentStock + qty[0]) + " Kg's");
        });

        saveButton.setOnClickListener(v -> {
            int newStock = currentStock + qty[0];
            currentStock = newStock;
            tvInStock.setText("In stock: " + newStock);
            // TODO: Update Firestore/database if needed
            dialog.dismiss();
        });

        dialog.show();
    }
}
