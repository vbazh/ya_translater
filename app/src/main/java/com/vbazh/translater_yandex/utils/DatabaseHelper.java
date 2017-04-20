package com.vbazh.translater_yandex.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.model.Translation;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализуйющий доступ к данным из базы
 */

public class DatabaseHelper {

    private SQLiteDatabase mDatabase;
    private DatabaseCreater databaseCreater;

    public DatabaseHelper(Context context) {
        databaseCreater = new DatabaseCreater(context);
    }

//    public List<Language> getLanguagesList() {
//        Log.d("TAGS", "getLanguages");
//        List<Language> languages = new ArrayList<>();
//        mDatabase = databaseCreater.getWritableDatabase();
//        Cursor cursor = mDatabase.query("languages", null, null, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            int id = cursor.getColumnIndex("id");
//            int codeLang = cursor.getColumnIndex("code_lang");
//            int nameLang = cursor.getColumnIndex("name_lang");
//            do {
//                languages.add(new Language(cursor.getInt(id), cursor.getString(codeLang), cursor.getString(nameLang)));
//            } while (cursor.moveToNext());
//        } else cursor.close();
//        mDatabase.close();
//        return languages;
//    }
//
//    public void updateLanguages(List<Language> languages) {
//        Log.d("TAGS", "updateLanguages");
//
//        Log.d("updateLanguages langs=", "" + languages.size());
//        for (Language language : languages) {
//            mDatabase = databaseCreater.getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("code_lang", language.getCode());
//            contentValues.put("name_lang", language.getName());
//            mDatabase.insert("languages", null, contentValues);
//        }
//        mDatabase.close();
//
//    }

    public List<Translation> getHistory() {

        List<Translation> translations = new ArrayList<>();
        mDatabase = databaseCreater.getWritableDatabase();
        Cursor cursor = mDatabase.query(Constants.DATABASE_TABLE_HISTORY, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(Constants.DATABASE_COLUMD_ID);
            int sourceCodeLang = cursor.getColumnIndex(Constants.DATABASE_COLUMN_SOURCE_LANG_CODE);
            int targetCodeLang = cursor.getColumnIndex(Constants.DATABASE_COLUMN_TARGET_LANG_CODE);
            int sourceText = cursor.getColumnIndex(Constants.DATABASE_COLUMN_SOURCE_TEXT);
            int targetText = cursor.getColumnIndex(Constants.DATABASE_COLUMN_TARGET_TEXT);
            int inFavorites = cursor.getColumnIndex(Constants.DATABASE_COLUMN_IN_FAVORITES);
            do {
                boolean favorite = (cursor.getInt(inFavorites) != 0);
                translations.add(new Translation(cursor.getInt(id), cursor.getString(sourceCodeLang),
                        cursor.getString(targetCodeLang), cursor.getString(sourceText), cursor.getString(targetText),
                        favorite));
            } while (cursor.moveToNext());
        } else cursor.close();
        mDatabase.close();

        return translations;
    }

    public List<Translation> getFavorites() {

        List<Translation> translations = new ArrayList<>();
        mDatabase = databaseCreater.getWritableDatabase();
        Cursor cursor = mDatabase.query(Constants.DATABASE_TABLE_FAVORITES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(Constants.DATABASE_COLUMD_ID);
            int sourceCodeLang = cursor.getColumnIndex(Constants.DATABASE_COLUMN_SOURCE_LANG_CODE);
            int targetCodeLang = cursor.getColumnIndex(Constants.DATABASE_COLUMN_TARGET_LANG_CODE);
            int sourceText = cursor.getColumnIndex(Constants.DATABASE_COLUMN_SOURCE_TEXT);
            int targetText = cursor.getColumnIndex(Constants.DATABASE_COLUMN_TARGET_TEXT);
            int inFavorites = cursor.getColumnIndex(Constants.DATABASE_COLUMN_IN_FAVORITES);
            do {
                boolean favorite = (cursor.getInt(inFavorites) != 0);
                translations.add(new Translation(cursor.getInt(id), cursor.getString(sourceCodeLang),
                        cursor.getString(targetCodeLang), cursor.getString(sourceText), cursor.getString(targetText),
                        favorite));
            } while (cursor.moveToNext());
        } else cursor.close();
        mDatabase.close();
        return translations;
    }

    public void addToHistory(Translation translation) {
        if (translation != null) {
            mDatabase = databaseCreater.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.DATABASE_COLUMN_SOURCE_LANG_CODE, translation.getSourceLang());
            contentValues.put(Constants.DATABASE_COLUMN_TARGET_LANG_CODE, translation.getTargetLang());
            contentValues.put(Constants.DATABASE_COLUMN_SOURCE_TEXT, translation.getSourceText());
            contentValues.put(Constants.DATABASE_COLUMN_TARGET_TEXT, translation.getTranslatedText());
            contentValues.put(Constants.DATABASE_COLUMN_IN_FAVORITES, translation.getFavorite());
            mDatabase.insert(Constants.DATABASE_TABLE_HISTORY, null, contentValues);
            mDatabase.close();
        }
    }

    public void addToFavorites(Translation translation) {
        mDatabase = databaseCreater.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATABASE_COLUMD_ID, translation.getId());
        contentValues.put(Constants.DATABASE_COLUMN_SOURCE_LANG_CODE, translation.getSourceLang());
        contentValues.put(Constants.DATABASE_COLUMN_TARGET_LANG_CODE, translation.getTargetLang());
        contentValues.put(Constants.DATABASE_COLUMN_SOURCE_TEXT, translation.getSourceText());
        contentValues.put(Constants.DATABASE_COLUMN_TARGET_TEXT, translation.getTranslatedText());
        contentValues.put(Constants.DATABASE_COLUMN_IN_FAVORITES, translation.getFavorite());
        mDatabase.insert(Constants.DATABASE_TABLE_FAVORITES, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Constants.DATABASE_COLUMN_IN_FAVORITES, translation.getFavorite());
        mDatabase.update(Constants.DATABASE_TABLE_HISTORY, contentValues2, "id=?",
                new String[]{"" + translation.getId()});
        mDatabase.close();
    }

    public void removeFromHistory(Translation translation) {
        mDatabase = databaseCreater.getWritableDatabase();
        mDatabase.delete(Constants.DATABASE_TABLE_HISTORY, "id = " + translation.getId(), null);
        mDatabase.close();
    }

    public void removeFromFavorites(Translation translation) {
        mDatabase = databaseCreater.getWritableDatabase();
        mDatabase.delete(Constants.DATABASE_TABLE_FAVORITES, "id = " + translation.getId(), null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATABASE_COLUMN_IN_FAVORITES, 0);
        mDatabase.update(Constants.DATABASE_TABLE_HISTORY, contentValues, "id=?",
                new String[]{"" + translation.getId()});
        mDatabase.close();
    }

    public void removeAllHistory() {
        mDatabase = databaseCreater.getReadableDatabase();
        mDatabase.delete(Constants.DATABASE_TABLE_HISTORY, null, null);
        mDatabase.close();
    }

    public void removeAllFavorites() {
        mDatabase = databaseCreater.getReadableDatabase();
        mDatabase.delete(Constants.DATABASE_TABLE_FAVORITES, null, null);
        mDatabase.close();
    }

}
