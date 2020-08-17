package com.krystofrapp.limelightbeauty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import java.util.Objects;

public class UpdatePassword extends AppCompatActivity {

    TextInputEditText update_email;
    private FirebaseAuth auth;
    private ContentLoadingProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);

        auth = FirebaseAuth.getInstance();
        Button update_password = findViewById(R.id.update_password);
        update_password.setOnClickListener(view -> changePassword());

        loadingProgressBar = findViewById(R.id.prgrsbar);
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void changePassword() {
        update_email = findViewById(R.id.update_email);
        final String emailPwd = Objects.requireNonNull(update_email.getText()).toString().trim();
        if (emailPwd.isEmpty()) {
            Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
        } else {

            loadingProgressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(emailPwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            changePasswordBuilder();

                        } else if (!task.isSuccessful()) {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Toast.makeText(UpdatePassword.this, "User doesn't exist.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });

        }
    }

    private void changePasswordBuilder() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Check Your Email.")
                .setPositiveButtonIcon(this.getResources().getDrawable(R.drawable.ic_check))
                .setCancelable(false)
                .setMessage("We have sent you an email with instructions to change your password.")
                .setPositiveButton("GOT IT", (dialogInterface, i) -> finish())
                .show();
    }

}
