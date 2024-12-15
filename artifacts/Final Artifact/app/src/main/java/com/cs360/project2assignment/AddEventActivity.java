package com.cs360.project2assignment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextEventName, editTextEventDate;
    private Button buttonAddEvent;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Make sure the IDs match those in the layout XML
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);

        databaseHelper = new DatabaseHelper(this);

        buttonAddEvent.setOnClickListener(v -> {
            String eventName = editTextEventName.getText().toString().trim();
            String eventDate = editTextEventDate.getText().toString().trim();

            // Validate input fields
            if (eventName.isEmpty() || eventDate.isEmpty()) {
                Toast.makeText(AddEventActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to add the event
            long result = databaseHelper.addEvent(eventName, eventDate);
            if (result != -1) {
                Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


