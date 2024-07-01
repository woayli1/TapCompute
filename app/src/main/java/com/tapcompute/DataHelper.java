package com.tapcompute;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/3.
 */

public class DataHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String TAP_NAME = "tap_name";
    private static final String TAP_ATTACK = "tap_attack";
    private static final String TAP_COST = "tap_cost";

    private static final String TABLE_NAME_MAN = "tap_man";
    private static final String TABLE_NAME_WOMAN = "tap_woman";

    DataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MAN + " (" + TAP_NAME + " VARCHAR, " + TAP_ATTACK + " FLOAT, " + TAP_COST + " FLOAT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_WOMAN + " (" + TAP_NAME + " VARCHAR, " + TAP_ATTACK + " FLOAT, " + TAP_COST + "  FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String> getSelectStringItem(String table_name, String items) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> item = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM '" + table_name + "' order by tap_attack/tap_cost DESC;", null);
        int names = cursor.getColumnIndex(items);
        while (cursor.moveToNext()) {
            item.add(cursor.getString(names));
        }
        db.close();
        cursor.close();
        return item;
    }

    void insertInto(String table_name, String tap_name, String tap_atc, String tap_cost) {
        SQLiteDatabase db = getWritableDatabase();
        float tap_at = Float.parseFloat(tap_atc);
        float tap_cos = Float.parseFloat(tap_cost);
        db.execSQL("insert into " + table_name + " values ('" + tap_name + "','" + tap_at + "','" + tap_cos + "');");
    }

    void delete(String table_name, String tap_name, String tap_atc) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name + " WHERE " + TAP_NAME + " = '" + tap_name + "' AND " + TAP_ATTACK + " = '" + tap_atc + "';");
    }
}
