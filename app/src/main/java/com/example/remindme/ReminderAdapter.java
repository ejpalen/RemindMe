package com.example.remindme;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    ImageView borderLine;  // Add ImageView reference for border_line
    Button btnComplete, btnEdit, btnDelete;
    SQLiteDatabase db;


    public ReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, 0, reminders);

        // Initialize the SQLiteDatabase object
        db = context.openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reminder reminder = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_reminderlist, parent, false);
        }

        TextView reminderTitle = convertView.findViewById(R.id.reminderTitle);
        TextView reminderDescription = convertView.findViewById(R.id.reminderDescription);
        TextView reminderTime = convertView.findViewById(R.id.reminderTime);
        borderLine = convertView.findViewById(R.id.border_line);  // Initialize ImageView reference
        btnComplete = convertView.findViewById(R.id.btn_complete);  // Initialize Button references
        btnEdit = convertView.findViewById(R.id.btn_edit);
        btnDelete = convertView.findViewById(R.id.btn_delete);
        ImageView expandIconBtn = convertView.findViewById(R.id.expandIcon);




        // User does not exist, proceed with inserting into the database and starting the login activity

        // Set the initial visibility state based on the reminder's property
        if (reminder.isActionMenuVisible()) {
            borderLine.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            borderLine.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ReminderAdapter", "btnComplete clicked for reminder ID: " + reminder.getReminderID());

                // Check if the database is not null
                if (db != null) {
                    // Update the reminder_status to 1 in the database
                    updateReminderStatus(reminder.getReminderID());

                    // Notify the adapter that the data set has changed
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "arghh!!", Toast.LENGTH_SHORT).show();
                } else {
                    // Log an error or show a Toast message to indicate the issue
                    Toast.makeText(getContext(), "Database is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddReminderIntent = new Intent(ReminderAdapter.this.getContext(), EditReminder.class);
                AddReminderIntent.putExtra("reminderID", reminder.getReminderID());
                getContext().startActivity(AddReminderIntent);
            }
        });

        expandIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the visibility state
                reminder.toggleActionMenuVisibility();

                // Notify the adapter that the data set has changed
                notifyDataSetChanged();
            }
        });

        reminderTitle.setText(reminder.getReminderTitle());
        reminderDescription.setText(reminder.getReminderDescription());
        reminderTime.setText(reminder.getReminderTime());

        return convertView;
    }

    private void updateReminderStatus(String reminderId) {
        try {

            db.execSQL("UPDATE reminderTable SET reminder_status = 1 WHERE reminder_id = '"+ reminderId +"'");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error updating reminder status", Toast.LENGTH_SHORT).show();
        }
    }



}

