package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find all the blocks by ID
        LinearLayout salesBlock = view.findViewById(R.id.salesblock);
        LinearLayout productsBlock = view.findViewById(R.id.productsblock);
        LinearLayout ordersBlock = view.findViewById(R.id.ordersblock);
        LinearLayout customersBlock = view.findViewById(R.id.customersblock);
        LinearLayout paymentsBlock = view.findViewById(R.id.paymentsblock);
        LinearLayout queriesBlock = view.findViewById(R.id.queriesblock);

        // Set click listeners for each block
//        salesBlock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to Analytics using its nav ID
//                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
//                navController.navigate(R.id.analyticsFragment); // <-- Make sure this ID is correct in your nav_graph.xml
//
//                // Change selected item in BottomNavigationView
//                BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
//                navBar.setSelectedItemId(R.id.analyticsFragment); // same ID as the nav menu item
//            }
//        });


        productsBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new ProductPage());
            }
        });

        ordersBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new OrdersPage());
            }
        });

        customersBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new CustomersPage());
            }
        });

        paymentsBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new PaymentsPage());
            }
        });

        queriesBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new QueriesPage());
            }
        });

        return view;
    }

    // Helper function to open a new fragment
    private void openFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
