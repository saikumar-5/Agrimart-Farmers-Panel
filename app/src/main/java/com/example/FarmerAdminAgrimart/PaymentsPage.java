package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PaymentsPage extends Fragment {

    public PaymentsPage() {
        // Required empty public constructor
    }

    public static PaymentsPage newInstance(String param1, String param2) {
        PaymentsPage fragment = new PaymentsPage();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Read params if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payments_page, container, false);
    }
}
