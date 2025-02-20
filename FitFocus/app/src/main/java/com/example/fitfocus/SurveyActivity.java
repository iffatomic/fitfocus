package com.example.fitfocus;

import android.content.Intent;  // Import this
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {

    private static final String TAG = "SurveyActivity";

    private RadioGroup ageGroup, experienceGroup;
    private EditText heightInput, weightInput;
    private Button submitButton, goToMainButton;
    private TextView bmiResult;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ageGroup = findViewById(R.id.ageGroup);
        experienceGroup = findViewById(R.id.experienceGroup);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        submitButton = findViewById(R.id.submitButton);
        bmiResult = findViewById(R.id.bmiResult);
        goToMainButton = findViewById(R.id.goToMainButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });

        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void submitSurvey() {
        if (currentUser == null) {
            Toast.makeText(SurveyActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedAgeId = ageGroup.getCheckedRadioButtonId();
        int selectedExperienceId = experienceGroup.getCheckedRadioButtonId();

        if (selectedAgeId == -1 || selectedExperienceId == -1) {
            Toast.makeText(SurveyActivity.this, "Please select age and experience", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedAge = findViewById(selectedAgeId);
        RadioButton selectedExperience = findViewById(selectedExperienceId);

        String age = selectedAge.getText().toString();
        String experience = selectedExperience.getText().toString();
        String height = heightInput.getText().toString();
        String weight = weightInput.getText().toString();

        if (height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(SurveyActivity.this, "Please input height and weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double heightValue;
        double weightValue;
        try {
            heightValue = Double.parseDouble(height);
            weightValue = Double.parseDouble(weight);
        } catch (NumberFormatException e) {
            Toast.makeText(SurveyActivity.this, "Invalid height or weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double bmi = weightValue / ((heightValue / 100) * (heightValue / 100));
        String workoutPlan = (bmi >= 25) ? "Weight loss" : "Muscle gain";

        Map<String, Object> surveyData = new HashMap<>();
        surveyData.put("userId", currentUser.getUid());
        surveyData.put("age", age);
        surveyData.put("experience", experience);
        surveyData.put("height", heightValue);
        surveyData.put("weight", weightValue);
        surveyData.put("bmi", bmi);
        surveyData.put("workoutPlan", workoutPlan);

        Log.d(TAG, "Submitting survey data: " + surveyData);

        db.collection("surveys")
                .add(surveyData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SurveyActivity.this, "Survey submitted successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Survey submitted successfully");

                            // Display BMI and suggested workout plan
                            bmiResult.setText(String.format("BMI: %.2f\nSuggested workout plan: %s", bmi, workoutPlan));
                            bmiResult.setVisibility(View.VISIBLE);

                            // Show the Go to Main button
                            goToMainButton.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(SurveyActivity.this, "Failed to submit survey", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Failed to submit survey", task.getException());
                        }
                    }
                });
    }
}
