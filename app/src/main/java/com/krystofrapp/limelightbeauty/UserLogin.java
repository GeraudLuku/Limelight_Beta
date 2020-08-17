package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class UserLogin extends AppCompatActivity {

    private static final String TAG = "UserLogin";

    private ProgressBar progressBar;
    private TextView tv_to_signUp, txtChangePassword;
    private CardView login;
    private TextInputEditText email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.cl_progressbar);
        progressBar.setVisibility(View.GONE);

        tv_to_signUp = findViewById(R.id.to_sign_up);
        login = findViewById(R.id.customer_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login.setOnClickListener(view -> {
            if (view == login) {
                loginCustomer();
            }
        });

        tv_to_signUp.setOnClickListener(view -> toSignUp());

        txtChangePassword = findViewById(R.id.reset_pwd);
        txtChangePassword.setOnClickListener(view -> {
            Intent intent = new Intent(UserLogin.this, ForgotPassword.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void toSignUp() {
        Intent intent = new Intent(UserLogin.this, CustomSignUp.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    @SuppressWarnings("ConstantConditions")
    private void loginCustomer() {
        String cust_email = email.getText().toString().trim();
        String cust_pwd = password.getText().toString().trim();

        //Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error);
        //customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        if (TextUtils.isEmpty(cust_email) && TextUtils.isEmpty(cust_pwd)) {
            Toast.makeText(this, "Enter your email and password to login.", Toast.LENGTH_SHORT).show();
        } else if (cust_email.isEmpty()) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        } else if (cust_pwd.isEmpty()) {
            Toast.makeText(this, "Password required to login", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            email.setEnabled(false);
            password.setEnabled(false);
            tv_to_signUp.setEnabled(false);
            txtChangePassword.setEnabled(false);
            FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(cust_email.trim(), cust_pwd.trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Log.d(TAG, "loginCustomer: ", task.getException());
                            Toast.makeText(UserLogin.this, "Sign-in Successful", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                            login.setEnabled(true);
                            email.setEnabled(true);
                            password.setEnabled(true);
                            tv_to_signUp.setEnabled(true);
                            txtChangePassword.setEnabled(true);
                        }
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "loginCustomer: ", task.getException());
                            Toast.makeText(this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                /*
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Toast.makeText(UserLogin.this, "This email doesn't exist with us", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: Email doesn't exist");
                                    login.setEnabled(true);
                                    email.setEnabled(true);
                                    password.setEnabled(true);
                                    tv_to_signUp.setEnabled(true);
                                    txtChangePassword.setEnabled(true);
                                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    Toast.makeText(UserLogin.this, "Invalid or wrong password.", Toast.LENGTH_SHORT).show();
                                    login.setEnabled(true);
                                    email.setEnabled(true);
                                    password.setEnabled(true);
                                    tv_to_signUp.setEnabled(true);
                                    txtChangePassword.setEnabled(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                */
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UserLogin.this, "User Not Found. Create A New Account. ", Toast.LENGTH_LONG).show();
                            emailOrPasswordExceptionBuilder();
                            Log.d(TAG, "onComplete: User doesn't exist");
                            login.setEnabled(true);
                            email.setEnabled(true);
                            password.setEnabled(true);
                            tv_to_signUp.setEnabled(true);
                            txtChangePassword.setEnabled(true);

                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UserLogin.this, "Password invalid. Enter a valid password.", Toast.LENGTH_LONG).show();
                            errorBuilder();
                            login.setEnabled(true);
                            email.setEnabled(true);
                            password.setEnabled(true);
                            tv_to_signUp.setEnabled(true);
                            txtChangePassword.setEnabled(true);

                        } else if (e instanceof FirebaseNetworkException) {
                            progressBar.setVisibility(View.GONE);
                            networkBuilder();
                            login.setEnabled(true);
                            email.setEnabled(true);
                            password.setEnabled(true);
                            tv_to_signUp.setEnabled(true);
                            txtChangePassword.setEnabled(true);
                        }
                    });
        }
        }

    private void errorBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Password not valid for this Account.")
                .setPositiveButton("TRY AGAIN.", (dialogInterface, i) -> dialogInterface.dismiss())
                .setNegativeButton("SIGN UP", ((dialogInterface, i) ->
                        startActivity(new Intent(UserLogin.this, CustomSignUp.class)))).show();
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
    private void emailOrPasswordExceptionBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("•User not found• OR •Wrongly inputted email address•");
        alertDialogBuilder.setMessage("Valid Format: •your_name@gmail.com• " + " OR " + "•your_name@yahoo.com•")
                .setPositiveButton("TRY AGAIN", (dialogInterface, i) -> dialogInterface.dismiss())
                .setNegativeButton("CREATE NEW ACCOUNT?", (dialogInterface, i) ->
                        startActivity(new Intent(UserLogin.this, CustomSignUp.class))).show();
    }

}