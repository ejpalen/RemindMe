package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Onboarding_UserInput extends AppCompatActivity {

    Button confirmUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_user_input);

        confirmUser = findViewById(R.id.confirm_user_btn);

        confirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Onboarding_UserInput.this,
                        MainActivity.class);

                startActivity(intent);
            }
        });


    }
}