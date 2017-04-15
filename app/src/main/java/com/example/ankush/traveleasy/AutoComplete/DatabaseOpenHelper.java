package com.example.ankush.traveleasy.AutoComplete;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by ankushbabbar on 15-Apr-17.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    public  final static String DATABASE_NAME = "travel.db";
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
}