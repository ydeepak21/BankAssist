package com.example.bankassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_signup extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.register_activity_name);
        signupEmail = findViewById(R.id.register_activity_email);
        signupUsername = findViewById(R.id.register_activity_username);
        signupPassword = findViewById(R.id.register_activity_password);
        signupButton = findViewById(R.id.register_activity_btn);
        loginRedirectText = findViewById(R.id.register_activity_login);

        signupButton.setOnClickListener(v -> {
            String name = signupName.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String username = signupUsername.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                signupName.setError("Please enter your name");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                signupEmail.setError("Please enter your email");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signupEmail.setError("Please enter a valid email address");
                return;
            }

            if (TextUtils.isEmpty(username)) {
                signupUsername.setError("Please enter a username");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                signupPassword.setError("Please enter a password");
                return;
            }

            if (password.length() < 6) {
                signupPassword.setError("Password must be at least 6 characters long");
                return;
            }

            // Perform signup operation
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");
            HelperClass helperClass = new HelperClass(name, email, username, password);
            reference.child(name).setValue(helperClass);

            Toast.makeText(activity_signup.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity_signup.this, activity_signin.class);
            startActivity(intent);
        });

        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(activity_signup.this, activity_signin.class);
            startActivity(intent);
        });
    }
}
