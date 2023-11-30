package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class OnboardingScreen extends AppCompatActivity {

    Button getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        getStarted = findViewById(R.id.getStarted_btn);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent obsintent = new Intent(OnboardingScreen.this, Login.class);
                startActivity(obsintent);
                finish();

            }
        });



    }
}