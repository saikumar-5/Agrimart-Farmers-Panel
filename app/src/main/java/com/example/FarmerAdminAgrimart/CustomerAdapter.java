package com.example.FarmerAdminAgrimart;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> customerList;
    private List<Customer> filteredCustomerList;
    private Context context;

    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.filteredCustomerList = new ArrayList<>(customerList);
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customers, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = filteredCustomerList.get(position);

        holder.customerName.setText(customer.getName());
        holder.customerPhone.setText(customer.getPhoneNumber());
        holder.customerSpent.setText("₹" + String.valueOf((int)customer.getSpentAmount()));
        holder.customerOrders.setText(String.valueOf(customer.getOrderCount()));
        holder.memberSinceDate.setText(customer.getMemberSinceDate());
    }

    @Override
    public int getItemCount() {
        return filteredCustomerList.size();
    }

    public void filter(String query) {
        filteredCustomerList.clear();

        if (query.isEmpty()) {
            filteredCustomerList.addAll(customerList);
        } else {
            String searchQuery = query.toLowerCase();

            for (Customer customer : customerList) {
                if (customer.getName().toLowerCase().contains(searchQuery) ||
                        customer.getPhoneNumber().contains(searchQuery)) {
                    filteredCustomerList.add(customer);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView customerName;
        TextView customerPhone;
        TextView customerSpent;
        TextView customerOrders;
        TextView memberSinceDate;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerPhone = itemView.findViewById(R.id.customerPhone);
            customerSpent = itemView.findViewById(R.id.customerSpent);
            customerOrders = itemView.findViewById(R.id.customerOrders);
            memberSinceDate = itemView.findViewById(R.id.memberSinceDate);
        }
    }
}