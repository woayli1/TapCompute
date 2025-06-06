package com.gc.tapCompute.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by gc on 2017/6/3.
 */

public class DataHelper extends SQLiteOpenHelper {

    public static final String DATA_BASE_NAME = "TapCompute";

    public static final String TABLE_NAME_MAN = "tap_man";
    public static final String TABLE_NAME_WOMAN = "tap_woman";

    public static final String TAP_NAME = "tap_name";
    public static final String TAP_ATTACK = "tap_attack";
    public static final String TAP_COST = "tap_cost";

    public DataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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

    public void insertInto(String table_name, String tap_name, String tap_atc, String tap_cost) {
        SQLiteDatabase db = getWritableDatabase();
        float tap_at = Float.parseFloat(tap_atc);
        float tap_cos = Float.parseFloat(tap_cost);
        db.execSQL("insert into " + table_name + " values ('" + tap_name + "','" + tap_at + "','" + tap_cos + "');");
        db.close();
    }

    public boolean isExist(String table_name, String tap_name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '" + table_name + "' where tap_name = '" + tap_name + "';", null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exist;
    }

    public void delete(String table_name, String tap_name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name + " WHERE " + TAP_NAME + " = '" + tap_name + "';");
        db.close();
    }
}
