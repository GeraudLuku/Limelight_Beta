package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MoMoPayment extends AppCompatActivity {

    TextView priceTv;
   // private CardView btnMomo;
    private EditText transactionId, momoPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momo_payment);

        String message = getIntent().getStringExtra(BookAppointment.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(message);

        CardView btnMomo = findViewById(R.id.btn_momo);
        transactionId = findViewById(R.id.transaction_id);
        momoPhoneNumber = findViewById(R.id.momo_phone);

        btnMomo.setOnClickListener(view -> paymentBuilder());
    }

    private void paymentBuilder() {
        final String transId = transactionId.getText().toString().trim();
        final String momo_phone = momoPhoneNumber.getText().toString().trim();

        if (momo_phone.isEmpty()) {
            momoNumberErrorBuilder();
        } else if (momo_phone.length() < 9) {
            Toast.makeText(MoMoPayment.this, "Number too short. Should be 9 digits exact.", Toast.LENGTH_SHORT).show();
        } else if (momo_phone.length() > 9) {
            Toast.makeText(MoMoPayment.this, "Number too long. Should be 9 digits exact.", Toast.LENGTH_SHORT).show();
        } else if (transId.isEmpty()) {
            transIdErrorBuilder();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setTitle("PAYMENT ALERT!")
                    .setMessage("Are you sure you've entered the correct •Transaction ID• of your Booked Appointment?")
                    .setPositiveButton("YES", (dialogInterface, i) -> new AlertDialog.Builder(MoMoPayment.this)
                            .setTitle("CONFIRMATION!")
                            .setCancelable(false)
                            .setMessage("Please note that, if you entered a wrong ''Transaction ID'', your •Booked Appointment• will not be valid and will be subject to cancellation.")
                            .setPositiveButton("CONFIRM", (dialogInterface1, i1) -> {
                                Toast.makeText(MoMoPayment.this, "PAYMENT RECORDED! You will receive a confirmation Email shortly", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MoMoPayment.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("CANCEL", (dialogInterface12, i12) -> {
                                Toast.makeText(MoMoPayment.this, "Please verify before submission.", Toast.LENGTH_LONG).show();
                                dialogInterface12.cancel();
                            })
                            .show())
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                        Toast.makeText(MoMoPayment.this, "Please verify before submission.", Toast.LENGTH_LONG).show();

                        dialogInterface.cancel();
                    })
                    .show();
        }
    }

    private void momoNumberErrorBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Please enter the •MTN Mobile Money• account number for this Payment.")
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void transIdErrorBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Please, get the ''Transaction ID'' of the payment sms you received from •MTN Mobile Money• after making this payment and enter it below.")
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

}