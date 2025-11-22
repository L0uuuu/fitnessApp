package com.example.applicationmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RelAct2 extends AppCompatActivity {

    TextView tvName, tvAge, tvGender, tvCalories;
    Button btnGoBack;
    double finalCalories=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act2);


        // 🔹 Connect UI elements
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvCalories = findViewById(R.id.tvCalories);
        btnGoBack = findViewById(R.id.btnGoBack);

        // 🔹 Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name");
            int age = extras.getInt("age");
            double weight = extras.getDouble("weight");
            double height = extras.getDouble("height");
            String gender = extras.getString("gender");
            int activityLevel = extras.getInt("activityLevel");

            // 🔹 Calculate BMR
            double calories = CalorieUtils.calculateCalories(gender, age, height, weight, activityLevel);
            finalCalories = calories;
            // 🔹 Display results nicely
            tvName.setText("👤 Name: " + name);
            tvAge.setText("🎂 Age: " + age + " years");
            tvGender.setText("⚧ Gender: " + gender);
            tvCalories.setText("🔥 Estimated Calorie Intake:\n" + (int) calories + " kcal/day");
        }

        // 🔹 Go Back button listener
        btnGoBack.setOnClickListener(v -> {
            double calculatedCalories = finalCalories; // your calculated result

            Intent returnIntent = new Intent();
            returnIntent.putExtra("calories", calculatedCalories);
            setResult(RESULT_OK, returnIntent);
            finish(); // go back to RelAct1
        });
    }
}
