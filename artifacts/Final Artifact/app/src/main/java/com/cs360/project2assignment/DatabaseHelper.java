package com.cs360.project2assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventTracking.db";
    private static final int DATABASE_VERSION = 2; // Increment version for migrations

    private static final String USERS_TABLE = "users";
    private static final String EVENTS_TABLE = "events";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, email TEXT)");
        db.execSQL("CREATE TABLE " + EVENTS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES " + USERS_TABLE + "(id))");
        Log.d("DatabaseHelper", "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + USERS_TABLE + " ADD COLUMN email TEXT");
            Log.d("DatabaseHelper", "Database upgraded to version 2: email column added");
        }
        // Handle further migrations for higher versions
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put("password", hashedPassword);

        try {
            long result = db.insertOrThrow(USERS_TABLE, null, contentValues);
            Log.d("DatabaseHelper", "User inserted successfully: " + username);
            return result != -1;
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error inserting user: " + username, e);
            return false;
        } finally {
            db.close();
        }
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM " + USERS_TABLE + " WHERE username=?", new String[]{username});

        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(0);
            boolean isValid = BCrypt.checkpw(password, storedHash);
            Log.d("DatabaseHelper", "User login attempt: " + username + " - Success: " + isValid);
            cursor.close();
            db.close();
            return isValid;
        }

        Log.d("DatabaseHelper", "User login failed: " + username + " - User not found");
        cursor.close();
        db.close();
        return false;
    }

    // Add event with error handling and parameterized query
    public long addEvent(String eventName, String eventDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", eventName);
        contentValues.put("date", eventDate);
        try {
            // Insert the event and return the row ID
            return db.insertOrThrow("events", null, contentValues);
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error adding event", e);
            return -1;
        } finally {
            db.close();
        }
    }
}

