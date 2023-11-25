package com.example.remindme;

public class Reminder {
    private String reminderTitle;
    private String reminderDescription;
    private String reminderTime;

    public Reminder(String reminderTitle, String reminderDescription, String reminderTime) {
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.reminderTime = reminderTime;
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

    // Getters and setters
}
