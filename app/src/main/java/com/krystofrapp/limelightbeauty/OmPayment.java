package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OmPayment extends AppCompatActivity {

    TextView priceTv;
    //private CardView btnOm;
    private EditText transactionId, omPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_om_payment);

        String message = getIntent().getStringExtra(BookAppointment.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(message);

       CardView btnOm = findViewById(R.id.btn_om);
        btnOm.setOnClickListener(view -> {
            omPhoneNumber = findViewById(R.id.om_phone_number);
            transactionId = findViewById(R.id.transaction_id);
            final String om_phone = omPhoneNumber.getText().toString().trim();
            final String transId = transactionId.getText().toString().trim();

            if (om_phone.isEmpty()) {
                omNumberErrorBuilder();
            } else if (om_phone.length() < 9) {
                Toast.makeText(OmPayment.this, "Number too short. Should be 9 digits exact.", Toast.LENGTH_SHORT).show();
            } else if (om_phone.length() > 9) {
                Toast.makeText(OmPayment.this, "Number too long. Should be 9 digits exact.", Toast.LENGTH_SHORT).show();
            } else if (transId.isEmpty()) {
                transIdErrorBuilder();
            } else {
                paymentBuilder();
            }
        });
    }

    private void paymentBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("PAYMENT ALERT!")
                .setMessage("Are you sure you've entered the correct •Transaction ID• of your Booked Appointment?")
                .setPositiveButton("YES", (dialogInterface, i) -> new AlertDialog.Builder(OmPayment.this)
                        .setTitle("CONFIRMATION!")
                        .setCancelable(false)
                        .setMessage("Please note that, if you entered a wrong ''Transaction ID'', your •Booked Appointment• will not be valid and may be subject to cancellation. DO YOU WISH TO PROCEED TO SUBMISSION.?")
                        .setPositiveButton("CONFIRM", (dialogInterface1, i1) -> {
                            Toast.makeText(OmPayment.this, "PAYMENT RECORDED! You will receive a confirmation Email shortly.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(OmPayment.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("CANCEL", (dialogInterface12, i12) -> {
                            Toast.makeText(OmPayment.this, "Please verify before submission.", Toast.LENGTH_LONG).show();
                            dialogInterface12.cancel();
                        })
                        .show())
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                    Toast.makeText(OmPayment.this, "Please verify before submission.", Toast.LENGTH_LONG).show();

                    dialogInterface.dismiss();
                })
                .show();
    }


    private void omNumberErrorBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Please enter your •Orange Money• account number for this Payment.")
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void transIdErrorBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Please, get the ''Transaction ID'' of the payment sms you received from •Orange Money• after making this payment and enter it below.")
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

}