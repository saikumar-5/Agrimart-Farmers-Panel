package com.example.FarmerAdminAgrimart;

public class Notification {
    public static final int TYPE_QUERY_ANSWERED = 1;
    public static final int TYPE_NEW_ORDER = 2;
    public static final int TYPE_PAYMENT = 3;
    public static final int TYPE_GOVT_SCHEME = 4;

    private int type;
    private String message;
    private boolean isRead;

    public Notification(int type, String message, boolean isRead) {
        this.type = type;
        this.message = message;
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getIcon() {
        switch (type) {
            case TYPE_QUERY_ANSWERED:
                return "💬";
            case TYPE_NEW_ORDER:
                return "📦";
            case TYPE_PAYMENT:
                return "💰";
            case TYPE_GOVT_SCHEME:
                return "🏛";
            default:
                return "📩";
        }
    }
}