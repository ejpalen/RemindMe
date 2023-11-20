package com.example.remindme;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.TimePicker;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText userName, reminderTitle, reminderDescription;
    Button saveName, saveReminder, statusReminder, editReminder, deleteReminder, reminderTime;
    int hour, minute;
    SQLiteDatabase db;
    Cursor cursor;
    AlertDialog.Builder builder;
    StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_onboarding_screen);
//        setContentView(R.layout.activity_onboarding_user_input);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this,
//                OnboardingScreen.class);

        Intent intent = new Intent(MainActivity.this,
                Add_Reminder.class);

        startActivity(intent);
        createUserDB();
        saveUserReminder();
        saveUserName();
        //saveUserTime();
        deleteUserReminder();
        editUserReminder();
        statusUserReminder();
    }

    public void createUserDB(){
        userName=findViewById(R.id.editTextText);
        reminderTitle=findViewById(R.id.reminderinput);
        reminderDescription=findViewById(R.id.descriptioninput);
        //reminderTime=(R.id.editTextTime);

        builder = new AlertDialog.Builder(this);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS nameTable;");
        db.execSQL("DROP TABLE IF EXISTS reminderTable;");
        db.execSQL("CREATE TABLE IF NOT EXISTS nameTable (user_name TEXT PRIMARY KEY);");
        db.execSQL("CREATE TABLE IF NOT EXISTS reminderTable (reminder_title TEXT PRIMARY KEY AUTOINCREMENT, reminder_descripton TEXT, reminder_time TIME, reminder_status BOOLEAN not null default 0);");
        userName.setEnabled(false);
    }

    public void displayMessage(String title, String message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void saveUserName(){
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().isEmpty()){
                    displayMessage("Error!","Please Enter Name");
                    return;
                }
                db.execSQL("INSERT INTO nameTable(userName) " + "VALUES('"+userName.getText().toString()+"')");
                //DUMMY DATA
                db.execSQL("INSERT INTO nameTable(userName) " + "VALUES('Aaron')");
                displayMessage("Login Successful.","Welcome to RemindMe!");
                clearEntries();
            }
        });
    }

    public void saveUserReminder(){
        saveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reminderTitle.getText().toString().isEmpty() && reminderDescription.getText().toString().isEmpty() /*&&  reminderTime.getValue.ToString().isEmpty()*/){
                    displayMessage("Error!","Please fill out all information");
                    return;
                }
                db.execSQL("INSERT INTO reminderTable(reminder_title)" + "VALUES('"+reminderTitle.getText().toString()+"')");
                //DUMMY DATA
                db.execSQL("INSERT INTO reminderTable(reminder_title)" + "VALUES('Backstab')");

                db.execSQL("INSERT INTO reminderTable(reminder_description)" + "VALUES('"+reminderDescription.getText().toString()+"')");
                //DUMMY DATA
                db.execSQL("INSERT INTO reminderTable(reminder_description)" + "VALUES('Backstab si EJ sa harap nya.')");

                db.execSQL("INSERT INTO reminderTable(reminder_time) " + "VALUES('"+reminderTime.getDrawingTime()+"')");
                //DUMMY DATA
                db.execSQL("INSERT INTO reminderTable(reminder_time)" + "VALUES('9:30')");

                displayMessage("Information!.","Reminder has been successfully saved");
                clearEntries();
            }
        });
    }

    /*public void saveUserTime(){
        TimePicker tp =(TimePicker) findViewById(R.id.editTextTime);
        String time = tp.getCurrentHour() + ":" + tp.getCurrentMinute();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour =  9;
                minute = 30;
                reminderTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

                if(reminderTitle.getText().toString().isEmpty() && reminderDescription.getText().toString().isEmpty() &&  reminderTime.gettime()){
                    displayMessage("Error!","Please fill out all information");
                    return;
                }

                db.execSQL("INSERT INTO reminderTable(reminder_time) " + "VALUES('"+reminderTime.getDrawingTime()+"')");
                //DUMMY DATA
                db.execSQL("INSERT INTO reminderTable(reminder_time)" + "VALUES('Aaron')");
                displayMessage("Information!.","Reminder has been successfully saved");
                clearEntries();
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }*/

    public void statusUserReminder(){
        statusReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cursor.moveToFirst()){
                    cursor = db.rawQuery("SELECT * FROM reminderTable WHERE reminder_title = reminder_title",null);
                    db.execSQL("UPDATE reminderTable SET reminder_status = TRUE WHERE reminder_status = FALSE");
                    displayMessage("Information!","Reminder has been successfully modified!");
                }
                else {
                    displayMessage("Error!", "Reminder not updated");
                }
            }
        });
    }

    public void editUserReminder(){
        editReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cursor.moveToFirst()){
                    cursor = db.rawQuery("SELECT * FROM reminderTable WHERE reminder_title = reminder_title",null);
                    db.execSQL("UPDATE reminderTable SET reminder_title='"+reminderTitle.getText().toString()+"'," +
                            "reminder_description='"+reminderDescription.getText().toString()+ "'," +
                            "reminder_time='"+reminderTime.getDrawingTime()+
                            "'WHERE reminder_title = reminder_title'");
                    displayMessage("Information!","Reminder has been successfully modified!");
                }else{
                    displayMessage("Error!","Reminder not updated");
                }
                clearEntries();
            }
        });
    }

    public void deleteUserReminder(){
        deleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = db.rawQuery("SELECT * FROM reminderTable WHERE reminder_title = reminder_title",null);
                if(cursor.moveToFirst()){
                    db.execSQL("DELETE FROM reminderTable WHERE reminder_title = reminder_title");
                    displayMessage("Information!","Reminder has been successfully deleted!");
                }else{
                    displayMessage("Error!","Reminder not deleted");
                }
                clearEntries();
            }
        });
    }

    private void clearEntries() {
    }

}