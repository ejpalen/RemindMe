package com.example.remindme;

public class Reminder {
    private String reminderTitle;
    private String reminderDescription;
    private String reminderTime;
    private String reminderID;
    private String reminderStatus;

    private boolean isActionMenuVisible = false;
    public Reminder(String reminderTitle, String reminderDescription, String reminderTime, String reminderID, String reminderStatus) {
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.reminderTime = reminderTime;
        this.reminderID = reminderID;
        this.reminderStatus = reminderStatus;
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

    public String getReminderStatus() {
        return reminderStatus;
    }

}
