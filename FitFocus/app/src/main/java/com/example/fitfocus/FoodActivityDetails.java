package com.example.fitfocus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Html;

public class FoodActivityDetails extends AppCompatActivity {

    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        textView = findViewById(R.id.txt);
        imageView = findViewById(R.id.food_image);

        Intent intent = getIntent();
        if (intent != null) {
            String story = intent.getStringExtra("story");
            int imageResource = intent.getIntExtra("image", -1);

            if (story != null && !story.isEmpty()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    textView.setText(Html.fromHtml(story, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textView.setText(Html.fromHtml(story));
                }
            } else {
                textView.setText("No content available");
            }

            if (imageResource != -1) {
                imageView.setImageResource(imageResource);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground); // default image
            }
        }
    }

    public void goback(View view) {
        Intent intent = new Intent(FoodActivityDetails.this, FoodActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FoodActivityDetails.this, FoodActivity.class);
        startActivity(intent);
        finish();
    }
}
