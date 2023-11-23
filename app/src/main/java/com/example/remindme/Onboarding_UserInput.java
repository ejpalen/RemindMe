package com.example.remindme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Onboarding_UserInput extends AppCompatActivity {

    Button saveUser;
    EditText userName;
    SQLiteDatabase db;
    AlertDialog.Builder builder;

//    public Onboarding_UserInput(SQLiteDatabase database) {
//        this.db = database;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_user_input);

        userName = findViewById(R.id.createaccount_name_tv);
        saveUser = findViewById(R.id.login_btn);

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(Onboarding_UserInput.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else {

                    db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
                    db.execSQL("INSERT INTO nameTable(user_name) VALUES('" + name + "')");

                    // Start the Homepage activity
                    Intent homepageintent = new Intent(Onboarding_UserInput.this, Home.class);
                    startActivity(homepageintent);
                }
            }

            private void displayMessage(String title, String message) {
                builder.setCancelable(true);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.show();
            }
        });




    }
}