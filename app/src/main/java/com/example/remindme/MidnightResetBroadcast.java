package com.example.remindme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;
import android.util.Log;

import androidx.core.app.NotificationCompat;

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

            String content = "MidnightResetBroadcast: Daily Reminders reset is complete";

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id", "Reminder Channel", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.alarm)
                    .setContentTitle("RemindMe")
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        } finally {
            // Close the database
            if (db != null) {
                db.close();
            }
        }
    }
}
