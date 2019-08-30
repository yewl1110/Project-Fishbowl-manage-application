package com.example.fish;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper implements Serializable {

    static int DATABASE_VER = 1;
    static final String DATABASE_NAME = "app.db";

    static final String TABLE_NAME = "app";

    static final String COLUMN_ID = "id";
    static final String COLUMN_SERIAL = "serial";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_TEMPERATURE = "temperature";
    static final String COLUMN_LIGHT = "light";
    static final String COLUMN_pH = "ph";
    static final String COLUMN_ADUINO_SERIAL = "aduino_serial";
    static final String COLUMN_LAST_UPDATE = "last_update";


    static final String createQuery = "create table if not exists " + TABLE_NAME + "(" +
            COLUMN_SERIAL + " character(20) not null, " +
            COLUMN_ID + " character(20) not null primary key, " +
            COLUMN_NAME + " character(20), " +
            COLUMN_TEMPERATURE + " double, " +
            COLUMN_LIGHT + " int, " +
            COLUMN_pH + " double, " +
            COLUMN_ADUINO_SERIAL + " character(20), " +
            COLUMN_LAST_UPDATE + " datetime );";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 1:{
                db.execSQL("drop database " + DATABASE_NAME + ";");
                db.execSQL(createQuery);
                break;
            }
        }
    }

    public boolean insert(SQLiteDatabase db, Column column) {
        if (db == null) {
            return false;
        }

        String query = "insert into " + TABLE_NAME + " ( " + COLUMN_SERIAL + ", " + COLUMN_ID + ", " + COLUMN_NAME + ") ";
        query += "values ('" + column.id + "', '" + column.serial + "', '" + column.name + "');";
        Log.e("insertt", query);
        db.execSQL(query);
        return true;
    }

    public Cursor select(SQLiteDatabase db) {
        if (db == null) return null;

        String query = "select * from " + TABLE_NAME + ";";
        return db.rawQuery(query, null);
    }

    public Cursor select(SQLiteDatabase db, String id) {
        if (db == null) return null;

        String query = "select * from " + TABLE_NAME + " where " + COLUMN_ID + "= '" + id + "';";
        return db.rawQuery(query, null);
    }

    public void update(SQLiteDatabase db, String id, String name) {
        String query = "update " + TABLE_NAME + " set " + COLUMN_NAME + " = '" + name + "' where " + COLUMN_ID + " = '" + id + "';";
        db.execSQL(query);
    }

    public void delete(SQLiteDatabase db, String id) {
        String query = "delete from " + TABLE_NAME + " where " + COLUMN_ID + " = '" + id + "';";
        Log.e("deletee", query);
        db.execSQL(query);
    }

    public void delete(SQLiteDatabase db, ArrayList<String> idArray) {
        if (idArray.isEmpty()) return;

        String query = "delete from " + TABLE_NAME + " where " + COLUMN_ID + " in (";
        for (int i = 0; i < idArray.size() - 1; i++) {
            query += " '" + idArray.get(i) + "',";
        }
        query += "'" + idArray.get(idArray.size() - 1) + "');";
        Log.e("deletee", query);
        db.execSQL(query);
    }
}
