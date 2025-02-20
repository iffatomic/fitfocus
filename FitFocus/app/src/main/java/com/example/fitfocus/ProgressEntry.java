package com.example.fitfocus;

public class ProgressEntry {
    private long date;
    private float value;

    public ProgressEntry() { }

    public ProgressEntry(long date, float value) {
        this.date = date;
        this.value = value;
    }

    // Getters and setters
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
