package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomersPage extends Fragment {

    private EditText searchEditText;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;

    public CustomersPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customers_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEditText = view.findViewById(R.id.searchEditText);
        backButton = view.findViewById(R.id.backbtnid);
        recyclerView = view.findViewById(R.id.customersRecyclerView);

        if (backButton == null || searchEditText == null || recyclerView == null) {
            Log.e("CustomersPage", "One or more views not found. Check layout IDs.");
            return;
        }

        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerList = generateDummyCustomers();
        customerAdapter = new CustomerAdapter(getContext(), customerList);
        recyclerView.setAdapter(customerAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerAdapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private List<Customer> generateDummyCustomers() {
        List<Customer> list = new ArrayList<>();
        list.add(new Customer("Amit Sharma", "9876543210", 1200.0, 4, "2023-01-15"));
        list.add(new Customer("Priya Verma", "9823456789", 3000.0, 6, "2022-09-22"));
        list.add(new Customer("Rajesh Singh", "9812345678", 750.0, 2, "2024-03-10"));
        list.add(new Customer("Sneha Kapoor", "9900123456", 2200.0, 5, "2021-12-05"));
        list.add(new Customer("Manish Patel", "9988776655", 1750.0, 3, "2020-07-30"));
        list.add(new Customer("Deepika Rao", "9123456789", 4000.0, 8, "2023-04-18"));
        list.add(new Customer("Karan Mehta", "9870011223", 1500.0, 4, "2022-11-11"));
        return list;
    }
}
