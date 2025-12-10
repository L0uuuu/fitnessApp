package com.example.applicationmobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {
    TextView text1;
    TextView txtSignIn;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize all views
        text1 = findViewById(R.id.text1);
        txtSignIn = findViewById(R.id.txtSignIn);
        btnSignUp = findViewById(R.id.btnSignUp); // ADD THIS LINE

        //changing the text color
        String fullText = "Create Your\nAccount";

        //SpannableString is a special type of string that allows us to apply different styles (spans) to different parts of the text
        SpannableString spannableString = new SpannableString(fullText);
        //Converts the hex color code #BBF246  into Android's color format
        int customColor = Color.parseColor("#BBF246");
        // Color the first 'C'
        spannableString.setSpan(
                new ForegroundColorSpan(customColor), // creates a color span with our green color
                0, 1, //applies the color from index 0 to index 1 (just the first character 'C')
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE // means the span doesn't expand if text is added before or after it
        );
        // Color the 'Y' in "Your"
        spannableString.setSpan(
                new ForegroundColorSpan(customColor),
                7, 8,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Color the word "Account"
        spannableString.setSpan(
                new ForegroundColorSpan(customColor),
                13, fullText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        text1.setText(spannableString);

        String text = "Sign In";
        SpannableString spannableStringSignIn = new SpannableString(text);
        spannableStringSignIn.setSpan(
                new ForegroundColorSpan(customColor),
                5, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        txtSignIn.setText(spannableStringSignIn);

        // Now btnSignUp is properly initialized
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, FirstActivity.class);
            startActivity(intent);
        });

    }
}