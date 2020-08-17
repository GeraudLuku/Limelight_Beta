package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpgradeAccount extends AppCompatActivity {

    private TextInputEditText busName, phoneNumber, bsAddress, nkName;
    private MaterialCheckBox facial, nails, hair, massage;
    private Button register;
    private FirebaseFirestore upgradeAccountDetails = FirebaseFirestore.getInstance();
    private ContentLoadingProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_account);

        //Fields
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
        String bus_name = busName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String address = bsAddress.getText().toString().trim();
        String nk_name = nkName.getText().toString().trim();

       /* String face = facial.getText().toString().trim();
        String nail = nails.getText().toString().trim();
        String hair_dress = hair.getText().toString().trim();
        String massage_sess = massage.getText().toString().trim();  */


        if (bus_name.isEmpty()) {
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

            loadingProgressBar.setVisibility(View.VISIBLE);
            ProfileDetails businessDetails = new ProfileDetails(bus_name, phone, address, nk_name);
            upgradeAccountDetails.collection("Business Accounts").document("Account Details").set(businessDetails)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            loadingProgressBar.setVisibility(View.GONE);
                            register.setEnabled(false);
                            successDialogBuilder();
                        }
                        else {
                            loadingProgressBar.setVisibility(View.GONE);
                           showFailureDialog();
                        }
                    });
        }
    }

    private void successDialogBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Upgrade successful.")
                .setPositiveButton("Proceed", (dialogInterface, i) -> {
                    Intent intent = new Intent(UpgradeAccount.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }).show();
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(UpgradeAccount.this)
                .setMessage("Failed To Process Upgrade! Try Again.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }
}
