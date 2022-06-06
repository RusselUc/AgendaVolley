package com.example.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Sqlite extends SQLiteOpenHelper {
    public Sqlite(Context context){
        super(context, "Agenda", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table users(id  INTEGER PRIMARY KEY, name TEXT, password TEXT)");
        sqLiteDatabase.execSQL("create table eventos(id  INTEGER PRIMARY KEY,titulo Text, activity TEXT,today TEXT, hourmin TEXT, id_user INTEGER references users(id))");
        sqLiteDatabase.execSQL("create table favoritos(id  INTEGER PRIMARY KEY,id_user INTEGER references users(id), id_eventos INTEGER references eventos(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
