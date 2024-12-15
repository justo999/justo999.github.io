package com.cs360.project2assignment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        Button createAccountButton = findViewById(R.id.buttonCreateAccount);

        db = new DatabaseHelper(this);

        createAccountButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(CreateAccountActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.insertUser(username, password)) {
                Log.d("CreateAccountActivity", "User created successfully: " + username);
                Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                finish(); // Return to login screen
            } else {
                Log.e("CreateAccountActivity", "Failed to create user: " + username);
                Toast.makeText(CreateAccountActivity.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
