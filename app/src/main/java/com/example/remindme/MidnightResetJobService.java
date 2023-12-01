package com.example.remindme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

public class MidnightResetJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        // Perform the task you want to do at midnight
        updateReminderStatus(getApplicationContext());

        // Reschedule the job for the next midnight
        scheduleMidnightResetJob(getApplicationContext());

        // Return true because the job is still running
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Return false to drop the job if the conditions are no longer met
        return false;
    }

    private void updateReminderStatus(Context context) {
        // Open the database
        SQLiteDatabase db = context.openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);

        try {
            // Update all rows in the reminderTable where reminder_status is 0
            db.execSQL("UPDATE reminderTable SET reminder_status = 0 WHERE reminder_status = 1");

            String content = "MidnightResetJobService: Daily Reminders reset is complete";

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

    public static void scheduleMidnightResetJob(Context context) {
        JobIntentService.enqueueWork(context, MidnightResetJobService.class, 1, new Intent());
    }
}