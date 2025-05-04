package com.example.FarmerAdminAgrimart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {
    private List<Order> orderList;
    private List<Order> orderListFull;
    private Context context;
    private OnOrderClickListener listener;

    // Interface for handling item clicks
    public interface OnOrderClickListener {
        void onOrderClick(Order order, int position);
    }

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.orderListFull = new ArrayList<>(orderList);
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Set text fields
        holder.customerName.setText(order.getCustomerName());
        holder.phoneNumber.setText(order.getPhoneNumber());
        holder.orderId.setText("Order ID: #" + order.getOrderId());
        holder.orderAmount.setText("₹ " + String.format("%,.0f", order.getAmount()));
        holder.orderDate.setText(order.getDate());
        holder.orderStatus.setText(order.getStatus());

        // Update the order progress icons based on currentStep
        updateOrderProgress(holder, order.getCurrentStep());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order, position);
            }
        });
    }

    private void updateOrderProgress(OrderViewHolder holder, int currentStep) {
        // Get references to all progress icons
        ImageView[] progressIcons = {
                holder.confirmedIcon,
                holder.pickedUpIcon,
                holder.shippedIcon,
                holder.outForDeliveryIcon,
                holder.deliveredIcon
        };

        // Update background and icon color based on step completion
        for (int i = 0; i < progressIcons.length; i++) {
            if (i <= currentStep) {
                // Set completed step (green circle background)
                progressIcons[i].setBackground(ContextCompat.getDrawable(context, R.drawable.circle_backgrnd_green));
                progressIcons[i].setColorFilter(ContextCompat.getColor(context, android.R.color.white));
            } else {
                // Set pending step (gray circle background)
                progressIcons[i].setBackground(ContextCompat.getDrawable(context, R.drawable.crcle_bg));
                // Fix: Use android.R.color.darker_gray instead of R.color.lightGray which doesn't exist
                progressIcons[i].setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray));
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public Filter getFilter() {
        return orderFilter;
    }

    private Filter orderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Order> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Order order : orderListFull) {
                    if (order.getCustomerName().toLowerCase().contains(filterPattern) ||
                            order.getOrderId().toLowerCase().contains(filterPattern) ||
                            order.getPhoneNumber().contains(filterPattern)) {
                        filteredList.add(order);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orderList.clear();
            orderList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, phoneNumber, orderId, orderAmount, orderDate, orderStatus;
        ImageView confirmedIcon, pickedUpIcon, shippedIcon, outForDeliveryIcon, deliveredIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Text views
            customerName = itemView.findViewById(R.id.customerName);
            phoneNumber = itemView.findViewById(R.id.customerPhone);
            orderId = itemView.findViewById(R.id.orderId);
            orderAmount = itemView.findViewById(R.id.orderAmounts);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatuss);

            // Progress icons
            confirmedIcon = itemView.findViewById(R.id.confirmedIcons);
            pickedUpIcon = itemView.findViewById(R.id.pickedUpIcons);
            shippedIcon = itemView.findViewById(R.id.shippedIcons);
            outForDeliveryIcon = itemView.findViewById(R.id.outForDeliveryIcons);
            deliveredIcon = itemView.findViewById(R.id.deliveredIcons);
        }
    }

    // Method to update order list
    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        this.orderListFull = new ArrayList<>(newOrderList);
        notifyDataSetChanged();
    }
}