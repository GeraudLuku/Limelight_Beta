package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);

        Button profile = findViewById(R.id.profile_btn);
        Button settings = findViewById(R.id.settings_btn);
        Button update_pwd = findViewById(R.id.update_pwd_btn);
        Button upgrade_account = findViewById(R.id.upgrade_acc_btn);
        Button logout_btn = findViewById(R.id.logout);

        profile.setOnClickListener(view -> userProfile());

        settings.setOnClickListener(view -> {
        });
        update_pwd.setOnClickListener(view -> {
            Intent intent = new Intent(AccountFragment.this, UpdatePassword.class);
            startActivity(intent);
        });
        upgrade_account.setOnClickListener(view -> upgradeUser());
        logout_btn.setOnClickListener(view -> logoutUser());


    }

    private void userProfile() {
        Intent profileIntent = new Intent(AccountFragment.this, UserProfile.class);
        startActivity(profileIntent);
    }

    private void upgradeUser() {
        Intent intent = new Intent(AccountFragment.this, UpgradeAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
        Intent signOutIntent = new Intent(AccountFragment.this, UserLogin.class);
        signOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        signOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signOutIntent);
        finish();
    }
}