package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Add_Name extends AppCompatActivity {

    SQLiteDatabase db;
    TextView addName, loginTextView;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        addName = findViewById(R.id.user_name_tv);
        save = findViewById(R.id.save_btn);
        View otherView = getLayoutInflater().inflate(R.layout.activity_login, null);
        loginTextView = otherView.findViewById(R.id.login_name_tv);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String nickname = addName.getText().toString();
                String loginValue = loginTextView.getText().toString();

                if (nickname.isEmpty()) {
                    Toast.makeText(Add_Name.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }
                else {
                    db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
                    db.execSQL("UPDATE nameTable SET add_name = '" + nickname + "' WHERE user_status = 1");
                    db.execSQL("UPDATE loggedInTable SET loggedIn_status = 1 WHERE id=1");

                    Toast.makeText(Add_Name.this, "Welcome'" + nickname + "'", Toast.LENGTH_SHORT).show();

                    Intent HomeIntent = new Intent(Add_Name.this, MainActivity.class);
                    startActivity(HomeIntent);
                    finish();
                }
            }
        });
    }
}