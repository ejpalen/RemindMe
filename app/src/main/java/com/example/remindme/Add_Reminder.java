package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class Add_Reminder extends AppCompatActivity {

    Button backButton;

    FrameLayout backButtonFrame;
    EditText descriptionInput;
    EditText reminderInput;
    Button selecttime;
    int hour, minute;
    SQLiteDatabase db;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");


        // Reference XML elements
        backButtonFrame = findViewById(R.id.backButtonFrame);
        descriptionInput = findViewById(R.id.descriptioninput);
        reminderInput = findViewById(R.id.reminderinput);
        selecttime = findViewById(R.id.selecttime);
        submitButton = findViewById(R.id.submitbtn);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        // Set click listener for the back button
        backButtonFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Reminder.this,
                        MainActivity.class);
                finish();
                startActivity(intent);

            }
        });

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle submit button click
                // Add your logic here

                db.execSQL("INSERT INTO reminderTable(user_id, reminder_title, reminder_description, reminder_time) VALUES('" + userID + "', '" + reminderInput.getText().toString() + "', '" + descriptionInput.getText().toString() + "', '" + selecttime.getText().toString() + "')");
                Toast.makeText(Add_Reminder.this, "New Reminder Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_Reminder.this,
                        Home.class);
                startActivity(intent);
            }
        });
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                selecttime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}