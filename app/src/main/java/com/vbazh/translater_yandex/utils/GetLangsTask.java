package com.vbazh.translater_yandex.utils;

import android.os.AsyncTask;

import com.vbazh.translater_yandex.model.Language;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * получение списка доступных языков
 *
 */
public class GetLangsTask extends AsyncTask<Void, Integer, List<Language>> {


    private AsyncResponse response = null;

    //интерфейс нужен, чтобы получить данные в нужном месте
    public interface AsyncResponse {
        void processFinish(List<Language> output);
    }

    public GetLangsTask(AsyncResponse response) {
        this.response = response;

    }

    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected List<Language> doInBackground(Void... params) {
        return new JSONPojoGetter().getLanguages();
    }

    protected void onPostExecute(List<Language> languages) {

        //перед возвращением сортируем в алфавитном порядке
        Collections.sort(languages, new Comparator<Language>() {
            @Override
            public int compare(Language lang1, Language lang2) {
                return lang1.getName().compareToIgnoreCase(lang2.getName());
            }
        });
        response.processFinish(languages);
    }
}