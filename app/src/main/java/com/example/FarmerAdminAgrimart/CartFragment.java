package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements OrderAdapter.OnOrderClickListener {
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private List<Order> activeOrderList;
    private List<Order> completedOrderList;
    private EditText searchEditText;
    private Button activeOrdersTab, completedOrdersTab;
    private boolean showingActiveOrders = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize views
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        activeOrdersTab = view.findViewById(R.id.activeOrdersTab);
        completedOrdersTab = view.findViewById(R.id.completedOrdersTab);

        // Set up RecyclerView
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setHasFixedSize(true);

        // Initialize lists
        orderList = new ArrayList<>();
        activeOrderList = new ArrayList<>();
        completedOrderList = new ArrayList<>();

        // Load sample data
        loadSampleOrders();

        // Create and set adapter
        orderAdapter = new OrderAdapter(getContext(), activeOrderList);
        orderAdapter.setOnOrderClickListener(this);
        ordersRecyclerView.setAdapter(orderAdapter);

        // Set up search functionality
        setupSearch();

        // Set up tab buttons
        setupTabButtons();

        return view;
    }

    private void loadSampleOrders() {
        // Add active orders (steps 0-3)
        activeOrderList.add(new Order("Venkata Ramana", "9908947050", "4340",
                5500, "Fri, 25 Jan", "Confirmed", 0));
        activeOrderList.add(new Order("John Doe", "1234567890", "4341",
                6500, "Sat, 26 Jan", "Picked Up", 1));
        activeOrderList.add(new Order("Mary Johnson", "5556667777", "4343",
                3200, "Fri, 25 Jan", "Shipped", 2));
        activeOrderList.add(new Order("Robert Smith", "3334445555", "4344",
                8900, "Thu, 24 Jan", "Out for Delivery", 3));

        // Add completed orders (step 4 - delivered)
        completedOrderList.add(new Order("Jane Smith", "0987654321", "4342",
                7500, "Sun, 27 Jan", "Delivered", 4));
        completedOrderList.add(new Order("Emma Wilson", "4443332222", "4345",
                4200, "Wed, 23 Jan", "Delivered", 4));

        // Initially show active orders
        orderList.addAll(activeOrderList);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                orderAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void setupTabButtons() {
        // Initially highlight active orders tab
        activeOrdersTab.setTextColor(getResources().getColor(R.color.AgrimartGreen));
        completedOrdersTab.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // Set click listeners
        activeOrdersTab.setOnClickListener(v -> {
            if (!showingActiveOrders) {
                showingActiveOrders = true;
                activeOrdersTab.setTextColor(getResources().getColor(R.color.AgrimartGreen));
                completedOrdersTab.setTextColor(getResources().getColor(android.R.color.darker_gray));
                orderAdapter.updateOrderList(activeOrderList);
                searchEditText.setText("");
            }
        });

        completedOrdersTab.setOnClickListener(v -> {
            if (showingActiveOrders) {
                showingActiveOrders = false;
                completedOrdersTab.setTextColor(getResources().getColor(R.color.AgrimartGreen));
                activeOrdersTab.setTextColor(getResources().getColor(android.R.color.darker_gray));
                orderAdapter.updateOrderList(completedOrderList);
                searchEditText.setText("");
            }
        });
    }

    @Override
    public void onOrderClick(Order order, int position) {
        // Handle order click - e.g., show order details
        Toast.makeText(getContext(), "Order #" + order.getOrderId() + " clicked", Toast.LENGTH_SHORT).show();
        // You would typically navigate to an order details screen here
    }
}