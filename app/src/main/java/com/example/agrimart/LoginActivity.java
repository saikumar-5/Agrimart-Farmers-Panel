package com.example.agrimart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_GOOGLE_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private ImageButton btnGoogleLogin;
    private TextView goToSignup;
    private FrameLayout progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private List<String> profileNames = Arrays.asList("Pepper","Nala","Mittens","Sassy","Sugar","Socks","Bear","Garfield","Lucky","Gracie","Tinkerbell","Max","Midnight","Jack","Felix","Lily","Bandit","Milo","Lucy","Boots","Bailey","Chloe","Cuddles","Oscar","Annie","Luna","Kiki","Baby","Lola","Sasha","Zoey","Jack","Angel","Jasper","Misty","Sammy","Peanut","Kitty","Dusty","Casper","Lily","Boo");
    private List<String> profileCollections = Arrays.asList("micah", "notionists");

    private String generateRandomProfileImg() {
        Random rand = new Random();
        String seed = profileNames.get(rand.nextInt(profileNames.size()));
        String collection = profileCollections.get(rand.nextInt(profileCollections.size()));
        return "https://api.dicebear.com/6.x/" + collection + "/svg?seed=" + seed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editEmail = findViewById(R.id.emailEditText);
        editPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginbtn);
        btnGoogleLogin = findViewById(R.id.googlelogin);
        goToSignup = findViewById(R.id.createAccountBtn);
        progressBar = findViewById(R.id.progressOverlay);

        btnLogin.setOnClickListener(v -> loginUser());

        goToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        btnGoogleLogin.setOnClickListener(v -> googleLogin());
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        goToHomeScreen();
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(LoginActivity.this, "No account found with this email.", Toast.LENGTH_LONG).show();
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void googleLogin() {
        Intent signInIntent = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this,
                new com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                        com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()).getSignInIntent();

        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            progressBar.setVisibility(View.VISIBLE);
            com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount> task =
                    com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                com.google.android.gms.auth.api.signin.GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            String email = user.getEmail();

                            db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
                                if (doc.exists()) {
                                    goToHomeScreen();
                                } else {
                                    String image = generateRandomProfileImg();
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("name", name);
                                    userData.put("email", email);
                                    userData.put("profile_img", image);

                                    db.collection("users").document(uid).set(userData)
                                            .addOnSuccessListener(unused -> goToHomeScreen())
                                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show());
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fragment_profile", "profile");
        startActivity(intent);
        finish();
    }
}