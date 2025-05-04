package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PaymentsPage extends Fragment {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private List<PaymentItem> paymentList;
    private List<PaymentItem> filteredPaymentList;
    private EditText searchEditText;

    public PaymentsPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments_page, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.payments_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Back button functionality
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Initialize payment list
        initPaymentList();

        // Initialize filtered list with all items
        filteredPaymentList = new ArrayList<>(paymentList);

        // Set up the adapter
        adapter = new PaymentAdapter(filteredPaymentList);
        recyclerView.setAdapter(adapter);

        // Set up search functionality
        searchEditText = view.findViewById(R.id.search_payments);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPayments(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        return view;
    }

    private void filterPayments(String query) {
        filteredPaymentList.clear();

        if (query.isEmpty()) {
            // If search query is empty, show all payments
            filteredPaymentList.addAll(paymentList);
        } else {
            String lowerCaseQuery = query.toLowerCase();

            // Filter payments based on name or amount
            for (PaymentItem payment : paymentList) {
                if (payment.getName().toLowerCase().contains(lowerCaseQuery) ||
                        payment.getAmount().toLowerCase().contains(lowerCaseQuery) ||
                        payment.getDate().toLowerCase().contains(lowerCaseQuery)) {
                    filteredPaymentList.add(payment);
                }
            }
        }

        // Notify adapter that data has changed
        adapter.notifyDataSetChanged();
    }

    private void initPaymentList() {
        paymentList = new ArrayList<>();

        paymentList.add(new PaymentItem(
                "Sohan Gupta",
                "15 March 2025",
                "₹4,120"));
        paymentList.add(new PaymentItem(
                "Mohan Gupta",
                "8 March 2025",
                "₹1,980"));
        paymentList.add(new PaymentItem(
                "Rahul Gupta",
                "2 March 2025",
                "₹3,841"));
        for (int i = 0; i < 10; i++) {
            paymentList.add(new PaymentItem(
                    "Rohan Gupta",
                    "12 Feb 2025",
                    "₹4,500"));
        }
    }

    // Payment item class
    public static class PaymentItem {
        private String name;
        private String date;
        private String amount;

        public PaymentItem(String name, String date, String amount) {
            this.name = name;
            this.date = date;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public String getAmount() {
            return amount;
        }
    }

    // Payment adapter class
    private class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
        private List<PaymentItem> paymentList;

        public PaymentAdapter(List<PaymentItem> paymentList) {
            this.paymentList = paymentList;
        }

        @Override
        public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.payment_item, parent, false);
            return new PaymentViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(PaymentViewHolder holder, int position) {
            PaymentItem payment = paymentList.get(position);
            holder.nameTextView.setText(payment.getName());
            holder.dateTextView.setText(payment.getDate());
            holder.amountTextView.setText(payment.getAmount());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PaymentDetailActivity.class);
                    intent.putExtra("name", payment.getName());
                    intent.putExtra("date", payment.getDate());
                    intent.putExtra("amount", payment.getAmount());
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return paymentList.size();
        }

        public class PaymentViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView dateTextView;
            public TextView amountTextView;
            public TextView receivedTextView;
            public TextView creditedTextView;

            public PaymentViewHolder(View view) {
                super(view);
                nameTextView = view.findViewById(R.id.payment_name);
                dateTextView = view.findViewById(R.id.payment_date);
                amountTextView = view.findViewById(R.id.payment_amount);
                receivedTextView = view.findViewById(R.id.received_from_text);
                creditedTextView = view.findViewById(R.id.credited_to_text);
            }
        }
    }
}