package com.example.fitfocus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.content.SharedPreferences;
import android.view.View;
import android.graphics.Color;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProgressTrackingActivity extends AppCompatActivity {

    private TextView caloriesBurnedTextView;
    private TextView daysCommittedTextView;
    private TextView totalMinutesTextView;
    private Button resetButton;
    private FirebaseAuth auth;
    private LinearLayout dateContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracking);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Progress Tracking");

        caloriesBurnedTextView = findViewById(R.id.calories_burned);
        daysCommittedTextView = findViewById(R.id.days_committed);
        totalMinutesTextView = findViewById(R.id.total_minutes);
        resetButton = findViewById(R.id.reset_button);
        dateContainer = findViewById(R.id.date_container);
        auth = FirebaseAuth.getInstance();

        loadProgress();
        updateDateViews();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetConfirmationDialog();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadProgress() {
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_" + uid, MODE_PRIVATE);
        int caloriesBurned = sharedPreferences.getInt("calories_burned", 0);
        int daysCommitted = sharedPreferences.getInt("days_committed", 0);
        int totalMinutes = sharedPreferences.getInt("total_minutes", 0);

        // Display the progress data
        caloriesBurnedTextView.setText("Calories Burned: " + caloriesBurned + " kcal");
        daysCommittedTextView.setText("Days Committed: " + daysCommitted);
        totalMinutesTextView.setText("Minutes: " + totalMinutes);
    }

    private void resetProgress() {
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_" + uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("calories_burned", 0);
        editor.putInt("days_committed", 0);
        editor.putInt("total_minutes", 0);
        editor.apply();
        loadProgress(); // Reload progress to update UI
    }

    private void showResetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Progress");
        builder.setMessage("Are you sure you want to reset your progress?");

        builder.setPositiveButton("Yes", null);
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                positiveButton.setTextColor(getResources().getColor(android.R.color.black));
                negativeButton.setTextColor(getResources().getColor(android.R.color.black));

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetProgress();
                        dialog.dismiss();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void updateDateViews() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String todayDate = dateFormat.format(calendar.getTime());

        for (int i = -3; i <= 3; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, i);
            String dateText = dateFormat.format(calendar.getTime());
            String dayText = dayFormat.format(calendar.getTime());

            View dateView = getLayoutInflater().inflate(R.layout.date_item, dateContainer, false);
            TextView dayTextView = dateView.findViewById(R.id.day_text);
            TextView dateTextView = dateView.findViewById(R.id.date_text);

            dayTextView.setText(dayText);
            dateTextView.setText(dateText);

            if (dateText.equals(todayDate)) {
                dayTextView.setTextColor(Color.parseColor("#FFFF00")); // Purple color
                dateTextView.setTextColor(Color.parseColor("#FFFF00"));
            }

            dateContainer.addView(dateView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProgressTrackingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
