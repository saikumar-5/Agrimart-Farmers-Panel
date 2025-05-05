package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailsFragment extends Fragment {

    private static final String ARG_ORDER = "order";

    private Order order;
    private RecyclerView orderItemsRecyclerView;
    private OrderItemAdapter orderItemAdapter;
    private List<OrderItem> orderItems;

    // Order ID and Date
    private TextView orderIdText, orderDateText;

    // Timeline status views
    private ImageView confirmedIcon, pickedUpIcon, shippedIcon, outForDeliveryIcon, deliveredIcon;
    private View outForDeliveryLine;
    private TextView confirmedTimeText, pickedUpTimeText, shippedTimeText, outForDeliveryTimeText, deliveredTimeText;

    // Shipping Details
    private TextView customerNameText, customerAddressText, customerPhoneText;

    // Price Details
    private TextView listPriceText, sellingPriceText, totalPriceText;

    // Call Agent Card
    private CardView callDeliveryAgentCard;

    // Back Button
    private ImageButton backButton;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided order.
     *
     * @param order The order to display details for.
     * @return A new instance of fragment OrderDetailsFragment.
     */
    public static OrderDetailsFragment newInstance(Order order) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, (Serializable) order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Use the layout file from your XML resource
        View view = inflater.inflate(R.layout.fragment_order_layout, container, false);

        // Initialize views
        initializeViews(view);

        // Set up RecyclerView
        setupRecyclerView();

        // Display order details
        displayOrderDetails();

        // Set up click listeners
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        // Order ID and Date
        orderIdText = view.findViewById(R.id.orderIdText);
        orderDateText = view.findViewById(R.id.orderDateText);

        // Status Timeline Views
        confirmedIcon = view.findViewById(R.id.confirmedIcon);
        pickedUpIcon = view.findViewById(R.id.pickedUpIcon);
        shippedIcon = view.findViewById(R.id.shippedIcon);
        outForDeliveryIcon = view.findViewById(R.id.outForDeliveryIcon);
        deliveredIcon = view.findViewById(R.id.deliveredIcon);
        outForDeliveryLine = view.findViewById(R.id.outForDeliveryLine);

        // Status Timeline Text Views
        confirmedTimeText = view.findViewById(R.id.confirmedTimeText);
        pickedUpTimeText = view.findViewById(R.id.pickedUpTimeText);
        shippedTimeText = view.findViewById(R.id.shippedTimeText);
        outForDeliveryTimeText = view.findViewById(R.id.outForDeliveryTimeText);
        deliveredTimeText = view.findViewById(R.id.deliveredTimeText);

        // Shipping Details
        customerNameText = view.findViewById(R.id.customerNameText);
        customerAddressText = view.findViewById(R.id.customerAddressText);
        customerPhoneText = view.findViewById(R.id.customerPhoneText);

        // Price Details
        listPriceText = view.findViewById(R.id.listPriceText);
        sellingPriceText = view.findViewById(R.id.sellingPriceText);
        totalPriceText = view.findViewById(R.id.totalPriceText);

        // RecyclerView for Order Items
        orderItemsRecyclerView = view.findViewById(R.id.orderItemsRecyclerView);

        // Call Delivery Agent Card
        callDeliveryAgentCard = view.findViewById(R.id.callDeliveryAgentCard);

        // Back Button
        backButton = view.findViewById(R.id.backButton);
    }

    private void setupRecyclerView() {
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderItemsRecyclerView.setHasFixedSize(true);

        // Load order items
        loadOrderItems();

        // Set up adapter
        orderItemAdapter = new OrderItemAdapter(getContext(), orderItems);
        orderItemsRecyclerView.setAdapter(orderItemAdapter);
    }

    private void loadOrderItems() {
        orderItems = new ArrayList<>();

        // In a real app, you would load these from a database or API based on the order ID
        if (order != null && order.getItems() != null && !order.getItems().isEmpty()) {
            orderItems.addAll(order.getItems());
        } else {
            // Since no items are available, add sample items for display
            orderItems.add(new OrderItem("Fresh Tomatoes", 40.0, 2, R.drawable.tomatoes));
            orderItems.add(new OrderItem("Organic Potatoes", 30.0, 3, R.drawable.tomato_22));
            orderItems.add(new OrderItem("Green Beans", 60.0, 1, R.drawable.tomato_img));
        }
    }

    private void displayOrderDetails() {
        if (order == null) return;

        // Display Order ID and Date
        orderIdText.setText("Order ID : #" + order.getOrderId());
        orderDateText.setText(order.getOrderDate());

        // Display Shipping Information
        customerNameText.setText(order.getCustomerName());
        customerAddressText.setText(order.getCustomerAddress());
        customerPhoneText.setText(order.getPhoneNumber());

        // Display Price Information
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        double listPrice = order.getListPrice();
        double sellingPrice = order.getSellingPrice();

        listPriceText.setText(format.format(listPrice).replace(".00", ""));
        sellingPriceText.setText(format.format(sellingPrice).replace(".00", ""));
        totalPriceText.setText(format.format(sellingPrice).replace(".00", ""));

        // Update Order Timeline based on the current status
        updateOrderTimeline();
    }

    private void updateOrderTimeline() {
        // Default timeline colors
        int activeColor = getResources().getColor(R.color.AgrimartGreen);
        int inactiveColor = getResources().getColor(android.R.color.darker_gray);

        // Set current timestamps
        String timestamp = new SimpleDateFormat("EEE, dd MMMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date());

        // Default - set all to inactive
        confirmedIcon.setColorFilter(inactiveColor);
        pickedUpIcon.setColorFilter(inactiveColor);
        shippedIcon.setColorFilter(inactiveColor);
        outForDeliveryIcon.setColorFilter(inactiveColor);
        deliveredIcon.setColorFilter(inactiveColor);
        outForDeliveryLine.setBackgroundColor(inactiveColor);

        // Update UI based on current step
        switch (order.getStepNumber()) {
            case 4: // Delivered
                deliveredIcon.setColorFilter(activeColor);
                deliveredTimeText.setText(timestamp);
                // fall through
            case 3: // Out for Delivery
                outForDeliveryIcon.setColorFilter(activeColor);
                outForDeliveryLine.setBackgroundColor(activeColor);
                outForDeliveryTimeText.setText(order.getStepNumber() >= 3 ? timestamp : "");
                // fall through
            case 2: // Shipped
                shippedIcon.setColorFilter(activeColor);
                shippedTimeText.setText(order.getStepNumber() >= 2 ? timestamp : "");
                // fall through
            case 1: // Picked Up
                pickedUpIcon.setColorFilter(activeColor);
                pickedUpTimeText.setText(order.getStepNumber() >= 1 ? timestamp : "");
                // fall through
            case 0: // Confirmed
                confirmedIcon.setColorFilter(activeColor);
                confirmedTimeText.setText(timestamp);
                break;
        }
    }

    private void setupClickListeners() {
        // Back button click listener
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Call delivery agent click listener
        callDeliveryAgentCard.setOnClickListener(v -> {
            if (order != null && order.getDeliveryAgentPhone() != null && !order.getDeliveryAgentPhone().isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + order.getDeliveryAgentPhone()));
                startActivity(callIntent);
            }
        });
    }
}