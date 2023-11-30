package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class EditReminder extends AppCompatActivity {


    Button backButton;

    FrameLayout backButtonFrame;
    EditText descriptionInput;
    EditText reminderInput;
    Button selecttime;
    int hour, minute;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        // Reference XML elements
        backButtonFrame = findViewById(R.id.backButtonFrame);
        descriptionInput = findViewById(R.id.descriptioninput);
        reminderInput = findViewById(R.id.reminderinput);
        selecttime = findViewById(R.id.selecttime);
        submitButton = findViewById(R.id.submitbtn);


        String reminderTitle = "", reminderDescription = "", reminderTime="";

        Intent intent = getIntent();
        String reminderID = intent.getStringExtra("reminderID");



        SQLiteDatabase db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        Cursor reminder_cursor = db.rawQuery("SELECT * FROM reminderTable WHERE reminder_id = '" + reminderID +"'", null);
        if (reminder_cursor != null && reminder_cursor.moveToFirst()) {
                reminderTitle = reminder_cursor.getString(reminder_cursor.getColumnIndex("reminder_title"));
                reminderDescription = reminder_cursor.getString(reminder_cursor.getColumnIndex("reminder_description"));
                reminderTime = reminder_cursor.getString(reminder_cursor.getColumnIndex("reminder_time"));
                reminder_cursor.close(); // Close the cursor when done
        }

        descriptionInput = findViewById(R.id.descriptioninput);
        reminderInput = findViewById(R.id.reminderinput);
        selecttime = findViewById(R.id.selecttime);
        submitButton = findViewById(R.id.submitbtn);


        reminderInput.setText(reminderTitle);
        descriptionInput.setText(reminderDescription);

        // Parse the time from the database and format it
        String[] timeComponents = reminderTime.split(":");
        int hours = Integer.parseInt(timeComponents[0]);
        int minutes = Integer.parseInt(timeComponents[1]);

        selecttime.setText(String.format(Locale.getDefault(), "%02d:%02d", hours, minutes));


        // Set click listener for the back button
        backButtonFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditReminder.this,
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

                db.execSQL("UPDATE reminderTable SET reminder_title = '" + reminderInput.getText().toString() + "', " +
                        "reminder_description = '" + descriptionInput.getText().toString() + "', " +
                        "reminder_time = '" + selecttime.getText().toString() + "' " +
                        "WHERE reminder_id = '" + reminderID + "'");

                Toast.makeText(EditReminder.this, "Reminder has been edited", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(EditReminder.this,
                        MainActivity.class);
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