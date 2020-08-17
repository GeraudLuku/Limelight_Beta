package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    private TextInputEditText resetEmailpassword;
    private CardView sendBtn;
    //private FirebaseAuth auth;
    private ProgressBar contentLoadingProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //auth = FirebaseAuth.getInstance();

        contentLoadingProgressBar = findViewById(R.id.prgrsbar);
        contentLoadingProgressBar.setVisibility(View.GONE);

        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(view -> changePassword());
    }


    private void changePassword() {
        resetEmailpassword = findViewById(R.id.reset_email_pwd);
        final String emailPwd = Objects.requireNonNull(resetEmailpassword.getText()).toString().trim();
        if (emailPwd.isEmpty()) {
            Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
        } else {
            resetEmailpassword.setEnabled(false);
            sendBtn.setEnabled(false);
            contentLoadingProgressBar.setVisibility(View.VISIBLE);
            FirebaseAuth
                    .getInstance()
                    .sendPasswordResetEmail(emailPwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            contentLoadingProgressBar.setVisibility(View.GONE);
                            changePasswordBuilder();
                            resetEmailpassword.setEnabled(true);
                            sendBtn.setEnabled(true);
                        } if (!task.isSuccessful()) {
                            Log.d(TAG, "changePassword: ", task.getException());
                            Toast.makeText(this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            /*
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Toast.makeText(ForgotPassword.this, "User doesn't exist.", Toast.LENGTH_SHORT).show();
                                contentLoadingProgressBar.setVisibility(View.GONE);
                                invalidUser();
                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                               // Log.d(TAG, "onComplete: Malformed email");
                                contentLoadingProgressBar.setVisibility(View.GONE);
                                errorBuilder();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            */
                        }

                    })
            .addOnFailureListener(e -> {
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    Log.d(TAG, "changePassword: badly formed email address." );
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    errorBuilder();
                    resetEmailpassword.setEnabled(true);
                    sendBtn.setEnabled(true);
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    Log.d(TAG, "changePassword: User doesn't exist.");
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    invalidUserBuilder();
                    resetEmailpassword.setEnabled(true);
                    sendBtn.setEnabled(true);
                } else if (e instanceof FirebaseNetworkException) {
                    Log.d(TAG, "changePassword: Network connection error");
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    networkBuilder();
                    resetEmailpassword.setEnabled(true);
                    sendBtn.setEnabled(true);
                }
            });

        }
    }

    private void errorBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Wrongly Formatted Email ID!");
        alertDialogBuilder.setMessage("Correct format: your_name@gmail.com, " + "OR" + " your_name@yahoo.com")
                .setPositiveButton("OKAY", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    resetEmailpassword.setEnabled(true);
                    sendBtn.setEnabled(true);
                }).show();
    }

    private void invalidUserBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("This User Doesn't Exist With Us.")
                .setPositiveButton("SIGN UP", (dialogInterface, i) -> {
                    Intent intent = new Intent(ForgotPassword.this, CustomSignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    resetEmailpassword.setEnabled(true);
                    sendBtn.setEnabled(true);
                })
                .setNegativeButton("TRY AGAIN", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }


    private void changePasswordBuilder() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Check Your Email!")
                .setPositiveButtonIcon(this.getResources().getDrawable(R.drawable.ic_check))
                .setCancelable(false)
                .setMessage("We have sent you instructions to change your password. Use it here to Login.")
                .setPositiveButton("GOT IT", (dialogInterface, i) -> {
                    Intent intent = new Intent(ForgotPassword.this, UserLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .show();
    }

    private void invalidUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Invalid User!")
                .setPositiveButtonIcon(this.getResources().getDrawable(R.drawable.ic_check))
                .setMessage("Email doesn't exist.")
                .setCancelable(false)
                .setPositiveButton("SIGN UP", (dialogInterface, i) -> {
                    Intent intent = new Intent(ForgotPassword.this, CustomSignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("TRY AGAIN", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    resetEmailpassword.setEnabled(true);
                    sendBtn.setEnabled(true);
                })
                .show();
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

}
