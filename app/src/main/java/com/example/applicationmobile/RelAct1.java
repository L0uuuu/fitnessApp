package com.example.applicationmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RelAct1 extends AppCompatActivity  {

    EditText etName, etAge, etWeight, etHeight;
    RadioGroup radioGroupGender;
    SeekBar seekBarActivity;
    TextView tvActivityLabel;
    Button btnCalculate;

    int activityLevel = 0; // default (Sedentary)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_act1); // make sure this matches your XML filename

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calorieInputLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 Link XML elements
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        seekBarActivity = findViewById(R.id.seekBarActivity);
        tvActivityLabel = findViewById(R.id.tvActivityLabel);
        btnCalculate = findViewById(R.id.btnCalculate);

        // 🔹 Handle SeekBar movement
        seekBarActivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                activityLevel = progress;
                String[] levels = {"Sedentary", "Light", "Moderate", "Active"};
                tvActivityLabel.setText("Activity Level: " + (progress + 1) + " (" + levels[progress] + ")");
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 🔹 Button click: collect and send info
        btnCalculate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            int age = Integer.parseInt(etAge.getText().toString().trim());
            double weight = Double.parseDouble(etWeight.getText().toString().trim());
            double height = Double.parseDouble(etHeight.getText().toString().trim());

            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            String gender = selectedGenderButton.getText().toString();

            // 🔹 Bundle data
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putInt("age", age);
            bundle.putDouble("weight", weight);
            bundle.putDouble("height", height);
            bundle.putString("gender", gender);
            bundle.putInt("activityLevel", activityLevel);

            // 🔹 Send to second activity
            Intent intent = new Intent(RelAct1.this, RelAct2.class);
            intent.putExtras(bundle);
            // OLD STYLE
            startActivityForResult(intent, 100); // request code = 100
            //finish();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            double calories = data.getDoubleExtra("calories", 0);
            // For example show a toast or do something useful
            Toast.makeText(this, "Daily Calories: " + calories, Toast.LENGTH_LONG).show();
        }
    }
}
