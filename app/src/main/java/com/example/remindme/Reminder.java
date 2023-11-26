package com.example.remindme;

public class Reminder {
    private String reminderTitle;
    private String reminderDescription;
    private String reminderTime;
    private String reminderID;

    private boolean isActionMenuVisible = false;
    public Reminder(String reminderTitle, String reminderDescription, String reminderTime, String reminderID) {
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.reminderTime = reminderTime;
        this.reminderID = reminderID;
    }

    public void toggleActionMenuVisibility() {
        isActionMenuVisible = !isActionMenuVisible;
    }

    public boolean isActionMenuVisible() {
        return isActionMenuVisible;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public String getReminderID() {
        return reminderID;
    }

    // Getters and setters
}
