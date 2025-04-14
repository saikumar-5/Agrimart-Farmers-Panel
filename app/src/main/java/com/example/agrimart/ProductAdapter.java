package com.example.agrimart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<AgricultureProduct> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(AgricultureProduct product);
    }

    public ProductAdapter(List<AgricultureProduct> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        AgricultureProduct product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to add a new product to the list
    public void addProduct(AgricultureProduct product) {
        productList.add(product);
        notifyItemInserted(productList.size() - 1);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName, tvProductQuantity, tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        public void bind(final AgricultureProduct product, final OnProductClickListener listener) {
            tvProductName.setText(product.getName());
            tvProductQuantity.setText(product.getPackagingType() != null ?
                    product.getPackagingType() : "1 kg");
            tvProductPrice.setText("₹" + product.getPrice() + "/kg");

            // If we have a valid image URL/resource, set it
            // Otherwise, use a default image
            ivProductImage.setImageResource(R.drawable.tomatoes); // Default image for now

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}