package com.example.remindme;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your database table(s) here
        db.execSQL("CREATE TABLE IF NOT EXISTS reminderTable ("
                + "reminder_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "reminder_title TEXT,"
                + "reminder_description TEXT,"
                + "reminder_time TEXT,"
                + "reminder_status INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
        // This method is called when the DATABASE_VERSION is increased
    }
}
