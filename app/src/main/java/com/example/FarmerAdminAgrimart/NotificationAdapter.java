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

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_NOTIFICATION = 1;

    private final Context context;
    private final List<Object> items;
    private int unreadCount = 0;
    private int readCount = 0;

    public NotificationAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    public void setData(List<Notification> unreadNotifications, List<Notification> readNotifications) {
        items.clear();

        // Update counts
        unreadCount = unreadNotifications.size();
        readCount = readNotifications.size();

        if (!unreadNotifications.isEmpty()) {
            items.add("unread");
            items.addAll(unreadNotifications);
        }

        if (!readNotifications.isEmpty()) {
            items.add("read");
            items.addAll(readNotifications);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_NOTIFICATION;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
            return new NotificationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            String headerText = (String) items.get(position);

            if (headerText.equals("unread")) {
                headerHolder.headerText.setText("is Unread");
                headerHolder.countText.setText("(" + unreadCount + ")");
                headerHolder.indicatorDot.setVisibility(View.VISIBLE);
            } else {
                headerHolder.headerText.setText("Read");
                headerHolder.countText.setText("(" + readCount + ")");
//                headerHolder.indicatorDot.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof NotificationViewHolder) {
            NotificationViewHolder notificationHolder = (NotificationViewHolder) holder;
            Notification notification = (Notification) items.get(position);

            notificationHolder.notificationIcon.setText(notification.getIcon());
            notificationHolder.notificationText.setText(notification.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;
        TextView countText;
        TextView indicatorDot;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.headerText);
            countText = itemView.findViewById(R.id.countText);
            indicatorDot = itemView.findViewById(R.id.indicatorDot);
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationIcon;
        TextView notificationText;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationIcon = itemView.findViewById(R.id.notificationIcon);
            notificationText = itemView.findViewById(R.id.notificationText);
        }
    }
}