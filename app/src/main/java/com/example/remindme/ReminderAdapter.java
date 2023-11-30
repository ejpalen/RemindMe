package com.example.remindme;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    ImageView borderLine;  // Add ImageView reference for border_line
    Button btnComplete, btnEdit, btnDelete;
    SQLiteDatabase db;
    String reminderStatus;


    public interface OnCompleteListener {
        void onCompleteClicked(String reminderId);
    }

    private OnCompleteListener onCompleteListener;

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
        TextView reminderStatusIndicator = convertView.findViewById(R.id.status_indicator);
        borderLine = convertView.findViewById(R.id.border_line);  // Initialize ImageView reference
        btnComplete = convertView.findViewById(R.id.btn_complete);  // Initialize Button references
        btnEdit = convertView.findViewById(R.id.btn_edit);
        btnDelete = convertView.findViewById(R.id.btn_delete);
        LinearLayout btnsLinearLayout = convertView.findViewById(R.id.buttons_LinearLayout);
        ImageView expandIconBtn = convertView.findViewById(R.id.expandIcon);

        Intent AddReminderIntent = new Intent(ReminderAdapter.this.getContext(), EditReminder.class);
        AddReminderIntent.putExtra("reminderID", reminder.getReminderID());

        Cursor cursor = db.rawQuery("SELECT reminder_status FROM reminderTable WHERE reminder_id = '" + reminder.getReminderID() + "'", null);
        if (cursor != null && cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex("reminder_status");
            reminderStatus = cursor.getString(userNameIndex);
            cursor.close(); // Close the cursor when done
        }

        if ("0".equals(reminderStatus)) {
            expandIconBtn.setVisibility(View.VISIBLE);
            reminderStatusIndicator.setTextColor(Color.parseColor("#FF9D3C"));
            reminderStatusIndicator.setText("Ongoing");

        } else {
            expandIconBtn.setVisibility(View.GONE);
            reminderStatusIndicator.setTextColor(Color.parseColor("#39CE79"));
            reminderStatusIndicator.setText("Completed");
        }


        Boolean btnClicked = reminder.isActionMenuVisible();


        // User does not exist, proceed with inserting into the database and starting the login activity

        // Set the initial visibility state based on the reminder's property
        if (reminder.isActionMenuVisible()) {
            borderLine.setVisibility(View.VISIBLE);
            btnsLinearLayout.setVisibility(View.VISIBLE);
        } else {
            borderLine.setVisibility(View.GONE);
            btnsLinearLayout.setVisibility(View.GONE);
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

                    if (onCompleteListener != null) {
                        onCompleteListener.onCompleteClicked(reminder.getReminderID());
                    }

                    reminder.toggleActionMenuVisibility();

                    remove(reminder);

                    Toast.makeText(getContext(), "Reminder is marked completed", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(AddReminderIntent);
            }

        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("DELETE FROM reminderTable WHERE reminder_id = '" + reminder.getReminderID() + "'");
                remove(reminder);
                Toast.makeText(getContext(), "Reminder is deleted", Toast.LENGTH_SHORT).show();
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

