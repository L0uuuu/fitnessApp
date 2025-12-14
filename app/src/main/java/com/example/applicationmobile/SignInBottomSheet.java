package com.example.applicationmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SignInBottomSheet extends BottomSheetDialogFragment {

    private EditText emailInput, passwordInput;
    private DatabaseHelper databaseHelper;

    public SignInBottomSheet() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize database
        databaseHelper = new DatabaseHelper(requireContext());

        // Find EditText fields
        emailInput = view.findViewById(R.id.email_input);
        passwordInput = view.findViewById(R.id.password_input);

        // Set up Sign In button click listener
        view.findViewById(R.id.btnSignIn).setOnClickListener(v -> signIn());

        // Set up Sign Up text click listener (optional)
        view.findViewById(R.id.txtSignUp).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Sign Up clicked", Toast.LENGTH_SHORT).show();
        });

        // Setup full expansion after view is created
        view.post(() -> setupBottomSheet());
    }

    private void setupBottomSheet() {
        if (getDialog() instanceof BottomSheetDialog) {
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();

            // Get the bottom sheet view
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                // Make the bottom sheet background transparent
                bottomSheet.setBackgroundResource(android.R.color.transparent);

                // Get and configure the behavior
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
                behavior.setPeekHeight(0);

                // Set height to match parent
                ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
                if (params != null) {
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    bottomSheet.setLayoutParams(params);
                }
            }

            // Configure the dialog window
            Window window = dialog.getWindow();
            if (window != null) {
                // Make window background transparent
                window.setBackgroundDrawableResource(android.R.color.transparent);

                // Set window to full screen
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                // Set dim amount for background activity visibility
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.dimAmount = 0.0f; // No additional dim from window
                window.setAttributes(layoutParams);
            }
        }
    }

    private void signIn() {
        // Get user input
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials in database
        boolean isAuthenticated = databaseHelper.authenticateUser(email, password);

        if (isAuthenticated) {
            Toast.makeText(requireContext(), "Sign in successful!", Toast.LENGTH_SHORT).show();

            // Dismiss the bottom sheet
            dismiss();

            // Simply navigate to FirstActivity
            Intent intent = new Intent(requireContext(), FirstActivity.class);
            startActivity(intent);

            // Optional: Close current activity if needed
            // requireActivity().finish();

        } else {
            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setupBottomSheet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close database connection
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}