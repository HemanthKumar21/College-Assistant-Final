package com.example.hemanth.collegeassistant.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "student.db";
    public static final String TABLE_NAME = "info";
    public static final String COL_1 = "SID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SEMESTER";
    public static final String COL_4 = "CGPA";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (SID TEXT PRIMARY KEY,NAME TEXT,SEMESTER INTEGER,CGPA REAL)");
        db.execSQL("CREATE TABLE LOGDATA (QID INTEGER,QUESTION TEXT,QTYPE TEXT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS LOGDATA");
        onCreate(db);
    }

    public boolean insertData(String sid,String name,String semester,String cgpa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,sid);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,semester);
        contentValues.put(COL_4,cgpa);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void insertLog(int qid,String query,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        final String insertData = "INSERT INTO LOGDATA VALUES('"+qid+"','"+query+"','"+type+"')";
        db.execSQL(insertData);
    }

    public Cursor getData(String Roll) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE SID = '"+Roll+"'",null);
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }

    public Cursor getLog() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LOGDATA",null);
        return cursor;
    }

    public Cursor getLog(int qid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM LOGDATA WHERE QID = '"+qid+"'",null);
        return res;
    }

    public void deleteLog(int qid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("LOGDATA", "QID = '"+qid+"'", null);
    }
}
