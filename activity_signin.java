package com.example.bankassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class activity_signin extends AppCompatActivity {

    EditText loginUsername, loginPassword;  // Instance Variables
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        loginUsername = findViewById(R.id.login_activity_username);
        loginPassword = findViewById(R.id.login_activity_password);
        loginButton = findViewById(R.id.login_activity_btn_login);
        signupRedirectText = findViewById(R.id.login_activity_register);

        loginButton.setOnClickListener(v -> {
            if (!validateUsername() | !validatePassword()){

            } else {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(activity_signin.this, activity_signup.class);
            startActivity(intent);
        });
    }

    public Boolean validateUsername(){
        String val = loginUsername.getText().toString();
        if (val.isEmpty()){
            loginUsername.setError("Username cannot be empty" );
            return false;
        }else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password cannot be empty" );
            return false;
        }else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (!Objects.equals(passwordFromDB, userPassword)) {
                        loginUsername.setError(null);
                        Intent intent = new Intent(activity_signin.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                }else {
                    loginUsername.setError("User dose not exist");
                    loginUsername.requestFocus();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}