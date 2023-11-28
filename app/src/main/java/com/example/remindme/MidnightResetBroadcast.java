package com.example.remindme;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;
import android.util.Log;

public class MidnightResetBroadcast extends BroadcastReceiver {

    SQLiteDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MidnightResetBroadcast", "onReceive called");

        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            // Check if the current time is midnight (00:00)
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            if (hour == 0 && minute == 0) {
                // Add your code to perform actions at midnight
                updateReminderStatus(context);
            }
        }
    }

    public void updateReminderStatus(Context context) {
        // Open or create the database
        db = context.openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        try {
            // Update all rows in the reminderTable where reminder_status is 0
            db.execSQL("UPDATE reminderTable SET reminder_status = 0");
        } finally {
            // Close the database
            if (db != null) {
                db.close();
                Toast.makeText(context, "oks na bossing", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
