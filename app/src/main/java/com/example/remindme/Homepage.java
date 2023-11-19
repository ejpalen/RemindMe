package com.example.remindme;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;

public class Homepage extends AppCompatActivity {

    private TextView greetingText, userNameText;
    private ImageView timeOfDayImage;
    private RecyclerView recyclerView;
    private List<ReminderItem> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Assuming you have a TextView with the id "greetingText" in your layout
        greetingText = findViewById(R.id.greetingText);

        // Assuming you have a TextView with the id "userNameText" in your layout
        userNameText = findViewById(R.id.userNameText);

        // Assuming you have an ImageView with the id "timeOfDayImage" in your layout
        timeOfDayImage = findViewById(R.id.timeOfDayImage);

        recyclerView = findViewById(R.id.reminderRecyclerView);

        // Initialize RecyclerView
//        reminderList = getDummyData(); // Replace with your data source
//        ReminderAdapter adapter = new ReminderAdapter(reminderList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Get the user name from the input screen (assuming it's passed through Intent)
        String confirmUser = getIntent().getStringExtra("confirmUser");

        // Set the user name in the TextView
        userNameText.setText("Hello, " + confirmUser + "!");

        // Set the greeting and image based on the time of day
        if (hourOfDay >= 6 && hourOfDay < 12) {
            greetingText.setText("Good Morning,");
            timeOfDayImage.setImageResource(R.drawable.morning);
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greetingText.setText("Good Afternoon,");
            timeOfDayImage.setImageResource(R.drawable.afternoon);
        } else {
            greetingText.setText("Good Evening,");
            timeOfDayImage.setImageResource(R.drawable.evening);
        }
    }

    // Dummy data for testing
    private List<ReminderItem> getDummyData() {
        List<ReminderItem> dummyList = new ArrayList<>();
        reminderList.add(new ReminderItem("Meeting", "Team meeting at 10 AM"));
        reminderList.add(new ReminderItem("Call", "Call mom at 3 PM"));
        // Add more dummy data as needed
        return reminderList;
    }

    // Inner class for ReminderItem (Replace with your actual ReminderItem class)
    private static class ReminderItem {
        private String title;
        private String description;

        ReminderItem(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

}
