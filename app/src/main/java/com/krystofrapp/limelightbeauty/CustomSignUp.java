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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class CustomSignUp extends AppCompatActivity {

    //Tag for the logs optional
    private static final String TAG = "auth_sign_in";
    //A constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
   // private static final String TAG = "CustomSignUp";
    private GoogleSignInClient mGoogleSignInClient;
    private CardView signUpBtn, googleSignin;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextInputEditText name, email, password, confirmPassword;
    private TextView toLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signUpBtn = findViewById(R.id.signup_btn);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.confirm_password);
        toLogin = findViewById(R.id.go_to_login);

        toLogin.setOnClickListener(view -> {
            Intent intent = new Intent(CustomSignUp.this, UserLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });
        toLogin.setOnLongClickListener(view -> {
            intentBuilder();
            return true;
        });
        signUpBtn.setOnClickListener(view -> signUpUser());
        googleSignin = findViewById(R.id.google_signin);
        googleSignin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            googleSignIn();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account;
                //Google Sign In was successful, authenticate with Firebase
                account = signInAccountTask.getResult(ApiException.class);
                //authenticating with firebase
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google SignIn Failed: ",e );
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        Toast.makeText(this, ""+acct.getId(), Toast.LENGTH_LONG).show();

        //getting the auth credential
        AuthCredential google_credential = GoogleAuthProvider.getCredential(acct.getId(), null);

        //Now using firebase we are signing in the user here
       FirebaseAuth
               .getInstance()
               .signInWithCredential(google_credential)
               .addOnCompleteListener(this, task -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (task.isSuccessful() && user != null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        signUpBtn.setEnabled(false);
                        name.setEnabled(false);
                        email.setEnabled(false);
                        password.setEnabled(false);
                        confirmPassword.setEnabled(false);
                        toLogin.setEnabled(false);
                        Intent intent = new Intent(CustomSignUp.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "signInWithCredential: Success.");
                        Toast.makeText(CustomSignUp.this, "Google Sign In Successful.", Toast.LENGTH_LONG).show();
                    }else if (!task.isSuccessful()) {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Failed with exception"+task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "signInWithCredential: failure", task.getException());
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseNetworkException) {
                        Toast.makeText(CustomSignUp.this, "Google Sign In Failed: ", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        networkBuilder();
                    }
                });
    }

    @SuppressWarnings("ConstantConditions")
    private void signUpUser() {
         String username = name.getText().toString().trim();
         String user_email = email.getText().toString().trim();
         String pwd = password.getText().toString().trim();
         String cpwd = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(user_email) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cpwd)){
            Toast.makeText(this, "All fields are required to Sign•Up for an account!", Toast.LENGTH_SHORT).show();
        }
        else if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (user_email.isEmpty()) {
            Toast.makeText(this, "Your email is required", Toast.LENGTH_SHORT).show();
        } else if (pwd.isEmpty()) {
            Toast.makeText(this, "Your password is required", Toast.LENGTH_SHORT).show();
        } else if (pwd.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long in length", Toast.LENGTH_SHORT).show();
        } else if (cpwd.isEmpty()) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
        } else if (!cpwd.equals(pwd)) {
            Toast.makeText(this, "Passwords do not match! Please check again", Toast.LENGTH_SHORT).show();
        }

        else {
            progressBar.setVisibility(View.VISIBLE);
            signUpBtn.setEnabled(false);
            name.setEnabled(false);
            email.setEnabled(false);
            password.setEnabled(false);
            confirmPassword.setEnabled(false);
            toLogin.setEnabled(false);
                FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(user_email.trim(), pwd.trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CustomSignUp.this, "Sign•Up • Successful.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CustomSignUp.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                signUpBtn.setEnabled(true);
                                name.setEnabled(true);
                                email.setEnabled(true);
                                password.setEnabled(true);
                                confirmPassword.setEnabled(true);
                                toLogin.setEnabled(true);
                                startActivity(intent);
                                finish();
                            }else if (!task.isSuccessful()) {
                                Toast.makeText(this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "signUpUser: ", task.getException());
                                /*
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    Log.d(TAG, "onComplete: Malformed email");
                                    progressBar.setVisibility(View.GONE);
                                    errorBuilder();
                                    signUpBtn.setEnabled(true);
                                    name.setEnabled(true);
                                    email.setEnabled(true);
                                    password.setEnabled(true);
                                    confirmPassword.setEnabled(true);
                                    toLogin.setEnabled(true);
                                } catch (FirebaseAuthUserCollisionException emailExists) {
                                    Log.d(TAG, "onComplete: emails already exists");
                                    Toast.makeText(CustomSignUp.this, "Email already exists, LOGIN.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(CustomSignUp.this, UserLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                */
                            }
                        })
                .addOnFailureListener(e -> {
                    if (e instanceof  FirebaseAuthUserCollisionException) {
                        Log.d(TAG, "onComplete: emails already exists");
                        progressBar.setVisibility(View.GONE);
                        emailExists();
                        signUpBtn.setEnabled(true);
                        name.setEnabled(true);
                        email.setEnabled(true);
                        password.setEnabled(true);
                        confirmPassword.setEnabled(true);
                        toLogin.setEnabled(true);
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Log.d(TAG, "onComplete: Malformed email");
                        progressBar.setVisibility(View.GONE);
                        errorBuilder();
                        signUpBtn.setEnabled(true);
                        name.setEnabled(true);
                        email.setEnabled(true);
                        password.setEnabled(true);
                        confirmPassword.setEnabled(true);
                        toLogin.setEnabled(true);
                    } else if (e instanceof FirebaseNetworkException) {
                        Log.d(TAG, "signUpUser: Network Exception");
                        progressBar.setVisibility(View.GONE);
                        networkBuilder();
                        signUpBtn.setEnabled(true);
                        name.setEnabled(true);
                        email.setEnabled(true);
                        password.setEnabled(true);
                        confirmPassword.setEnabled(true);
                        toLogin.setEnabled(true);
                    }
                });
        }
    }

    private void errorBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Badly Formatted Email ID!");
        alertDialogBuilder.setMessage("Correct format: •your_name@gmail.com• " + "OR" + "•your_name@yahoo.com•")
                .setPositiveButton("OKAY", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    private void intentBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Go to Login Page?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    Intent intent = new Intent(CustomSignUp.this, UserLogin.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }
    private void emailExists() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Email Already Exists.")
                .setPositiveButton("LOGIN", (dialogInterface, i) -> {
                    Intent intent = new Intent(CustomSignUp.this, UserLogin.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                })
                .setNegativeButton("TRY AGAIN", (dialogInterface, i) -> dialogInterface.dismiss()).show();
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
        signUpBtn.setEnabled(true);
        name.setEnabled(true);
        email.setEnabled(true);
        password.setEnabled(true);
        confirmPassword.setEnabled(true);
        toLogin.setEnabled(true);
    }
}
