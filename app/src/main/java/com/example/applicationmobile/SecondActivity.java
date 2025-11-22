package com.example.applicationmobile;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    ProgressBar progressBar;

    private Button buttonGoToAct1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);


        progressBar = findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.progressBar);
        ObjectAnimator.ofInt(progressBar, "progress", 75, 100)
                .setDuration(800)
                .start();

        buttonGoToAct1 = findViewById(R.id.buttonGoToAct1);
        buttonGoToAct1.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RelAct1.class);
            startActivity(intent);
        });

    }
}