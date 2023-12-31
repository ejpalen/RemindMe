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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView login_name, login_password, loginCreateAccountbtn;
    Button loginBtn;
    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCreateAccountbtn = findViewById(R.id.loginCreateAccount_btn);
        loginBtn = findViewById(R.id.login_btn);
        login_name = findViewById(R.id.login_name_tv);
        login_password = findViewById(R.id.login_password_tv);

        loginCreateAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAccountIntent = new Intent(Login.this, CreateAccount.class);
                startActivity(createAccountIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = login_name.getText().toString();
                String enteredPassword = login_password.getText().toString();

                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter both name and password", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Check the entered username and password in your database
                    boolean isValidUser = checkUserCredentials(enteredUsername, enteredPassword);

                    if (isValidUser) {
                        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
                        db.execSQL("UPDATE nameTable SET user_status = 1 WHERE user_name = '" + enteredUsername + "'");
                        db.execSQL("UPDATE loggedInTable SET loggedIn_status = 1 WHERE id=1");

                        Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                        Intent HomeIntent = new Intent(Login.this, MainActivity.class);
                        startActivity(HomeIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(Login.this, "Invalid name or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkUserCredentials(String enteredUsername, String enteredPassword) {
        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        String query = "SELECT * FROM nameTable WHERE user_name = ? AND user_pass = ?";
        String[] selectionArgs = {enteredUsername, enteredPassword};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        boolean isValid = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return isValid;
    }
}