package com.example.remindme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText userName, reminderTitle, reminderDescription;
    Button saveReminder, statusReminder, editReminder, deleteReminder, reminderTime;
    //int hour, minute;
    SQLiteDatabase db;
    Cursor cursor;
    AlertDialog.Builder builder;
    StringBuffer buffer;
    //String packageName;
    Intent intent;

    TextView greetingText, userNameText, noReminders;
    private ImageView timeOfDayImage;
    private ReminderAdapter reminderAdapter;
    String userNameDisplay = "";
    String userID="";

    Spinner spinner;
    Boolean isOngoing = true;

    private MidnightResetBroadcast midnightResetBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUserDB();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");

        // Create and register the MidnightResetBroadcast receiver with the intent filter
        midnightResetBroadcast = new MidnightResetBroadcast();
        IntentFilter timeTickIntentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(midnightResetBroadcast, timeTickIntentFilter);

        Cursor cursor = db.rawQuery("SELECT * FROM loggedInTable WHERE loggedIn_status = 1", null);
        if (cursor != null && cursor.getCount() > 0) {

        } else {
            // If user_id = 1 does not exist in the database, start the OnboardingScreen activity
            Intent intent = new Intent(MainActivity.this, OnboardingScreen.class);
            startActivity(intent);
            finish();
        }

        noReminders = findViewById(R.id.noRemindersText);

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

        spinner = findViewById(R.id.dropwdown);

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

                Intent AddReminderIntent = new Intent(MainActivity.this, Add_Reminder.class);
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

                Toast.makeText(MainActivity.this, "You are now logged out", Toast.LENGTH_SHORT).show();

                Intent LogoutIntent = new Intent(MainActivity.this, Login.class);
                startActivity(LogoutIntent);
                finish();
            }
        });

        cursor = db.rawQuery("SELECT user_name FROM nameTable WHERE user_status = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex("user_name");
            if (userNameIndex != -1) {
                userNameDisplay = cursor.getString(userNameIndex);
                cursor.close(); // Close the cursor when done
                if (userNameDisplay != null) {
                    userNameText.setText("Hello, " + userNameDisplay + "!");
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
        displayReminders(isOngoing);

        spinner();

    }

    @Override
    protected void onDestroy() {
        // Unregister the receiver when the activity is destroyed
        unregisterReceiver(midnightResetBroadcast);

        super.onDestroy();
    }

    public void createUserDB(){
        userName=findViewById(R.id.createaccount_name_tv);
        reminderTitle=findViewById(R.id.reminderinput);
        reminderDescription=findViewById(R.id.descriptioninput);
        //reminderTime=(R.id.editTextTime);
        saveReminder=findViewById(R.id.submitbtn);

        builder = new AlertDialog.Builder(this);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS nameTable (user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, user_pass TEXT, user_status BOOLEAN not null default 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS loggedInTable (id INTEGER PRIMARY KEY, loggedIn_status BOOLEAN not null default 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS reminderTable (reminder_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, reminder_title TEXT, reminder_description TEXT, reminder_time TIME, reminder_status BOOLEAN not null default 0);");
        db.execSQL("INSERT OR IGNORE INTO loggedInTable(id, loggedIn_status) VALUES(1,0);");
    }

    public void displayMessage(String title, String message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void scheduleNotification(Intent notificationIntent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        long futureInMillis = System.currentTimeMillis() + 10000; // Adjust this time as needed

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    private Intent getNotification(String content) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("content", content);
        return intent;
    }


    private void displayReminders(boolean isOngoing) {
        List<Reminder> reminders = getRemindersFromDatabase(isOngoing);
        reminderAdapter = new ReminderAdapter(this, reminders);

        ListView remindersListView = findViewById(R.id.remindersListView);
        remindersListView.setAdapter(reminderAdapter);

    }

    public void onCompleteClicked(String reminderId) {
        // Handle "Complete" button click
        // Refresh the reminders and update the display
        displayReminders(true);
    }

    private List<Reminder> getRemindersFromDatabase(boolean isOngoing) {
        List<Reminder> reminders = new ArrayList<>();
        noReminders.setVisibility(View.GONE);

        try {
            String query = "SELECT * FROM reminderTable WHERE user_id='" + userID + "'";
            if (isOngoing) {
                query += " AND reminder_status=0";
            } else {
                query += " AND reminder_status=1";
            }

            Cursor cursor = db.rawQuery(query, null);

            reminders.clear();



            while (cursor.moveToNext()) {
                @SuppressLint("Range") String reminderTitle = cursor.getString(cursor.getColumnIndex("reminder_title"));
                @SuppressLint("Range") String reminderDescription = cursor.getString(cursor.getColumnIndex("reminder_description"));
                @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex("reminder_time"));
                @SuppressLint("Range") String reminderID = cursor.getString(cursor.getColumnIndex("reminder_id"));
                @SuppressLint("Range") String reminderStatus = cursor.getString(cursor.getColumnIndex("reminder_status"));

                Reminder reminder = new Reminder(reminderTitle, reminderDescription, reminderTime, reminderID, reminderStatus);
                reminders.add(reminder);
            }

            if (reminders.isEmpty()) {
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

    public void spinner (){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

// Set a listener to handle spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item, for example, filter the RecyclerView based on the selection
                String selectedOption = parentView.getItemAtPosition(position).toString();
                // Add your logic to filter the RecyclerView based on the selected option
                // You may need to update the data in the RecyclerView accordingly

                if ("Completed".equals(selectedOption)) {
                    isOngoing = false;
                } else {
                    isOngoing = true;
                }

                displayReminders(isOngoing);

                Log.d("HomeActivity", "Selected option: " + selectedOption);
                Log.d("HomeActivity", "isOngoing: " + isOngoing);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if nothing is selected
            }
        });
    }
}