package com.example.FarmerAdminAgrimart;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView notificationicon= view.findViewById(R.id.notificon);
        notificationicon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openFragment(new NotificationFragment());
            }
        });
        // Find all the blocks by ID
        LinearLayout salesBlock = view.findViewById(R.id.salesblock);
        LinearLayout productsBlock = view.findViewById(R.id.productsblock);
        LinearLayout ordersBlock = view.findViewById(R.id.ordersblock);
        LinearLayout customersBlock = view.findViewById(R.id.customersblock);
        LinearLayout paymentsBlock = view.findViewById(R.id.paymentsblock);
        LinearLayout queriesBlock = view.findViewById(R.id.queriesblock);

        // Set click listeners for each block
        salesBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Analytics fragment using the same pattern as other fragments
                openFragment(new WishlistFragment());

                // Update the bottom navigation
                BottomNavigationView navBar = requireActivity().findViewById(R.id.bottom_navigation);
                navBar.setSelectedItemId(R.id.nav_products); // Use your actual menu item ID
            }
        });

        productsBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Analytics fragment using the same pattern as other fragments
                openFragment(new ProductFragment());

                // Update the bottom navigation
                BottomNavigationView navBar = requireActivity().findViewById(R.id.bottom_navigation);
                navBar.setSelectedItemId(R.id.nav_wishlist); // Use your actual menu item ID
            }
        });

        ordersBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new OrdersPage());
                BottomNavigationView navBar = requireActivity().findViewById(R.id.bottom_navigation);
                navBar.setSelectedItemId(R.id.nav_cart);
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