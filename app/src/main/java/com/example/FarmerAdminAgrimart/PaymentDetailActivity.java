package com.example.FarmerAdminAgrimart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String amount = intent.getStringExtra("amount");

        TextView nameTextView = findViewById(R.id.detail_name);
        TextView dateTextView = findViewById(R.id.detail_date);
        TextView amountTextView = findViewById(R.id.detail_amount);

        nameTextView.setText(name);
        dateTextView.setText(date);
        amountTextView.setText(amount);

        // Make the back button work
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }
} 