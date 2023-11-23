package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    TextView account_name, account_password, createAccountLoginbtn;
    Button creataccount_btn;
    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        creataccount_btn = findViewById(R.id.creataccount_btn);
        createAccountLoginbtn = findViewById(R.id.createAccountLogin_btn);
        account_name = findViewById(R.id.createaccount_name_tv);
        account_password = findViewById(R.id.createaccount_password_tv);

        createAccountLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAccountIntent = new Intent(CreateAccount.this, Login.class);
                startActivity(createAccountIntent);
            }
        });

        creataccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = account_name.getText().toString();
                String userPassword = account_password.getText().toString();

                // Check if account name and password are not empty
                if (userName.isEmpty() || userPassword.isEmpty()) {
                    // Display an error message or prompt the user to fill in both fields
                    // For example, show a Toast message
                    Toast.makeText(CreateAccount.this, "Please enter both account name and password", Toast.LENGTH_SHORT).show();
                } else {
                    // If both fields are filled, check if the user exists in the database
                    if (isUserExists(userName)) {
                        // User already exists, show an error message
                        Toast.makeText(CreateAccount.this, "Account already exists. Please choose a different username.", Toast.LENGTH_SHORT).show();
                    } else {
                        // User does not exist, proceed with inserting into the database and starting the login activity
                        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
                        db.execSQL("INSERT INTO nameTable(user_login, user_password) VALUES('" + userName + "', '" + userPassword + "')");
                        db.execSQL("UPDATE nameTable SET user_status = 0 WHERE user_login = '"+ userName +"'");

                        Intent loginIntent = new Intent(CreateAccount.this, Login.class);
                        startActivity(loginIntent);
                    }
                }
            }
        });
    }

    private boolean isUserExists(String userName) {
        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        String query = "SELECT * FROM nameTable WHERE user_login = ?";
        String[] selectionArgs = {userName};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean userExists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return userExists;
    }
}