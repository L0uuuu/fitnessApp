package com.example.applicationmobile;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import java.util.concurrent.TimeUnit;

public class GeminiApiHelper {
    private static final String TAG = "GeminiAPI";
    private static final String API_KEY = "AIzaSyD-77GCY7JMhJsIiMSub2BniZgNv1o59_0"; // PASTE YOUR FULL API KEY HERE

    private final OkHttpClient client;

    public interface MealSuggestionCallback {
        void onSuccess(String mealSuggestions);
        void onError(String error);
    }

    public GeminiApiHelper() {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .callTimeout(90, TimeUnit.SECONDS)
                .build();
    }

    public void getMealSuggestions(double calories, String gender, int age, MealSuggestionCallback callback) {
        new Thread(() -> {
            try {
                // Build the URL with API key
                String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

                Log.d(TAG, "API Key length: " + API_KEY.length());
                Log.d(TAG, "Using URL: " + url.substring(0, Math.min(100, url.length())) + "...");

                String prompt = String.format(
                        "Create 3 different daily meal plans for a %d-year-old %s with %.0f kcal daily target.\n\n" +
                                "Format each plan as:\n\n" +
                                "MEAL PLAN 1:\n" +
                                "Breakfast: [specific meal with portions] (~XXX kcal)\n" +
                                "Lunch: [specific meal with portions] (~XXX kcal)\n" +
                                "Dinner: [specific meal with portions] (~XXX kcal)\n" +
                                "Snack 1: [snack] (~XX kcal)\n" +
                                "Snack 2: [snack] (~XX kcal)\n\n" +
                                "Make the meals practical and balanced.",
                        age, gender, calories
                );

                // Build the request body according to Gemini API specs
                JSONObject requestBody = new JSONObject();

                JSONArray contentsArray = new JSONArray();
                JSONObject contentObject = new JSONObject();
                JSONArray partsArray = new JSONArray();
                JSONObject partObject = new JSONObject();

                partObject.put("text", prompt);
                partsArray.put(partObject);
                contentObject.put("parts", partsArray);
                contentsArray.put(contentObject);

                requestBody.put("contents", contentsArray);

                Log.d(TAG, "Request body: " + requestBody.toString());

                RequestBody body = RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("x-goog-api-key", API_KEY)
                        .post(body)
                        .build();

                Log.d(TAG, "Sending request to Gemini API...");
                Response response = client.newCall(request).execute();

                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response body: " + responseBody);

                    JSONObject jsonResponse = new JSONObject(responseBody);

                    // Parse the response
                    if (jsonResponse.has("candidates")) {
                        JSONArray candidates = jsonResponse.getJSONArray("candidates");
                        if (candidates.length() > 0) {
                            JSONObject firstCandidate = candidates.getJSONObject(0);
                            JSONObject content = firstCandidate.getJSONObject("content");
                            JSONArray parts = content.getJSONArray("parts");
                            String mealSuggestions = parts.getJSONObject(0).getString("text");

                            String finalSuggestions = mealSuggestions;
                            new Handler(Looper.getMainLooper()).post(() ->
                                    callback.onSuccess(finalSuggestions)
                            );
                        } else {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    callback.onError("No candidates in response")
                            );
                        }
                    } else {
                        new Handler(Looper.getMainLooper()).post(() ->
                                callback.onError("Invalid response format")
                        );
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No error body";
                    Log.e(TAG, "Error response: " + errorBody);

                    String errorMessage;
                    if (response.code() == 400) {
                        errorMessage = "Bad request - Check API key format";
                    } else if (response.code() == 403) {
                        errorMessage = "API key is invalid or doesn't have permission";
                    } else if (response.code() == 429) {
                        errorMessage = "Rate limit exceeded - wait a moment";
                    } else {
                        errorMessage = "Error " + response.code() + ": " + errorBody;
                    }

                    String finalError = errorMessage;
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onError(finalError)
                    );
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception occurred", e);
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("Exception: " + e.getMessage())
                );
            }
        }).start();
    }
}