package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
        // Create first active order with items
        Order order1 = new Order("Venkata Ramana", "9908947050", "4340",
                5500, "Fri, 25 Jan", "Confirmed", 0);
        order1.setCustomerAddress("24/42, Eenor Devi Sagar\nDarani, Nellipaka\nPodalakuru mandal\nChittoor - 524366");
        order1.addItem(new OrderItem("Fresh Tomatoes", 40.0, 2, R.drawable.tomatoes));
        order1.addItem(new OrderItem("Organic Potatoes", 30.0, 3, R.drawable.tomato_22));
        activeOrderList.add(order1);

        // Create second active order with items
        Order order2 = new Order("John Doe", "1234567890", "4341",
                6500, "Sat, 26 Jan", "Picked Up", 1);
        order2.addItem(new OrderItem("Green Beans", 60.0, 5, R.drawable.tomato_img));
        order2.addItem(new OrderItem("Fresh Carrots", 45.0, 2, R.drawable.tomatoes));
        activeOrderList.add(order2);

        // Create third active order with items
        Order order3 = new Order("Mary Johnson", "5556667777", "4343",
                3200, "Fri, 25 Jan", "Shipped", 2);
        order3.addItem(new OrderItem("Broccoli", 80.0, 1, R.drawable.tomato_22));
        order3.addItem(new OrderItem("Cauliflower", 70.0, 2, R.drawable.tomatoes));
        activeOrderList.add(order3);

        // Create fourth active order with items
        Order order4 = new Order("Robert Smith", "3334445555", "4344",
                8900, "Thu, 24 Jan", "Out for Delivery", 3);
        order4.addItem(new OrderItem("Spinach Bundle", 50.0, 3, R.drawable.tomato_img));
        order4.addItem(new OrderItem("Red Capsicum", 90.0, 1, R.drawable.tomatoes));
        order4.addItem(new OrderItem("Green Onions", 30.0, 2, R.drawable.tomato_22));
        activeOrderList.add(order4);

        // Create first completed order with items
        Order completed1 = new Order("Jane Smith", "0987654321", "4342",
                7500, "Sun, 27 Jan", "Delivered", 4);
        completed1.addItem(new OrderItem("Cabbage", 35.0, 2, R.drawable.tomato_img));
        completed1.addItem(new OrderItem("Eggplant", 40.0, 3, R.drawable.tomatoes));
        completedOrderList.add(completed1);

        // Create second completed order with items
        Order completed2 = new Order("Emma Wilson", "4443332222", "4345",
                4200, "Wed, 23 Jan", "Delivered", 4);
        completed2.addItem(new OrderItem("Lady's Finger", 55.0, 1, R.drawable.tomato_22));
        completed2.addItem(new OrderItem("Cucumber", 30.0, 2, R.drawable.tomatoes));
        completedOrderList.add(completed2);

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
        // Navigate to OrderDetailsFragment with the selected order
        OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance(order);

        // Use FragmentTransaction to replace current fragment with details fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, orderDetailsFragment);
        transaction.addToBackStack(null);  // Add to back stack so user can navigate back
        transaction.commit();
    }
}