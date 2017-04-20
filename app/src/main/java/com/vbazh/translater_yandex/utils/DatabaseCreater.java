package com.vbazh.translater_yandex.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vbazh.translater_yandex.Constants;

/**
 * Создание базы данных
 *
 * таблица history история всех переводов
 *
 * таблица favorites избранные переводы
 *
 */

public class DatabaseCreater extends SQLiteOpenHelper {

    public DatabaseCreater(Context context) {
        super(context, Constants.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        Log.d("LOGS", "create table with all languages");
//        db.execSQL("create table languages ("
//                +"id integer primary key autoincrement,"
//                +"code_lang text,"
//                +"name_lang text"+");");

        Log.d("LOGS", "create table with history");
        db.execSQL("create table history ("
                +"id integer primary key autoincrement,"
                +"source_lang_code text,"
                +"target_lang_code text,"
                +"source_text text,"
                +"target_text text,"
                +"in_favorites integer"+");");

        Log.d("LOGS", "create table with favorites");
        db.execSQL("create table favorites ("
                +"id integer primary key autoincrement,"
                +"source_lang_code text,"
                +"target_lang_code text,"
                +"source_text text,"
                +"target_text text,"
                +"in_favorites integer"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
