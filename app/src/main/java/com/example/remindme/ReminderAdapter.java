package com.example.remindme;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;

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

        reminderTitle.setText(reminder.getreminderTitle());
        reminderDescription.setText(reminder.getreminderDescription());
        reminderTime.setText(reminder.getreminderTime());

        return convertView;
    }
}

