package com.example.applicationmobile;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.applicationmobile.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity {

    private ActivityFirstBinding binding;
    private BroadcastReceiver stepReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button click → go to SecondActivity using binding
        binding.buttonGoToSecond.setOnClickListener(v -> {
            Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        // Initialize LocalBroadcastManager
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        // Start the step counter service
        startStepCounterService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the broadcast receiver
        registerStepReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the broadcast receiver when activity is paused
        unregisterStepReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear binding reference to prevent memory leaks
        binding = null;
    }

    private void startStepCounterService() {
        Intent serviceIntent = new Intent(this, StepCounterService.class);
        startService(serviceIntent);
    }

    private void registerStepReceiver() {
        stepReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (StepCounterService.ACTION_STEP_UPDATE.equals(intent.getAction())) {
                    int stepCount = intent.getIntExtra(StepCounterService.EXTRA_STEP_COUNT, 0);

                    // Update UI with new step count using binding
                    updateStepCount(stepCount);

                    // Update progress bar based on steps (example: every 10 steps = 1% progress)
                    updateProgressBar(stepCount);
                }
            }
        };

        // Register the receiver for step updates
        IntentFilter filter = new IntentFilter(StepCounterService.ACTION_STEP_UPDATE);
        localBroadcastManager.registerReceiver(stepReceiver, filter);
    }

    private void unregisterStepReceiver() {
        if (stepReceiver != null) {
            localBroadcastManager.unregisterReceiver(stepReceiver);
            stepReceiver = null;
        }
    }

    private void updateStepCount(int stepCount) {
        if (binding != null && binding.stepCountTextView != null) {
            binding.stepCountTextView.setText("Steps: " + stepCount);
        }
    }

    private void updateProgressBar(int stepCount) {
        if (binding != null && binding.progressBar != null) {
            // Example: every 10 steps = 1% progress, up to 100%
            int progress = Math.min(stepCount / 10, 100);
            binding.progressBar.setProgress(progress);
        }
    }
}