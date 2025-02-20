package com.example.fitfocus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity2 extends AppCompatActivity {

    int[] newArray;
    CheckBox[] checkboxes;
    Button finishButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Ensure the custom theme is applied
        setContentView(R.layout.activity_second2); // Ensure this is the correct layout for Muscle Gain

        auth = FirebaseAuth.getInstance();

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newArray = new int[]{
                R.id.mountainclimber, R.id.basiccrunches, R.id.benchdips, R.id.bicyclecrunches,
                R.id.legraise, R.id.heeltouches, R.id.legupcrunches, R.id.situps,
                R.id.alternativevups, R.id.plankrotation, // Ensure the IDs are correct
        };

        checkboxes = new CheckBox[]{
                findViewById(R.id.checkbox_mountainclimber), findViewById(R.id.checkbox_basiccrunches),
                findViewById(R.id.checkbox_benchdips), findViewById(R.id.checkbox_bicyclecrunches),
                findViewById(R.id.checkbox_legraise), findViewById(R.id.checkbox_heeltouches),
                findViewById(R.id.checkbox_legupcrunches), findViewById(R.id.checkbox_situps),
                findViewById(R.id.checkbox_alternativevups), findViewById(R.id.checkbox_plankrotation), // Ensure the IDs are correct
        };

        // Set checkboxes non-interactive
        for (CheckBox checkbox : checkboxes) {
            checkbox.setClickable(false);
        }

        finishButton = findViewById(R.id.finish_button);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ask for confirmation before finishing the activity
                new MaterialAlertDialogBuilder(SecondActivity2.this)
                        .setTitle("Finish Activity")
                        .setMessage("Would you like to finish your activity for today?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                submitActivity();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        // Disable the finish button initially
        finishButton.setEnabled(false);

        loadProgress();
        checkCompletionStatus();
    }

    private void checkCompletionStatus() {
        boolean allChecked = true;
        for (CheckBox checkbox : checkboxes) {
            if (!checkbox.isChecked()) {
                allChecked = false;
                break;
            }
        }
        finishButton.setEnabled(allChecked);
    }

    public void markWorkoutCompleted(int workoutIndex) {
        if (workoutIndex >= 0 && workoutIndex < checkboxes.length) {
            checkboxes[workoutIndex].setChecked(true);
            checkCompletionStatus();
            updateProgress(workoutIndex);
        }
    }

    private void submitActivity() {
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_" + uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Increment days committed if all checkboxes are checked
        boolean allChecked = true;
        for (CheckBox checkbox : checkboxes) {
            if (!checkbox.isChecked()) {
                allChecked = false;
                break;
            }
        }
        if (allChecked) {
            int daysCommitted = sharedPreferences.getInt("days_committed", 0) + 1;
            editor.putInt("days_committed", daysCommitted);
        }

        // Increment minutes spent (assuming 10 minutes per session)
        int totalMinutes = sharedPreferences.getInt("total_minutes", 0) + 10;
        editor.putInt("total_minutes", totalMinutes);

        // Increment calories burned (assuming 105 kcal per workout)
        int caloriesBurned = sharedPreferences.getInt("calories_burned", 0) + (27 * checkboxes.length);
        editor.putInt("calories_burned", caloriesBurned);

        editor.apply();

        // Reset the checkboxes
        resetCheckboxes();

        // Redirect to ProgressTrackingActivity
        Intent intent = new Intent(SecondActivity2.this, ProgressTrackingActivity.class);
        startActivity(intent);
        finish(); // Optionally close SecondActivity if you don't want the user to come back to it immediately
    }

    private void resetCheckboxes() {
        for (CheckBox checkbox : checkboxes) {
            checkbox.setChecked(false);
        }
        finishButton.setEnabled(false);
        clearProgress();
    }

    private void loadProgress() {
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_MuscleGain_" + uid, MODE_PRIVATE);
        for (int i = 0; i < checkboxes.length; i++) {
            boolean isChecked = sharedPreferences.getBoolean("checkbox_" + i, false);
            checkboxes[i].setChecked(isChecked);
        }
        checkCompletionStatus();
    }

    private void updateProgress(int workoutIndex) {
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_MuscleGain_" + uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the checkbox state
        editor.putBoolean("checkbox_" + workoutIndex, true);

        editor.apply();
    }

    private void clearProgress() {
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_MuscleGain_" + uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < checkboxes.length; i++) {
            editor.putBoolean("checkbox_" + i, false);
        }
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_privacy) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://iotexpert1.blogspot.com/2020/10/weight-loss-privacy-ploicy-page.html"));
            startActivity(intent);
            return true;
        }

        if (id == R.id.id_term) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://iotexpert1.blogspot.com/2020/10/weight-loss-terms-and-conditions-page.html"));
            startActivity(intent);
            return true;
        }

        if (id == R.id.id_restart) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Confirm Reset")
                    .setMessage("Are you sure about resetting your progress for today?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetCheckboxes();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Imagebuttonclicked(View view) {
        for (int i = 0; i < newArray.length; i++) {
            if (view.getId() == newArray[i]) {
                int value = i + 1;
                Log.i("FIRST", String.valueOf(value));
                Intent intent = new Intent(SecondActivity2.this, ThirdActivity2.class);
                intent.putExtra("value", String.valueOf(value));
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            int workoutIndex = data.getIntExtra("workout_completed", -1);
            if (workoutIndex != -1) {
                markWorkoutCompleted(workoutIndex);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProgress();
        checkCompletionStatus();
    }
}
