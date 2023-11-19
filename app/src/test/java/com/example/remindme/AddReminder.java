package com.example.remindme;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddReminder extends AppCompatActivity {

    Button backButton;
    EditText descriptionInput;
    EditText reminderInput;
    EditText editTextTime;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference XML elements
        backButton = findViewById(R.id.button);
        descriptionInput = findViewById(R.id.descriptioninput);
        reminderInput = findViewById(R.id.reminderinput);
        editTextTime = findViewById(R.id.editTextTime);
        submitButton = findViewById(R.id.submitbtn);

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddReminder.this,
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
