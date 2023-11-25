package com.example.remindme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Home extends AppCompatActivity {

    TextView greetingText, userNameText, noReminders;
    Cursor cursor;
    SQLiteDatabase db;
    private ImageView timeOfDayImage;
    private ReminderAdapter reminderAdapter;
    String userName = "";
    String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        noReminders = findViewById(R.id.noRemindersText);

        /*Intent intent = getIntent();
        if (intent != null) {
            String enteredUsername = intent.getStringExtra("username");
            if (enteredUsername != null) {
                TextView userNameTextView = findViewById(R.id.userNameText);
                userNameTextView.setText("Welcome, " + enteredUsername + "!");
            }
        }*/

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        Cursor userId_cursor = db.rawQuery("SELECT user_id FROM nameTable WHERE user_status = 1", null);
        if (userId_cursor != null && userId_cursor.moveToFirst()) {
            int userNameIndex = userId_cursor.getColumnIndex("user_id");
            if (userNameIndex != -1) {
                userID = userId_cursor.getString(userNameIndex);
                userId_cursor.close(); // Close the cursor when done
            }
        }


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


        FloatingActionButton fab_AddReminder = findViewById(R.id.fabAddReminder);
        FloatingActionButton fab_Logout = findViewById(R.id.fabLogout);

        fab_AddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent AddReminderIntent = new Intent(Home.this, Add_Reminder.class);
                AddReminderIntent.putExtra("userID", userID);
                startActivity(AddReminderIntent);

            }
        });

        fab_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
                db.execSQL("UPDATE loggedInTable SET loggedIn_status = 0 WHERE id=1");
                db.execSQL("UPDATE nameTable SET user_status = 0 WHERE user_status = 1");

                Toast.makeText(Home.this, "You are now logged out", Toast.LENGTH_SHORT).show();

                Intent LogoutIntent = new Intent(Home.this, Login.class);
                startActivity(LogoutIntent);
            }
        });

        Cursor cursor = db.rawQuery("SELECT user_name FROM nameTable WHERE user_status = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex("user_name");
            if (userNameIndex != -1) {
                userName = cursor.getString(userNameIndex);
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
            greetingText.setText("Good Evening,");
            timeOfDayImage.setImageResource(R.drawable.evening);
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greetingText.setText("Good Afternoon,");
            timeOfDayImage.setImageResource(R.drawable.afternoon);
        } else {
            greetingText.setText("Good Morning,");
            timeOfDayImage.setImageResource(R.drawable.morning);
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
        noReminders.setVisibility(View.GONE);

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM reminderTable WHERE user_id='" + userID + "'", null);
            if (cursor.moveToNext()) {
                String reminderTitle = cursor.getString(cursor.getColumnIndex("reminder_title"));
                String reminderDescription = cursor.getString(cursor.getColumnIndex("reminder_description"));
                String reminderTime = cursor.getString(cursor.getColumnIndex("reminder_time"));
                String reminderID = cursor.getString(cursor.getColumnIndex("reminder_id"));

                Reminder reminder = new Reminder(reminderTitle, reminderDescription, reminderTime, reminderID);
                reminders.add(reminder);
            }else {

                noReminders.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            // Handle exceptions
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return reminders;
    }

}
