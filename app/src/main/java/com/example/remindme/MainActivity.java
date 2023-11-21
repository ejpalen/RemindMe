package com.example.remindme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Locale;

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
//      setContentView(R.layout.activity_onboarding_screen);
//      setContentView(R.layout.activity_onboarding_user_input);
        setContentView(R.layout.activity_main);
//      Intent intent = new Intent(MainActivity.this,OnboardingScreen.class);

        createUserDB();
        /*saveUserReminder();
        //saveUserTime();
        deleteUserReminder();
        editUserReminder();
        statusUserReminder();*/

        //packageName = getApplicationContext().getPackageName().concat(".");
        //broadcastIntent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        //MyReceiver myReceiver = new MyReceiver();
        //registerReceiver(myReceiver,intentFilter );

        Cursor cursor = db.rawQuery("SELECT * FROM nameTable WHERE user_id = 1", null);
        if (cursor != null && cursor.getCount() > 0) {
            // If user_id = 1 exists in the database, start the Home activity
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        } else {
            // If user_id = 1 does not exist in the database, start the OnboardingScreen activity
            Intent intent = new Intent(MainActivity.this, OnboardingScreen.class);
            startActivity(intent);
        }

//        Onboarding_UserInput userInput = new Onboarding_UserInput(db);


    }

    /*public void broadcastIntent(){
        intent = new Intent();
        intent.setAction(packageName + "MY_CUSTOM_ACTION");
        intent.setClass(this, MyCustomReceiver.class);
        sendBroadcast(intent);
    }
    }*/

    public void createUserDB(){
        userName=findViewById(R.id.editTextText);
        reminderTitle=findViewById(R.id.reminderinput);
        reminderDescription=findViewById(R.id.descriptioninput);
        //reminderTime=(R.id.editTextTime);
        saveReminder=findViewById(R.id.submitbtn);

        builder = new AlertDialog.Builder(this);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS nameTable;");
        db.execSQL("DROP TABLE IF EXISTS reminderTable;");
        db.execSQL("CREATE TABLE IF NOT EXISTS nameTable (user_id INTEGER PRIMARY KEY, user_name TEXT );");
        db.execSQL("CREATE TABLE IF NOT EXISTS reminderTable (reminder_id INTEGER PRIMARY KEY AUTOINCREMENT, reminder_title TEXT, reminder_descripton TEXT, reminder_time TIME, reminder_status BOOLEAN not null default 0);");

        db.execSQL("INSERT INTO reminderTable(reminder_title, reminder_descripton, reminder_time, reminder_status) VALUES('Go to the gym', 'Meetup with Ej and Dan', '9:30', 0);");
        db.execSQL("INSERT INTO reminderTable(reminder_title, reminder_descripton, reminder_time, reminder_status) VALUES('UMAK Nexus App', 'Create the Shopping Page', '10:00', 0);");
        db.execSQL("INSERT INTO reminderTable(reminder_title, reminder_descripton, reminder_time, reminder_status) VALUES('Bring Laptop', 'Overnight with the team', '8:15', 0);");


    }

    public void displayMessage(String title, String message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}