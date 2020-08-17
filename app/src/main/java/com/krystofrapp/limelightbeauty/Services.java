package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Services extends AppCompatActivity {

    public static final String PRICE_MESSAGE = "Price Value";
    private TextView book1, book2, book3, book4, book5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_services);

        book1 = findViewById(R.id.book1);
        book2 = findViewById(R.id.book2);
        book3 = findViewById(R.id.book3);
        book4 = findViewById(R.id.book4);
        book5 = findViewById(R.id.book5);

        book1.setOnClickListener(view -> {
            Intent intent = new Intent(Services.this, BookAppointment.class);
            String message = book1.getText().toString().trim();
            intent.putExtra(PRICE_MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });
        book2.setOnClickListener(view -> {
            Intent intent = new Intent(Services.this, BookAppointment.class);
            String message = book2.getText().toString().trim();
            intent.putExtra(PRICE_MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });
        book3.setOnClickListener(view -> {
            Intent intent = new Intent(Services.this, BookAppointment.class);
            String message = book3.getText().toString().trim();
            intent.putExtra(PRICE_MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });
        book4.setOnClickListener(view -> {
            Intent intent = new Intent(Services.this, BookAppointment.class);
            String message = book4.getText().toString().trim();
            intent.putExtra(PRICE_MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });
        book5.setOnClickListener(view -> {
            Intent intent = new Intent(Services.this, BookAppointment.class);
            String message = book5.getText().toString().trim();
            intent.putExtra(PRICE_MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
