package com.example.applicationmobile;

public class CalorieUtils {

    public static double calculateCalories(String gender, int age, double height, double weight, int activityLevel) {
        // 1️⃣ Basal Metabolic Rate (BMR) formula (Mifflin-St Jeor Equation)
        double bmr;
        if (gender.equalsIgnoreCase("Male")) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // 2️⃣ Activity multiplier
        double multiplier;
        switch (activityLevel) {
            case 0: multiplier = 1.2; break;   // Sedentary
            case 1: multiplier = 1.375; break; // Light
            case 2: multiplier = 1.55; break;  // Moderate
            case 3: multiplier = 1.725; break; // Active
            case 4: multiplier = 1.9; break;   // Very Active
            default: multiplier = 1.2;
        }

        // 3️⃣ Final calorie intake
        return bmr * multiplier;
    }
}
