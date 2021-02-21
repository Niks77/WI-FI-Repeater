package com.example.wifidirect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Switch;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {
    final String TABLE_NAME = "UiState";
    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String TABLE = "Create Table IF NOT EXISTS " + TABLE_NAME + "(id Integer Primary Key, Switch1 Integer , Switch2 Integer, Switch3 Integer)";
            db.execSQL(TABLE);
            String query = "Insert into " + TABLE_NAME + " values(1,0,0,0)";
            db.execSQL(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void updateState(int s1,int s2,int s3){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateState = "Update " + TABLE_NAME + " Set Switch1 = " + s1  +" , Switch2 = " + s2 + " , Switch3 = " + s3 +  " where id=1";
        db.execSQL(updateState);

    }
    public HashMap<String,Integer> getState(){
        HashMap<String,Integer> map = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME + " where id = 1";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                map.put("Switch1",Integer.parseInt(cursor.getString(1)));
                map.put("Switch2",Integer.parseInt(cursor.getString(2)));
                map.put("Switch3",Integer.parseInt(cursor.getString(3)));

            }while (cursor.moveToNext());
        }

        return map;
    }
}
