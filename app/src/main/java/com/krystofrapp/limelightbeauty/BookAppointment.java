package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;

public class BookAppointment extends AppCompatActivity {

    public static final String PRICE_MESSAGE = "Price Value";
    public static final String TIME_MESSAGE = "Time Slot";
    private CardView confirmAppointment;
    private CalendarView calendarView;
    private TextView dateHolder;
    private EditText edtTime;
    private TextView priceTv;
    private ProgressBar progressBar;
    private TextView timeSlot1, timeSlot2, timeSlot3, timeSlot4, timeSlot5, timeSlot6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        String message = getIntent().getStringExtra(Services.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(message);

        timeSlot1 = findViewById(R.id.time_slot1);
        timeSlot2 = findViewById(R.id.time_slot2);
        timeSlot3 = findViewById(R.id.time_slot3);
        timeSlot4 = findViewById(R.id.time_slot4);
        timeSlot5 = findViewById(R.id.time_slot5);
        timeSlot6 = findViewById(R.id.time_slot6);

        edtTime = findViewById(R.id.edt_timePlace);
        edtTime.setInputType(InputType.TYPE_NULL);

        timeSlot1.setOnClickListener(view -> {
            String slot1 = timeSlot1.getText().toString().trim();
            edtTime.setText(slot1);
        });
        timeSlot2.setOnClickListener(view -> {
            String slot2 = timeSlot2.getText().toString().trim();
            edtTime.setText(slot2);

        });
        timeSlot3.setOnClickListener(view -> {
            String slot3 = timeSlot3.getText().toString().trim();
            edtTime.setText(slot3);

        });
        timeSlot4.setOnClickListener(view -> {
            String slot4 = timeSlot4.getText().toString().trim();
            edtTime.setText(slot4);
        });
        timeSlot5.setOnClickListener(view -> {
            String slot5 = timeSlot5.getText().toString().trim();
            edtTime.setText(slot5);
        });
        timeSlot6.setOnClickListener(view -> {
            String slot6 = timeSlot6.getText().toString().trim();
            edtTime.setText(slot6);
        });

        calendarView = findViewById(R.id.calendar);
        dateHolder = findViewById(R.id.date_holder);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            dateHolder.setText("");
            int m = month;
            int y = year;
            String dateConcat = dayOfMonth + "-" + m + "-" + y;
            dateHolder.append(dateConcat);
        });

        confirmAppointment = findViewById(R.id.cAppoint);

        confirmAppointment.setOnClickListener(view -> {
            String edt_time = edtTime.getText().toString().trim();
            if (edt_time.equals("")) {
                Snackbar.make(view, "Please select a Time Slot.", Snackbar.LENGTH_LONG)
                        .setAction("GOT IT", view1 -> {
                        })
                        .setActionTextColor(getResources().getColor(R.color.color_white))
                        .setDuration(Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.brown))
                        .show();
            } else {
                bookingBuilder();
            }
        });

    }

    private void bookingBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Are you sure about your selected appointment Date & Time?")
                .setPositiveButton("YES, PROCEED", (dialogInterface, i) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(BookAppointment.this, PaymentGateway.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    String priceText = priceTv.getText().toString().trim();
                    intent.putExtra(PRICE_MESSAGE, priceText);
                    String timeText = edtTime.getText().toString().trim();
                    intent.putExtra(TIME_MESSAGE, timeText);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    Toast.makeText(BookAppointment.this, "Date & Time Saved.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialogBuilder.show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        progressBar.setVisibility(View.GONE);
    }
}
