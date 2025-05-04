package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up back button
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Set up RecyclerView
        notificationsRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new NotificationAdapter(requireContext());
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