package com.example.agrimart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener; // 👈 add a listener

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
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
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.weight.setText(product.getQuantity());
        holder.price.setText(product.getPrice());
        holder.image.setImageResource(product.getImageResId());

        // 🔥 handle click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                Log.d("ProductAdapter", "Product clicked: " + product.getName());
                listener.onProductClick(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, weight, price;
        ImageView image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvProductName);
            weight = itemView.findViewById(R.id.tvProductQuantity);
            price = itemView.findViewById(R.id.tvProductPrice);
            image = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
