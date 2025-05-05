package com.example.FarmerAdminAgrimart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private Context context;
    private List<OrderItem> orderItems;

    public OrderItemAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Fix: Use the correct layout for order items
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);

        // Set item data
        holder.productNameText.setText(item.getName());

        // Format price with currency symbol
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedPrice = format.format(item.getPrice()).replace(".00", "");

        holder.productPriceText.setText(formattedPrice + " per kg");
        holder.productQuantityText.setText("Qty: " + item.getQuantity());

        // Set image if available
        if (item.getImageResourceId() != 0) {
            holder.productImage.setImageResource(item.getImageResourceId());
        }

        // Calculate and set total price
        String totalPrice = format.format(item.getTotalPrice()).replace(".00", "");
        holder.productTotalText.setText(totalPrice);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productNameText, productPriceText, productQuantityText, productTotalText;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productNameText = itemView.findViewById(R.id.productNameText);
            productPriceText = itemView.findViewById(R.id.productPriceText);
            productQuantityText = itemView.findViewById(R.id.productQuantityText);
            productTotalText = itemView.findViewById(R.id.productTotalText);
        }
    }
}