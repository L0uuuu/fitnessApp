// MainActivity.java
package com.example.applicationmobile;  // ← CHANGE THIS TO YOUR REAL PACKAGE NAME

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    // All your buttons
    private MaterialButton btnSignIn;
    private MaterialButton btnSignUp;
    private ImageView btnFacebook;
    private ImageView btnGoogle;
    private ImageView btnApple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // ← make sure your XML is named activity_main.xml

        // Find all views
        btnSignIn   = findViewById(R.id.btnSignIn);
        btnSignUp   = findViewById(R.id.btnSignUp);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnGoogle   = findViewById(R.id.btn_google);
        btnApple    = findViewById(R.id.btn_apple);

        // === SIGN IN BUTTON ===
        btnSignIn.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Sign In screen...", Toast.LENGTH_SHORT).show();
            // TODO: Start your Sign-In Activity or show dialog
            // Example: startActivity(new Intent(MainActivity.this, SignInActivity.class));
        });

        // === SIGN UP BUTTON ===
        btnSignUp.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Sign Up screen...", Toast.LENGTH_SHORT).show();
            // TODO: Start your Sign-Up Activity
            // startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });

        // === FACEBOOK ===
        btnFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Facebook Login", Toast.LENGTH_SHORT).show();
            // TODO: Facebook SDK login here
        });

        // === GOOGLE ===
        btnGoogle.setOnClickListener(v -> {
            Toast.makeText(this, "Google Login", Toast.LENGTH_SHORT).show();
            // TODO: Google Sign-In here
        });

        // === APPLE ===
        btnApple.setOnClickListener(v -> {
            Toast.makeText(this, "Apple Login", Toast.LENGTH_SHORT).show();
            // TODO: Sign in with Apple
        });
    }
}