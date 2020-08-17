package com.krystofrapp.limelightbeauty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class BusinessAccountSignUp extends AppCompatActivity {

    private static final String TAG = "BusinessAccountSignUp";
    private TextInputEditText email, password, busName, phoneNumber, bsAddress, nkName;
    private MaterialCheckBox facial, nails, hair, massage;
    private CardView register;
    private FirebaseFirestore upgradeAccountDetails = FirebaseFirestore.getInstance();
    private ContentLoadingProgressBar loadingProgressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_account_sign_up);

        auth = FirebaseAuth.getInstance();
        //Fields
        email =  findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        busName = findViewById(R.id.business_name);
        phoneNumber = findViewById(R.id.phone);
        bsAddress = findViewById(R.id.address);
        nkName = findViewById(R.id.nk_name);
        //Services
        facial = findViewById(R.id.facials);
        nails = findViewById(R.id.nails);
        hair = findViewById(R.id.hair);
        massage = findViewById(R.id.massage);
        loadingProgressBar = findViewById(R.id.progress);
        loadingProgressBar.setVisibility(View.GONE);

        register = findViewById(R.id.register);

        register.setOnClickListener(view -> registerBusiness());
    }

    @SuppressWarnings("ConstantConditions")
    private void registerBusiness() {
        final String email_address = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String bus_name = busName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String address = bsAddress.getText().toString().trim();
        String nk_name = nkName.getText().toString().trim();
       /* String face = facial.getText().toString().trim();
        String nail = nails.getText().toString().trim();
        String hair_dress = hair.getText().toString().trim();
        String massage_sess = massage.getText().toString().trim();  */

        if (email_address.isEmpty() && pwd.isEmpty() && bus_name.isEmpty() && phone.isEmpty() && address.isEmpty() && nk_name.isEmpty()) {
            emptyFieldsDialog();
            loadingProgressBar.setVisibility(View.GONE);
        } else if (bus_name.isEmpty()) {
            Toast.makeText(this, "Set your business name", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            Toast.makeText(this, "Enter your business phone number", Toast.LENGTH_SHORT).show();
        } else if (phone.length() > 9) {
            Toast.makeText(this, "Phone number too long. Should be 9 digits exact!", Toast.LENGTH_LONG).show();
        } else if (phone.length() < 9) {
            Toast.makeText(this, "Phone number too short. Should be 9 digits exact!.", Toast.LENGTH_LONG).show();
        } else if (address.isEmpty()) {
            Toast.makeText(this, "Set your business address", Toast.LENGTH_SHORT).show();
        } else if (nk_name.isEmpty()) {
            Toast.makeText(this, "Enter a nickname", Toast.LENGTH_SHORT).show();
        } else if (!facial.isChecked() && !nails.isChecked() && !hair.isChecked() && !massage.isChecked()) {
            Toast.makeText(this, "You must check at least one service you offer.", Toast.LENGTH_SHORT).show();
        } else if (bus_name.isEmpty() || phone.isEmpty() || address.isEmpty() || nk_name.isEmpty()) {
            Toast.makeText(this, "All details are required to create a Business Account", Toast.LENGTH_SHORT).show();
        } else {
            email.setEnabled(false);
            password.setEnabled(false);
            busName.setEnabled(false);
            phoneNumber.setEnabled(false);
            bsAddress.setEnabled(false);
            nkName.setEnabled(false);
            register.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(email_address, pwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            loadingProgressBar.setVisibility(View.VISIBLE);
                            ProfileDetails businessDetails = new ProfileDetails(bus_name, phone, address, nk_name);
                            upgradeAccountDetails
                                    .collection("Business Accounts")
                                    .document("Account Details")
                                    .set(businessDetails, SetOptions.merge())
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            loadingProgressBar.setVisibility(View.GONE);
                                            register.setEnabled(true);
                                            Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(BusinessAccountSignUp.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            loadingProgressBar.setVisibility(View.GONE);
                                            register.setEnabled(true);
                                            Toast.makeText(this, ""+task1.getException(), Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "registerBusiness: "+task1.getException());
                                            showFailureDialog();
                                        }
                                    });
                        } else if (!task.isSuccessful()) {
                            Log.d(TAG, "registerBusiness: "+task.getException());
                            Toast.makeText(this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Log.d(TAG, "onComplete: emails already exists");
                            loadingProgressBar.setVisibility(View.GONE);
                            emailExists();
                            email.setEnabled(true);
                            password.setEnabled(true);
                            busName.setEnabled(true);
                            phoneNumber.setEnabled(true);
                            bsAddress.setEnabled(true);
                            nkName.setEnabled(true);
                            register.setEnabled(true);
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.d(TAG, "onComplete: Malformed email");
                            loadingProgressBar.setVisibility(View.GONE);
                            errorBuilder();
                            email.setEnabled(true);
                            password.setEnabled(true);
                            busName.setEnabled(true);
                            phoneNumber.setEnabled(true);
                            bsAddress.setEnabled(true);
                            nkName.setEnabled(true);
                            register.setEnabled(true);
                        } else if (e instanceof FirebaseNetworkException) {
                            Log.d(TAG, "signUpUser: Network Exception");
                            loadingProgressBar.setVisibility(View.GONE);
                            networkBuilder();
                            email.setEnabled(true);
                            password.setEnabled(true);
                            busName.setEnabled(true);
                            phoneNumber.setEnabled(true);
                            bsAddress.setEnabled(true);
                            nkName.setEnabled(true);
                            register.setEnabled(true);
                        }
                    });
        }
        }

    private void showFailureDialog() {
        new AlertDialog.Builder(BusinessAccountSignUp.this)
                .setMessage("Failed To Process Sign Up! Try Again.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }
    private void emptyFieldsDialog() {
        new AlertDialog.Builder(BusinessAccountSignUp.this)
                .setMessage("All input fields are required to •Sign Up•")
                .setCancelable(false)
                .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss()).show();
    }
    private void emailExists() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Email Already Exists.")
                .setPositiveButton("LOGIN", (dialogInterface, i) -> {
                    Intent intent = new Intent(BusinessAccountSignUp.this, UserLogin.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                })
                .setNegativeButton("TRY AGAIN", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }
    private void errorBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Badly Formatted Email ID!");
        alertDialogBuilder.setMessage("Correct format: •your_name@gmail.com• " + "OR" + "•your_name@yahoo.com•")
                .setPositiveButton("OKAY", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }
    private void networkBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Connection Alert!");
        alertDialogBuilder.setMessage("Check if you have an internet connection.")
                .setPositiveButton("OKAY", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                }).show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        email.setEnabled(true);
        password.setEnabled(true);
        busName.setEnabled(true);
        phoneNumber.setEnabled(true);
        bsAddress.setEnabled(true);
        nkName.setEnabled(true);
        register.setEnabled(true);
    }
}