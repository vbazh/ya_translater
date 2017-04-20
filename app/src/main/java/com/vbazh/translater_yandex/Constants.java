package com.vbazh.translater_yandex;

import com.vbazh.translater_yandex.model.Language;

import java.util.List;

/**
 * класс с константами
 */

public class Constants {

    public static List<Language> languages;

    public final static String HISTORY_FRAGMENT_NAME = "History";
    public final static String FAVORITES_FRAGMENT_NAME = "Favorites";
    public final static String TRANSLATE_FRAGMENT_NAME = "Translate";

    public final static String SHARED_SETTINGS_PREFERENCES = "SettingsApp";
    public final static String SHARED_SOURCE_LANG_POSITION = "sourceLangPosition";
    public final static String SHARED_TARGET_LANG_POSITION = "targetLangPosition";
    public final static String SHARED_SOURCE_TEXT = "sourceText";
    public final static String SHARED_TRANSLATED_TEXT = "translatedText";

    public final static String YANDEX_TRANSLATE_API = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    public final static String YANDEX_GET_LANGS_API = "https://translate.yandex.net/api/v1.5/tr.json/getLangs";
    public final static String YANDEX_KEY_API = "trnsl.1.1.20170403T094720Z.8e78fe6e2bd0dd54.0820d776c99b9378c1577057e2354f8dbd012448";
    public final static String TRANSLATE_FORMAT_PLAIN = "plain";
    public final static String TRANSLATE_OPTIONS_SIMPLE = "1";
    public final static String TRANSLATE_LANG_MAIN_RU = "ru";
    public final static String TRANSLATE_CODE = "UTF-8";

    public final static String JSON_LANGS_ARRAY = "langs";
    public final static String JSON_TEXT_ARRAY = "text";

    public final static String DATABASE_NAME = "TranslaterDatabase";
    public final static String DATABASE_TABLE_HISTORY = "history";
    public final static String DATABASE_TABLE_FAVORITES = "favorites";
    public final static String DATABASE_COLUMD_ID = "id";
    public final static String DATABASE_COLUMN_SOURCE_LANG_CODE = "source_lang_code";
    public final static String DATABASE_COLUMN_TARGET_LANG_CODE = "target_lang_code";
    public final static String DATABASE_COLUMN_SOURCE_TEXT = "source_text";
    public final static String DATABASE_COLUMN_TARGET_TEXT = "target_text";
    public final static String DATABASE_COLUMN_IN_FAVORITES = "in_favorites";





}
