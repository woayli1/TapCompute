package com.tapcompute;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/3.
 */

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TapCompute";
    private static final int DATABASE_VERSION = 1;

    protected static final String TAP_NAME = "tap_name";
    protected static final String TAP_ATTACK = "tap_attack";
    protected static final String TAP_COST = "tap_cost";

    public static final String TABLE_NAME_MAN = "tap_man";
    public static final String TABLE_NAME_WOMAN = "tap_woman";

    public DataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MAN + " (" + TAP_NAME + " VARCHAR, " + TAP_ATTACK + " FLOAT, " + TAP_COST + " FLOAT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_WOMAN + " (" + TAP_NAME + " VARCHAR, " + TAP_ATTACK + " FLOAT, " + TAP_COST + "  FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList getSelectStringItem(String table_name, String items) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> itme = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM '" + table_name + "' order by tap_attack/tap_cost DESC;", null);
        int names = cursor.getColumnIndex(items);
        while (cursor.moveToNext()) {
            itme.add(cursor.getString(names));
        }
        return itme;
    }

    public void insertinto(String table_name, String tap_name, String tap_atc, String tap_cost) {
        SQLiteDatabase db = getWritableDatabase();
        Float tap_at=Float.parseFloat(tap_atc);
        Float tap_cos=Float.parseFloat(tap_cost);
        db.execSQL("insert into " + table_name + " values ('" + tap_name + "','" + tap_at + "','" + tap_cos + "');");
    }

    public void delete(String table_name, String tap_name, String tap_atc) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name + " WHERE " + TAP_NAME + " = '" + tap_name + "' AND " + TAP_ATTACK + " = '" + tap_atc + "';");
    }
}
