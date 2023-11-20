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

    TextView greetingText, userNameText, titleText, descriptionText;
    private ImageView timeOfDayImage;
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

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);

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
    // ReminderItem class with due date
    private static class ReminderItem {
        private String titleText;
        private String descriptionText;
        private String dueDateText;

        ReminderItem(String titleText, String descriptionText, String dueDateText) {
            this.titleText = titleText;
            this.descriptionText = descriptionText;
            this.dueDateText = dueDateText;
        }

        // Getter methods for title, description, and due date
    }

    // Update getDummyData to provide due dates
    private List<ReminderItem> getDummyData() {
        List<ReminderItem> reminderList = new ArrayList<>();
        reminderList.add(new ReminderItem("Meeting", "Team meeting at 10 AM", "2023-11-20"));
        reminderList.add(new ReminderItem("Call", "Call mom at 3 PM", "2023-11-21"));
        // Add more dummy data as needed
        return reminderList;
    }

    // Update onBindViewHolder in ReminderAdapter to handle actions
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        ReminderItem item = reminderList.get(position);
        holder.titleText.setText(item.titleText);
        holder.descriptionText.setText(item.descriptionText);
        holder.dueDateText.setText("Due Date: " + item.dueDateText);

        // Set click listener for the expand icon
        holder.expandIcon.setOnClickListener(v -> {
            // Toggle visibility of action menu

        });
    }

    // Inner class for ViewHolder
    class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;
        TextView dueDateText;
        ImageView expandIcon;

        ReminderViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            dueDateText = itemView.findViewById(R.id.dueDateText);
            expandIcon = itemView.findViewById(R.id.expandIcon);
        }
    }
}
