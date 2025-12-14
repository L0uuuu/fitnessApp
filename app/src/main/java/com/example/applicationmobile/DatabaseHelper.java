// DatabaseHelper.java
package com.example.applicationmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";

    // Create table SQL
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_EMAIL + " TEXT UNIQUE," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_FULL_NAME + " TEXT" +
                    ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);

        // Add a sample user for testing (optional)
        addSampleUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Add a sample user for testing
    private void addSampleUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, "test@example.com");
        values.put(COLUMN_PASSWORD, "password123");
        values.put(COLUMN_FULL_NAME, "John Doe");
        db.insert(TABLE_USERS, null, values);
    }

    // Method to register a new user
    public boolean registerUser(String email, String password, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FULL_NAME, fullName);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Method to check if email exists
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to authenticate user (check email and password)
    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password}
        );
        boolean authenticated = cursor.getCount() > 0;
        cursor.close();
        return authenticated;
    }

    // Method to get user details by email - FIXED VERSION
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email}
        );

        User user = null;
        if (cursor.moveToFirst()) {
            // Get column indexes safely
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);

            // Only create user if all columns exist
            if (idIndex >= 0 && emailIndex >= 0 && nameIndex >= 0) {
                user = new User(
                        cursor.getInt(idIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(nameIndex)
                );
            }
        }
        cursor.close();
        db.close();
        return user;
    }
}