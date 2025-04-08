package com.example.agrimart;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText editFullName, editMobile, editEmail, editPassword;
    CheckBox agreementCheckBox;
    TextView agreementText;
    private Button btnSignup;
    private FrameLayout progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth & Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Link UI Elements
        editFullName = findViewById(R.id.fullnameEditText);
        editMobile = findViewById(R.id.mobileEditText);
        agreementText = findViewById(R.id.agreementText);
        editEmail = findViewById(R.id.emailEditText);
        editPassword = findViewById(R.id.passwordEditText);
        agreementCheckBox = findViewById(R.id.agreementCheckBox);
        btnSignup = findViewById(R.id.signinbtn);
        progressBar = findViewById(R.id.progressOverlay);

        String text = "I've read and agreed to User Agreement and Privacy Policy";
        SpannableString ss = new SpannableString(text);

// Correct index positions
        int startUserAgreement = text.indexOf("User Agreement");
        int endUserAgreement = startUserAgreement + "User Agreement".length();

        int startPrivacyPolicy = text.indexOf("Privacy Policy");
        int endPrivacyPolicy = startPrivacyPolicy + "Privacy Policy".length();

        ClickableSpan userAgreementClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignupActivity.this, "User Agreement Clicked", Toast.LENGTH_SHORT).show();
            }
        };

        ClickableSpan privacyPolicyClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignupActivity.this, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show();
            }
        };
        ss.setSpan(userAgreementClick, startUserAgreement, endUserAgreement, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(privacyPolicyClick, startPrivacyPolicy, endPrivacyPolicy, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        agreementText.setText(ss);
        agreementText.setMovementMethod(LinkMovementMethod.getInstance());

        btnSignup.setOnClickListener(view -> registerUser());

    }

    private void registerUser() {
        String fullname = editFullName.getText().toString().trim();
        String mobile = editMobile.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Check if agreement checkbox is checked
        if (!agreementCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept the User Agreement and Privacy Policy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validation
        if (TextUtils.isEmpty(fullname)) {
            editFullName.setError("Full name is required");
            return;
        }
        if (TextUtils.isEmpty(mobile) || mobile.length() != 10) {
            editMobile.setError("Enter a valid 10-digit mobile number");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname)
                                    .build();
                            user.updateProfile(profileUpdates);

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("fullName", fullname);
                            userData.put("mobile", mobile);
                            userData.put("email", email);
                            userData.put("uid", user.getUid());

                            db.collection("users").document(user.getUid()).set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                        goToHomeScreen();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Exception exception = task.getException();
                        if (exception != null) {
                            handleSignupError(exception);
                        }
                    }
                });
    }

    /**
     * Handle Firebase signup errors.
     */
    private void handleSignupError(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            // Email already registered
            Toast.makeText(SignupActivity.this, "This email is already in use. Please log in.", Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthWeakPasswordException) {
            // Weak password
            Toast.makeText(SignupActivity.this, "Weak password! Use at least 6 characters.", Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // Invalid email format
            Toast.makeText(SignupActivity.this, "Invalid email format.", Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthException) {
            // Generic Firebase auth error
            Toast.makeText(SignupActivity.this, "Signup failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            // Other errors
            Toast.makeText(SignupActivity.this, "An error occurred: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void goToHomeScreen() {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(SignupActivity.this, MainActivity.class); // Your activity that hosts ProfileFragment
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fragment_profile", "profile"); // Pass data to indicate which fragment to open
        startActivity(intent);
        finish();
    }
}