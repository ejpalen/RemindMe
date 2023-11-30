package com.example.remindme;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.JobIntentService;

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
        } finally {
            // Close the database
            if (db != null) {
                db.close();
            }
        }

        // Display a toast message for testing purposes
        Toast.makeText(context, "Midnight reset complete", Toast.LENGTH_SHORT).show();
    }

    public static void scheduleMidnightResetJob(Context context) {
        JobIntentService.enqueueWork(context, MidnightResetJobService.class, 1, new Intent());
    }
}