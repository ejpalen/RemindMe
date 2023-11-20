package com.example.remindme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText userName, reminderTitle, reminderDescription, reminderTime;
    Button saveName, saveReminder, completeReminder, editReminder, deleteReminder;
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
        deleteUserReminder();
        editUserReminder();
    }

    public void createUserDB(){
        userName=findViewById(R.id.editTextText);
        reminderTitle=findViewById(R.id.reminderinput);
        reminderDescription=findViewById(R.id.descriptioninput);
        reminderTime=findViewById(R.id.editTextTime);

        builder = new AlertDialog.Builder(this);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS nameTable;");
        db.execSQL("DROP TABLE IF EXISTS reminderTable;");
        db.execSQL("CREATE TABLE IF NOT EXISTS nameTable (user_name TEXT PRIMARY KEY);");
        db.execSQL("CREATE TABLE IF NOT EXISTS reminderTable (reminder_title TEXT PRIMARY KEY AUTOINCREMENT, reminder_descripton TEXT, reminder_time TIME);");
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
                displayMessage("Login Successful.","Welcome to RemindMe!");
                clearEntries();
            }
        });
    }

    public void saveUserReminder(){
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reminderTitle.getText().toString().isEmpty() && reminderDescription.getText().toString().isEmpty() &&  reminderTime.getText().toString().isEmpty()){
                    displayMessage("Error!","Please fill out all information");
                    return;
                }
                db.execSQL("INSERT INTO reminderTable(reminder_title) " + "VALUES('"+reminderTitle.getText().toString()+"')");
                db.execSQL("INSERT INTO reminderTable(reminder_description) " + "VALUES('"+reminderDescription.getText().toString()+"')");
                db.execSQL("INSERT INTO reminderTable(reminder_time) " + "VALUES('"+reminderTime.getDrawingTime()+"')");
                displayMessage("Information!.","Reminder has been successfully saved");
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
    private void clearEntries() {
    }

}