package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PaymentGateway extends AppCompatActivity {

    public static final String PRICE_MESSAGE = "Price Value";
    TextView priceTv, timeTv;
    private CardView OM, MOMO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        String price_message = getIntent().getStringExtra(BookAppointment.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(price_message);

        String time_message = getIntent().getStringExtra(BookAppointment.TIME_MESSAGE);
        timeTv = findViewById(R.id.time_tv);
        timeTv.setText(time_message);

        OM = findViewById(R.id.btn_om);
        MOMO = findViewById(R.id.btn_momo);

        OM.setOnClickListener(view -> {
            if (view == OM) {
                Intent intent = new Intent(PaymentGateway.this, OmPayment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String priceText = priceTv.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, priceText);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });

        MOMO.setOnClickListener(view -> {
            if (view == MOMO) {
                Intent intent = new Intent(PaymentGateway.this, MoMoPayment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String priceText = priceTv.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, priceText);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });
    }
}