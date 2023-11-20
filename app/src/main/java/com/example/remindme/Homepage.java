package com.example.remindme;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    Cursor cursor;
    SQLiteDatabase db;
    private ImageView timeOfDayImage;
    private List<ReminderItem> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        // Assuming you have a TextView with the id "greetingText" in your layout
        greetingText = findViewById(R.id.greetingText);

        // Assuming you have a TextView with the id "userNameText" in your layout
        userNameText = findViewById(R.id.userNameText);

        // Assuming you have an ImageView with the id "timeOfDayImage" in your layout
        timeOfDayImage = findViewById(R.id.timeOfDayImage);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Get the user name from the input screen (assuming it's passed through Intent)
        //String confirmUser = getIntent().getStringExtra("confirmUser");
        //String confirmUser =

        Cursor cursor = db.rawQuery("SELECT user_name FROM nameTable WHERE user_id = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex("user_name");
            String userName = cursor.getString(userNameIndex);
            cursor.close(); // Close the cursor when done
            if (userName != null) {
                userNameText.setText("Hello, " + userName + "!");
            }
        } else {
            // Handle the case where the cursor is null or empty
            // You might want to set a default value for the username or show an error message
        }


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
        private String reminderTitle;
        private String reminderDescription;
        private String reminderTime;

        ReminderItem(String reminderTitle, String reminderDescription, String reminderTime) {
            this.reminderTitle = reminderTitle;
            this.reminderDescription = reminderDescription;
            this.reminderTime = reminderTime;
        }

        // Getter methods for title, description, and due date
    }

    // Update onBindViewHolder in ReminderAdapter to handle actions
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        ReminderItem item = reminderList.get(position);
        holder.reminderTitle.setText(item.reminderTitle);
        holder.reminderDescription.setText(item.reminderDescription);
        holder.reminderTime.setText(item.reminderTime);

        // Set click listener for the expand icon
        holder.expandIcon.setOnClickListener(v -> {
            // Toggle visibility of action menu

        });
    }

    // Inner class for ViewHolder
    class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView reminderTitle;
        TextView reminderDescription;
        TextView reminderTime;
        ImageView expandIcon;

        ReminderViewHolder(View itemView) {
            super(itemView);
            reminderTitle = itemView.findViewById(R.id.reminderTitle);
            reminderDescription = itemView.findViewById(R.id.reminderDescription);
            reminderTime = itemView.findViewById(R.id.reminderTime);
            expandIcon = itemView.findViewById(R.id.expandIcon);
        }
    }
}
