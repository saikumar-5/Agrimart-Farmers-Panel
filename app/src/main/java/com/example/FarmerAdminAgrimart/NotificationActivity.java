package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up RecyclerView
        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(this);
        notificationsRecyclerView.setAdapter(adapter);

        // Load notifications
        loadNotifications();
    }

    private void loadNotifications() {
        // For demonstration, we'll create sample notifications
        // In a real app, you would fetch these from a database or API
        List<Notification> unreadNotifications = new ArrayList<>();
        List<Notification> readNotifications = new ArrayList<>();

        // Unread notifications
        unreadNotifications.add(new Notification(
                Notification.TYPE_QUERY_ANSWERED,
                "Your query has been answered! Check the response now.",
                false));

        unreadNotifications.add(new Notification(
                Notification.TYPE_NEW_ORDER,
                "New order received! Check the details and start preparing.",
                false));

        unreadNotifications.add(new Notification(
                Notification.TYPE_PAYMENT,
                "Payment received! Your earnings have been credited.",
                false));

        unreadNotifications.add(new Notification(
                Notification.TYPE_GOVT_SCHEME,
                "Govt scheme update! Apply now for financial assistance.",
                false));

        // Read notifications
        readNotifications.add(new Notification(
                Notification.TYPE_QUERY_ANSWERED,
                "Your query has been answered! Check the response now.",
                true));

        readNotifications.add(new Notification(
                Notification.TYPE_NEW_ORDER,
                "New order received! Check the details and start preparing.",
                true));

        // Set data to adapter
        adapter.setData(unreadNotifications, readNotifications);
    }
}