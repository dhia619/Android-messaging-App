package com.example.messenger;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper {
    public static final String db_name = "messenger";
    public static final int db_version = 1;
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "pwd";
    private static final String TABLE_NAME = "user";

    public MyDataBase(Context context){
        super(context,db_name,null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(email TEXT,pwd TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table user(email TEXT,pwd TEXT)");
    }

    public boolean insertUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email",user.getEmail());
        values.put("pwd",user.getPwd());

        long result = db.insert("user",null,values);

        return result != -1;

    }

    // Function to delete all rows from the table
    public void deleteAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    // Function to retrieve email and password of the first row in the table
    public User retrieveFirstUser() {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " LIMIT 1", null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            user = new User(email, password);
        }
        cursor.close();
        db.close();
        return user;
    }

    public long user_exist(User user){
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db,"user","email=?");
    }

}
