package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class EditReminder extends AppCompatActivity {


    Button backButton;

    FrameLayout backButtonFrame;
    EditText descriptionInput;
    EditText reminderInput;
    EditText editTextTime;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        // Reference XML elements
        backButtonFrame = findViewById(R.id.backButtonFrame);
        descriptionInput = findViewById(R.id.descriptioninput);
        reminderInput = findViewById(R.id.reminderinput);
        editTextTime = findViewById(R.id.editTextTime);
        submitButton = findViewById(R.id.submitbtn);

        // Set click listener for the back button
        backButtonFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditReminder.this,
                        MainActivity.class);

                startActivity(intent);

                // Handle back button click
                finish(); // Close the current activity
            }
        });

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle submit button click
                // Add your logic here
            }
        });
    }
}