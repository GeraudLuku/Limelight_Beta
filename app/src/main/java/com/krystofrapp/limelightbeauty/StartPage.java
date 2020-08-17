package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;

public class StartPage extends AppCompatActivity {

    private CardView register, businessAccountBtn;
    private TextView toLogin;
    private FirebaseAuth mAuth;
    private AuthStateListener authStateListener;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("En");

        authStateListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                Intent intent = new Intent(StartPage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                //Toast.makeText(StartPage.this, "User doesn't exist.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(StartPage.this, UserLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };

        bar = findViewById(R.id.progressbar);
        bar.setVisibility(View.INVISIBLE);
        register = findViewById(R.id.custom_signup);
        businessAccountBtn = findViewById(R.id.businessAccount);

        register.setOnClickListener(view -> registerUser());

        businessAccountBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartPage.this, BusinessAccountSignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        toLogin = findViewById(R.id.goto_login);
        toLogin.setOnClickListener(view -> {
            Intent intent = new Intent(StartPage.this, UserLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });
        toLogin.setOnLongClickListener(view -> {
            intentBuilder();
            return true;
        });
    }
    private void registerUser() {
        Intent intent = new Intent(StartPage.this, CustomSignUp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void intentBuilder() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Go to Login Page?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    Intent intent = new Intent(StartPage.this, UserLogin.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialogBuilder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        register.setEnabled(true);
        businessAccountBtn.setEnabled(true);
        toLogin.setEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null) {
            mAuth.addAuthStateListener(authStateListener);
        }
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
        //if the user is already signed in
        //Update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(StartPage.this, HomeFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(StartPage.this, CustomSignUp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (googleSignInAccount != null) {
                // callAlertDialogBuilder();
                Intent intent = new Intent(StartPage.this, HomeFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sign in!", Toast.LENGTH_SHORT).show();
            }
        }
        */

}
