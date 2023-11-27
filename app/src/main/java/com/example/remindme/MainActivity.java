package com.example.remindme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUserDB();

        //packageName = getApplicationContext().getPackageName().concat(".");
        //broadcastIntent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");

        Cursor cursor = db.rawQuery("SELECT * FROM loggedInTable WHERE loggedIn_status = 1", null);
        if (cursor != null && cursor.getCount() > 0) {
            // If user_id = 1 exists in the database, start the Home activity
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        } else {
            // If user_id = 1 does not exist in the database, start the OnboardingScreen activity
            Intent intent = new Intent(MainActivity.this, OnboardingScreen.class);
            startActivity(intent);
        }

    }

    /*public void broadcastIntent(){
        intent = new Intent();
        intent.setAction(packageName + "MY_CUSTOM_ACTION");
        intent.setClass(this, MyCustomReceiver.class);
        sendBroadcast(intent);
    }
    }*/

    public void createUserDB(){
        userName=findViewById(R.id.createaccount_name_tv);
        reminderTitle=findViewById(R.id.reminderinput);
        reminderDescription=findViewById(R.id.descriptioninput);
        //reminderTime=(R.id.editTextTime);
        saveReminder=findViewById(R.id.submitbtn);

        builder = new AlertDialog.Builder(this);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
//        db.execSQL("DROP TABLE IF EXISTS nameTable;");
//        db.execSQL("DROP TABLE IF EXISTS loggedInTable;");
//        db.execSQL("DROP TABLE IF EXISTS reminderTable;");
        db.execSQL("CREATE TABLE IF NOT EXISTS nameTable (user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, user_pass TEXT, user_status BOOLEAN not null default 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS loggedInTable (id INTEGER PRIMARY KEY, loggedIn_status BOOLEAN not null default 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS reminderTable (reminder_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, reminder_title TEXT, reminder_description TEXT, reminder_time TIME, reminder_status BOOLEAN not null default 0);");

        db.execSQL("INSERT OR IGNORE INTO loggedInTable(id, loggedIn_status) VALUES(1,0);");
//        db.execSQL("INSERT OR IGNORE INTO reminderTable(user_name, reminder_title, reminder_descripton, reminder_time, reminder_status) VALUES('madeby.sol', 'Go to the gym', 'Meetup with Ej and Dan', '9:30', 0);");
//        db.execSQL("INSERT OR IGNORE INTO reminderTable(user_name, reminder_title, reminder_descripton, reminder_time, reminder_status) VALUES('madeby.sol', 'UMAK Nexus App', 'Create the Shopping Page', '10:00', 0);");
//        db.execSQL("INSERT OR IGNORE INTO reminderTable(user_name, reminder_title, reminder_descripton, reminder_time, reminder_status) VALUES('madeby.sol', 'Bring Laptop', 'Overnight with the team', '8:15', 0);");


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
}