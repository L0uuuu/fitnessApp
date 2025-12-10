package com.example.applicationmobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RelAct2 extends AppCompatActivity {

    TextView tvName, tvAge, tvGender, tvCalories, tvMealSuggestions;
    Button btnGoBack, btnGetMeals;
    ProgressBar progressBar;
    ScrollView scrollView;
    double finalCalories = 0;
    String gender = "";
    int age = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act2);

        // Connect UI elements
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvCalories = findViewById(R.id.tvCalories);
        tvMealSuggestions = findViewById(R.id.tvMealSuggestions);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGetMeals = findViewById(R.id.btnGetMeals);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name");
            age = extras.getInt("age");
            double weight = extras.getDouble("weight");
            double height = extras.getDouble("height");
            gender = extras.getString("gender");
            int activityLevel = extras.getInt("activityLevel");

            // Calculate BMR
            double calories = CalorieUtils.calculateCalories(gender, age, height, weight, activityLevel);
            finalCalories = calories;

            // Display results
            tvName.setText("👤 Name: " + name);
            tvAge.setText("🎂 Age: " + age + " years");
            tvGender.setText("⚧ Gender: " + gender);
            tvCalories.setText("🔥 Estimated Calorie Intake:\n" + (int) calories + " kcal/day");
        }

        // Get AI Meal Suggestions button
        btnGetMeals.setOnClickListener(v -> {
            getMealSuggestionsFromAI();
        });

        // Go Back button
        btnGoBack.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("calories", finalCalories);
            setResult(RESULT_OK, returnIntent);
            finish();
        });
    }
    public static String markdownToHtml(String markdown) {
        markdown = markdown.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>"); // bold
        markdown = markdown.replaceAll("(?m)^- ", "• "); // bullets
        markdown = markdown.replaceAll("(?m)^\\* ", "• ");
        markdown = markdown.replace("\n", "<br>"); // line breaks
        return markdown;
    }

    private void getMealSuggestionsFromAI() {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        btnGetMeals.setEnabled(false);
        tvMealSuggestions.setText("🤖 Generating personalized meal plans...");

        // Use Google Gemini API (fast and reliable!)
        GeminiApiHelper apiHelper = new GeminiApiHelper();
        apiHelper.getMealSuggestions(finalCalories, gender, age, new GeminiApiHelper.MealSuggestionCallback() {
            @Override
            public void onSuccess(String mealSuggestions) {
                progressBar.setVisibility(View.GONE);
                btnGetMeals.setEnabled(true);
                String html = markdownToHtml(mealSuggestions);
                tvMealSuggestions.setText("🍽️ Your Personalized Meal Plans:\n\n" + Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));

                // Scroll to show suggestions
                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                btnGetMeals.setEnabled(true);
                tvMealSuggestions.setText("❌ " + error);
                Toast.makeText(RelAct2.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}