package com.example.FarmerAdminAgrimart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText editFullName,editDateOfBirth,editAadharNumber,editMobileNumber,editEmail,editPassword,editFarmSize,editFullAddress,editVillage,editDistrict,editState,editPincode,editBankAccountHolderName,editBankAccountNumber,editIfscCode,editPreferredLanguage,editAlternatePhoneNumber,editWhatsappNumber;
    CheckBox agreementCheckBox;
    AutoCompleteTextView editGender;
    private TextInputEditText cropInputEditText;
    private ChipGroup selectedCropsChipGroup;
    private Button btnSignup;
    private FrameLayout progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        AutoCompleteTextView genderDropdown = findViewById(R.id.editGender);
        editFullName = findViewById(R.id.editFullname);
        editDateOfBirth = findViewById(R.id.editDateOfBirth);
        editGender = findViewById(R.id.editGender);
        editAadharNumber = findViewById(R.id.editAadhar);
        editMobileNumber = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editFarmSize = findViewById(R.id.editfarmSize);
        editFullAddress = findViewById(R.id.editfullAddress);
        editVillage = findViewById(R.id.editvillage);
        editDistrict = findViewById(R.id.editdistrict);
        editState = findViewById(R.id.editstate);
        editPincode = findViewById(R.id.editpinCode);
        editBankAccountHolderName = findViewById(R.id.editbankAccountHolderName);
        editBankAccountNumber = findViewById(R.id.editbankAccountNumber);
        editIfscCode = findViewById(R.id.editIFSCCode);
        editPreferredLanguage = findViewById(R.id.editpreferredLanguage);
        editAlternatePhoneNumber = findViewById(R.id.editalternatePhoneNumber);
        editWhatsappNumber = findViewById(R.id.editwhatsappNumber);
        btnSignup = findViewById(R.id.signinbtn);
        progressBar = findViewById(R.id.progressOverlay);

        String[] genderOptions = new String[] {"Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.simple_dropdown_item_1line, // Your custom layout
                genderOptions
        );
        genderDropdown.setAdapter(adapter);
        genderDropdown.setOnClickListener(v -> genderDropdown.showDropDown());

        cropInputEditText = findViewById(R.id.cropInputEditText);
        selectedCropsChipGroup = findViewById(R.id.selectedCropsChipGroup);

        // On adding a crop type to the list
        cropInputEditText.setOnEditorActionListener((v, actionId, event) -> {
            String inputText = cropInputEditText.getText().toString();

            if (!TextUtils.isEmpty(inputText)) {
                String[] crops = inputText.split(",");
                for (String crop : crops) {
                    String trimmedCrop = crop.trim(); // Clean extra spaces
                    addChip(trimmedCrop);
                }
                cropInputEditText.setText(""); // Clear input field
            } else {
                Toast.makeText(SignupActivity.this, "Please enter at least one crop", Toast.LENGTH_SHORT).show();
            }
            getSelectedCropTypes();

            return true;
        });

        btnSignup.setOnClickListener(v->registerUser());

    }

    private void addChip(String crop) {
        // Check if the crop has already been added
        if (isCropAlreadyAdded(crop)) {
            Toast.makeText(this, crop + " is already added", Toast.LENGTH_SHORT).show();
            return;
        }

        Chip chip = new Chip(this);
        chip.setText(crop);
        chip.setCloseIconVisible(true);
        chip.setChipBackgroundColorResource(R.color.AgrimartGreen);
        chip.setTextColor(getResources().getColor(android.R.color.white));
        chip.setCloseIconTintResource(android.R.color.white);

        chip.setOnCloseIconClickListener(v -> {
            selectedCropsChipGroup.removeView(chip); // Remove chip when close icon is clicked
        });

        selectedCropsChipGroup.addView(chip); // Add chip to the group
    }

    private List<String> getSelectedCropTypes() {
        List<String> cropList = new ArrayList<>();
        int chipCount = selectedCropsChipGroup.getChildCount();

        for (int i = 0; i < chipCount; i++) {
            View chipView = selectedCropsChipGroup.getChildAt(i);
            if (chipView instanceof Chip) {
                Chip chip = (Chip) chipView;
                cropList.add(chip.getText().toString());
            }
        }

        Log.d("SelectedCrops", "Crops: " + cropList.toString());

        return cropList;
    }


    private boolean isCropAlreadyAdded(String crop) {
        for (int i = 0; i < selectedCropsChipGroup.getChildCount(); i++) {
            Chip existingChip = (Chip) selectedCropsChipGroup.getChildAt(i);
            if (existingChip.getText().toString().equalsIgnoreCase(crop)) {
                return true;
            }
        }
        return false;
    }

    private void registerUser() {
        String fullname = editFullName.getText().toString().trim();
        String mobile = editMobileNumber.getText().toString().trim();
        String gender = editGender.getText().toString().trim();
        String dateOfBirth = editDateOfBirth.getText().toString().trim();
        String aadharNumber = editAadharNumber.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String farmSize = editFarmSize.getText().toString().trim();
        List<String> cropTypes = getSelectedCropTypes(); // a method to return selected crops as List<String>

        String fullAddress = editFullAddress.getText().toString().trim();
        String village = editVillage.getText().toString().trim();
        String district = editDistrict.getText().toString().trim();
        String state = editState.getText().toString().trim();
        String pincode = editPincode.getText().toString().trim();

        String accountHolderName = editBankAccountHolderName.getText().toString().trim();
        String accountNumber = editBankAccountNumber.getText().toString().trim();
        String ifscCode = editIfscCode.getText().toString().trim();

        String preferredLanguage =editPreferredLanguage.getText().toString();
        String altPhone = editAlternatePhoneNumber.getText().toString().trim();
        String whatsapp = editWhatsappNumber.getText().toString().trim();


        // Validation
        if (TextUtils.isEmpty(fullname)) {
            editFullName.setError("Full name is required");
            return;
        }
        if (TextUtils.isEmpty(mobile) || mobile.length() != 10) {
            editMobileNumber.setError("Enter a valid 10-digit mobile number");
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

                            Map<String, Object> farmerData = new HashMap<>();

                            Map<String, Object> personal = new HashMap<>();
                            personal.put("fullname", fullname);
                            personal.put("gender", gender);
                            personal.put("dateofbirth", dateOfBirth);
                            personal.put("aadharnumber", aadharNumber);
                            personal.put("mobilenumber", mobile);
                            personal.put("email address", email);
                            personal.put("password", password);

                            Map<String, Object> farm = new HashMap<>();
                            farm.put("farmsize", farmSize);
                            farm.put("croptype", cropTypes);

                            Map<String, Object> location = new HashMap<>();
                            location.put("Fulladdress", fullAddress);
                            location.put("village", village);
                            location.put("district", district);
                            location.put("State", state);
                            location.put("pincode", pincode);

                            Map<String, Object> bank = new HashMap<>();
                            bank.put("BankAccountHolderName", accountHolderName);
                            bank.put("BankAccountNumber", accountNumber);
                            bank.put("IFSC Code", ifscCode);

                            Map<String, Object> communication = new HashMap<>();
                            communication.put("prefferedLanguage", preferredLanguage);
                            communication.put("AlternativePhoneNumber", altPhone);
                            communication.put("WhatsappNumber", whatsapp);

                            farmerData.put("personal_details", personal);
                            farmerData.put("farm_details", farm);
                            farmerData.put("location_details", location);
                            farmerData.put("payment_bank_details", bank);
                            farmerData.put("communication_support_details", communication);
                            farmerData.put("uid", user.getUid());


                            db.collection("Farmers").document(user.getUid()).set(farmerData)
                                    .addOnSuccessListener(aVoid -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                        goToHomeScreen();
                                    })
                                    .addOnFailureListener(e -> {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(SignupActivity.this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                    );
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