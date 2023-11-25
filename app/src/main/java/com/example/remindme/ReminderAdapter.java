package com.example.remindme;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, 0, reminders);
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

        ImageView expandIconBtn = convertView.findViewById(R.id.expandIcon);

        expandIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(ReminderAdapter.this.getContext(), "Reminder Id is: " + reminder.getReminderID(), Toast.LENGTH_SHORT).show();
                Intent AddReminderIntent = new Intent(ReminderAdapter.this.getContext(), EditReminder.class);
                AddReminderIntent.putExtra("reminderID", reminder.getReminderID());
                getContext().startActivity(AddReminderIntent);
            }
        });

        reminderTitle.setText(reminder.getReminderTitle());
        reminderDescription.setText(reminder.getReminderDescription());
        reminderTime.setText(reminder.getReminderTime());

        return convertView;
    }

}

