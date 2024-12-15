package com.cs360.project2assignment;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class EventsGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_grid);

        String username = getIntent().getStringExtra("USERNAME");
        if (username != null) {
            Log.d("EventsGridActivity", "Logged in user: " + username);
        }
    }
}
