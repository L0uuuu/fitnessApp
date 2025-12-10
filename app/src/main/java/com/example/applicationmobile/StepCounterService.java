package com.example.applicationmobile;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class StepCounterService extends Service {
    public static final String ACTION_STEP_UPDATE = "com.example.applicationmobile.STEP_UPDATE";
    public static final String EXTRA_STEP_COUNT = "step_count";

    private Handler handler;
    private Runnable stepRunnable;
    private int stepCount = 0;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        Log.d("StepCounterService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            startStepCounter();
            isRunning = true;
            Log.d("StepCounterService", "Service started");
        }
        return START_STICKY; // Service will be restarted if killed
    }

    private void startStepCounter() {
        stepRunnable = new Runnable() {
            @Override
            public void run() {
                stepCount++;

                // Send broadcast with updated step count
                Intent broadcastIntent = new Intent(ACTION_STEP_UPDATE);
                broadcastIntent.putExtra(EXTRA_STEP_COUNT, stepCount);
                LocalBroadcastManager.getInstance(StepCounterService.this).sendBroadcast(broadcastIntent);

                Log.d("StepCounterService", "Step count: " + stepCount);

                // Schedule next step (every 1 second)
                handler.postDelayed(this, 1000);
            }
        };

        // Start the step counter
        handler.post(stepRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && stepRunnable != null) {
            handler.removeCallbacks(stepRunnable);
        }
        isRunning = false;
        Log.d("StepCounterService", "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // We're not using binding
    }
}