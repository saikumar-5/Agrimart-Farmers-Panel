package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button profileAuthBtn;
    private FirebaseAuth mAuth;
    private LinearLayout userDetailsLayout, btnLayout;
    private TextView userName, userLocation;
    private ImageView profileImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        profileAuthBtn = view.findViewById(R.id.profileLoginBtn);
        userName = view.findViewById(R.id.userName);
        userLocation = view.findViewById(R.id.userLocation);
        userDetailsLayout = view.findViewById(R.id.userDetailsLayout);
        btnLayout = view.findViewById(R.id.btnLayout);
        profileImage = view.findViewById(R.id.profileImage);

        profileAuthBtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        return view;
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // User is logged in, show details
            userDetailsLayout.setVisibility(View.VISIBLE);
            btnLayout.setVisibility(View.GONE);

            userName.setText(user.getDisplayName() != null ? user.getDisplayName() : "User");
            userLocation.setText(user.getEmail() != null ? user.getEmail() : "No Email");
            String uid = user.getUid();

            FirebaseFirestore.getInstance().collection("Farmers").document(uid)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            String name = doc.getString("fullname");
                            String imageUrl = doc.getString("profile_img");

                            userName.setText(name != null ? name : "User");

                            // Load profile image from imageUrl using Glide
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Log.d("ProfileDebug", "Image URL: " + imageUrl); // Debug line

                                Glide.with(requireContext())
                                        .load(imageUrl)
                                        .placeholder(R.drawable.profile_pic)
                                        .into(profileImage);
                            }

                        } else {
                            userName.setText("User");
                            profileImage.setImageResource(R.drawable.profile_pic);
                        }
                    })
                    .addOnFailureListener(e -> {
                        userName.setText("User");
                        profileImage.setImageResource(R.drawable.profile_pic);
                    });
        } else {
            // User is not logged in
            userDetailsLayout.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(); // Update UI when the fragment is resumed
    }
}