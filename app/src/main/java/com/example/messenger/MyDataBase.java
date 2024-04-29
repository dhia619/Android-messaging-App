package com.example.messenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper {
    public static final String db_name = "messenger";
    public static final int db_version = 1;
    public MyDataBase(Context context){
        super(context,db_name,null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id INTEGER PRIMARY KEY AUTOINCREMENT,full_name TEXT,email TEXT,phone_number TEXT,profile_image TEXT,birthdate TEXT,pwd TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table user(id INTEGER PRIMARY KEY AUTOINCREMENT,full_name TEXT,email TEXT,phone_number TEXT,profile_image TEXT,birthdate TEXT,pwd TEXT)");
    }

    public boolean insertUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name",user.getFull_name());
        values.put("email",user.getEmail());
        values.put("phone_number",user.getPhone_number());
        values.put("profile_image",user.getProfile_image());
        values.put("birthdate",user.getBirthdate());
        values.put("pwd",user.getPwd());

        long result = db.insert("user",null,values);

        return result != -1;

    }

    public long user_exist(User user){
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db,"user","email=?");
    }

}
