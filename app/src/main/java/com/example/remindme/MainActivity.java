package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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
    }
}