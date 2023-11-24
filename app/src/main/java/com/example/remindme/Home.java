package com.example.remindme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;

import java.util.Calendar;

public class Home extends AppCompatActivity {

    TextView greetingText, userNameText;
    Cursor cursor;
    SQLiteDatabase db;
    private ImageView timeOfDayImage;

    private ReminderAdapter reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Intent intent = getIntent();
        if (intent != null) {
            String enteredUsername = intent.getStringExtra("username");
            if (enteredUsername != null) {
                TextView userNameTextView = findViewById(R.id.userNameText);
                userNameTextView.setText("Welcome, " + enteredUsername + "!");
            }
        }*/

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);


        // Assuming you have a TextView with the id "greetingText" in your layout
        greetingText = findViewById(R.id.greetingText);

        // Assuming you have a TextView with the id "userNameText" in your layout
        userNameText = findViewById(R.id.userNameText);

        // Assuming you have an ImageView with the id "timeOfDayImage" in your layout
        timeOfDayImage = findViewById(R.id.timeOfDayImage);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Get the user name from the input screen (assuming it's passed through Intent)
        //String confirmUser = getIntent().getStringExtra("confirmUser");
        //String confirmUser =

        Cursor cursor = db.rawQuery("SELECT user_login FROM nameTable WHERE user_status = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex("user_login");
            if (userNameIndex != -1) {
                String userName = cursor.getString(userNameIndex);
                cursor.close(); // Close the cursor when done
                if (userName != null) {
                    userNameText.setText("Hello, " + userName + "!");
                }
            }
        }
        else {
            // Handle the case where the cursor is null or empty
            // You might want to set a default value for the username or show an error message
        }


        // Set the greeting and image based on the time of day
        if (hourOfDay >= 6 && hourOfDay < 12) {
            greetingText.setText("Good Morning,");
            timeOfDayImage.setImageResource(R.drawable.morning);
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greetingText.setText("Good Afternoon,");
            timeOfDayImage.setImageResource(R.drawable.afternoon);
        } else {
            greetingText.setText("Good Evening,");
            timeOfDayImage.setImageResource(R.drawable.evening);
        }

        // Display reminders in the ListView
        displayReminders();
    }

    private void displayReminders() {
        List<Reminder> reminders = getRemindersFromDatabase();
        // Assuming you have a ListView with the id "remindersListView" in your layout
        reminderAdapter = new ReminderAdapter(this, reminders);

        ListView remindersListView = findViewById(R.id.remindersListView);
        remindersListView.setAdapter(reminderAdapter);
    }

    private List<Reminder> getRemindersFromDatabase() {
        List<Reminder> reminders = new ArrayList<>();


        try {
            Cursor cursor = db.rawQuery("SELECT * FROM reminderTable", null);
                while (cursor.moveToNext()) {
                    String reminderTitleIndex = String.valueOf(cursor.getColumnIndex("reminder_title"));
                    String reminderDescriptonIndex = String.valueOf(cursor.getColumnIndex("reminder_descripton"));
                    String reminderTimeIndex = String.valueOf(cursor.getColumnIndex("reminder_time"));

                    Reminder reminder = new Reminder(reminderTitleIndex, reminderDescriptonIndex, reminderTimeIndex);
                    reminders.add(reminder);
                }
        } catch (Exception e) {
            // Handle exceptions
        } finally {
            db.close();
        }

        return reminders;
    }

}
