package com.vbazh.translater_yandex.utils;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.model.Language;
import com.vbazh.translater_yandex.model.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * делает запросы к серверу и возвращает обычные java объекты для работы
 */

public class JSONPojoGetter {

    //метод возвращающий перевод
    public Translation getTranslation(String sourceLang, String targetlang, String text) {

        Translation translation = new Translation();
        translation.setSourceLang(sourceLang);
        translation.setTargetLang(targetlang);
        translation.setSourceText(text);
        String textEncoded = null;

        String lang;
        if (sourceLang != null) {
            lang = sourceLang + "-" + targetlang;
        } else lang = targetlang;

        try {
            textEncoded = URLEncoder.encode(text, Constants.TRANSLATE_CODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONParser().getJSONFromUrl(
                Constants.YANDEX_TRANSLATE_API +
                        "?key=" + Constants.YANDEX_KEY_API+
                        "&text=" + textEncoded +
                        "&lang=" + lang +
                        "&format=" + Constants.TRANSLATE_FORMAT_PLAIN +
                        "&options=" + Constants.TRANSLATE_OPTIONS_SIMPLE,
                null);

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.JSON_TEXT_ARRAY);
            translation.setTranslatedText(jsonArray.optString(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return translation;
    }

    //метод возвращающий список языков
    public List<Language> getLanguages() {

        List<Language> languages = new ArrayList<>();

        JSONObject jsonObject = new JSONParser().getJSONFromUrl(
                Constants.YANDEX_GET_LANGS_API +
                        "?key=" + Constants.YANDEX_KEY_API +
                        "&ui=" + Constants.TRANSLATE_LANG_MAIN_RU,
                null);
        try {
            jsonObject = jsonObject.getJSONObject(Constants.JSON_LANGS_ARRAY);
            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = iter.next();
                languages.add(new Language(0, key, jsonObject.getString(key)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return languages;
    }
}
