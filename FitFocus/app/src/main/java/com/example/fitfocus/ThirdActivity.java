package com.example.fitfocus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ThirdActivity extends AppCompatActivity {

    String buttonvalue;
    Button startBtn;
    Button alternateBtn;
    private CountDownTimer countDownTimer;
    TextView mtextview;
    private boolean mTimeRunning;
    private long mTimeLeftinmills;
    private static final int TOTAL_WORKOUTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("value")) {
            buttonvalue = intent.getStringExtra("value");
            if (buttonvalue != null) {
                try {
                    int intValue = Integer.parseInt(buttonvalue);
                    switch (intValue) {
                        case 1:
                            setContentView(R.layout.activity_mountainclimber);
                            break;
                        case 2:
                            setContentView(R.layout.activity_basiccrunches);
                            break;
                        case 3:
                            setContentView(R.layout.activity_benchdips);
                            break;
                        case 4:
                            setContentView(R.layout.activity_bicyclecrunches);
                            break;
                        case 5:
                            setContentView(R.layout.activity_legraise);
                            break;
                        case 6:
                            setContentView(R.layout.activity_heeltouches);
                            break;
                        case 7:
                            setContentView(R.layout.activity_legupcrunches);
                            break;
                        case 8:
                            setContentView(R.layout.activity_situps);
                            break;
                        case 9:
                            setContentView(R.layout.activity_alternativevups);
                            break;
                        case 10:
                            setContentView(R.layout.activity_plankrotation);
                            break;
                        default:
                            return;
                    }

                    startBtn = findViewById(R.id.startbutton);
                    alternateBtn = findViewById(R.id.alternate_button);
                    mtextview = findViewById(R.id.time);

                    if (startBtn != null) {
                        startBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mTimeRunning) {
                                    stopTimer();
                                } else {
                                    startTimer();
                                }
                            }
                        });
                    }

                    if (alternateBtn != null) {
                        alternateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ThirdActivity.this, ThirdActivity.class);
                                intent.putExtra("value", "8");
                                startActivity(intent);
                            }
                        });
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mTimeRunning = false;
        startBtn.setText("START");
    }

    private void startTimer() {
        final CharSequence value1 = mtextview.getText();
        String num1 = value1.toString();
        String[] timeParts = num1.split(":");

        if (timeParts.length == 2) {
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);
            int totalSeconds = minutes * 60 + seconds;
            mTimeLeftinmills = totalSeconds * 1000;// Set the timer to 3000 for testing,  totalSeconds * 1000 for real thing

            countDownTimer = new CountDownTimer(mTimeLeftinmills, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftinmills = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    markWorkoutCompleted();
                    if (Integer.parseInt(buttonvalue) < TOTAL_WORKOUTS) {
                        Intent nextWorkoutIntent = new Intent(ThirdActivity.this, ThirdActivity.class);
                        nextWorkoutIntent.putExtra("value", String.valueOf(Integer.parseInt(buttonvalue) + 1));
                        startActivity(nextWorkoutIntent);
                    } else {
                        // Redirect to SecondActivity after the last workout
                        Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }.start();
            startBtn.setText("PAUSE");
            mTimeRunning = true;
        }
    }

    private void updateTimer() {
        int minutes = (int) mTimeLeftinmills / 60000;
        int seconds = (int) (mTimeLeftinmills % 60000) / 1000;

        String timeLeftText = "";
        if (minutes < 10) timeLeftText = "0";
        timeLeftText = timeLeftText + minutes + ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        mtextview.setText(timeLeftText);
    }

    private void markWorkoutCompleted() {
        int workoutIndex = Integer.parseInt(buttonvalue) - 1; // Ensure this calculation is correct

        // Save workout completion in SharedPreferences
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getSharedPreferences("FitnessProgress_" + uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checkbox_" + workoutIndex, true); // Ensure this saves the correct index
        editor.apply();

        Intent intent = new Intent();
        intent.putExtra("workout_completed", workoutIndex);
        setResult(RESULT_OK, intent);

        finish(); // Ensure this line is here to return to SecondActivity
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
