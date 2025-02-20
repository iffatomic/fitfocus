package com.example.fitfocus;

import java.util.List;

public class UserProgress {
    private int totalDaysCommitted;
    private float averageWorkoutDuration;
    private float averageCaloriesBurned;
    private List<ProgressEntry> progressData;

    public UserProgress() { }

    public UserProgress(int totalDaysCommitted, float averageWorkoutDuration, float averageCaloriesBurned, List<ProgressEntry> progressData) {
        this.totalDaysCommitted = totalDaysCommitted;
        this.averageWorkoutDuration = averageWorkoutDuration;
        this.averageCaloriesBurned = averageCaloriesBurned;
        this.progressData = progressData;
    }

    // Getters and setters
    public int getTotalDaysCommitted() {
        return totalDaysCommitted;
    }

    public void setTotalDaysCommitted(int totalDaysCommitted) {
        this.totalDaysCommitted = totalDaysCommitted;
    }

    public float getAverageWorkoutDuration() {
        return averageWorkoutDuration;
    }

    public void setAverageWorkoutDuration(float averageWorkoutDuration) {
        this.averageWorkoutDuration = averageWorkoutDuration;
    }

    public float getAverageCaloriesBurned() {
        return averageCaloriesBurned;
    }

    public void setAverageCaloriesBurned(float averageCaloriesBurned) {
        this.averageCaloriesBurned = averageCaloriesBurned;
    }

    public List<ProgressEntry> getProgressData() {
        return progressData;
    }

    public void setProgressData(List<ProgressEntry> progressData) {
        this.progressData = progressData;
    }
}
