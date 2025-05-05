package com.example.FarmerAdminAgrimart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {
    private Context context;
    private List<Order> orderList;
    private List<Order> orderListFull;
    private OnOrderClickListener listener;

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

    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        this.orderListFull = new ArrayList<>(newOrderList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_summary_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Format the price with rupee symbol
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedPrice = format.format(order.getSellingPrice()).replace(".00", "");

        holder.customerNameTextView.setText(order.getCustomerName());
        holder.orderIdTextView.setText("Order #" + order.getOrderId());
        holder.priceTextView.setText(formattedPrice);
        holder.statusTextView.setText(order.getStatus());
        holder.dateTextView.setText(order.getOrderDate());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order, position);
            }
        });
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
                            order.getStatus().toLowerCase().contains(filterPattern)) {
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

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTextView, orderIdTextView, priceTextView, statusTextView, dateTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}