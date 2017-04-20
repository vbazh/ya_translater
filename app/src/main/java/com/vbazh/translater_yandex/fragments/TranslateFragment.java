package com.vbazh.translater_yandex.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.R;
import com.vbazh.translater_yandex.adapters.SpinnerAdapter;
import com.vbazh.translater_yandex.model.Language;
import com.vbazh.translater_yandex.model.Translation;
import com.vbazh.translater_yandex.utils.DatabaseHelper;
import com.vbazh.translater_yandex.utils.GetLangsTask;
import com.vbazh.translater_yandex.utils.InternetConnection;
import com.vbazh.translater_yandex.utils.JSONPojoGetter;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * перевод
 */

public class TranslateFragment extends Fragment {

    private Translation translation;
    private TextView translatedText;
    private EditText sourceText;
    private Button clearButton;
    private Spinner sourceLanguage;
    private Spinner targetLanguage;
    private Context mContext;
    private SharedPreferences settingsPreferences;
    private ToggleButton addToFavorites;
    private LinearLayout internetAvailableDialog;
    private Handler handler;

    public TranslateFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        mContext = getContext();
        init(view);
        return view;
    }

    void init(View view) {
        ImageButton swapButton = (ImageButton) view.findViewById(R.id.button_swap);
        clearButton = (Button) view.findViewById(R.id.button_clear);
        translatedText = (TextView) view.findViewById(R.id.text_view_translated_text);
        sourceText = (EditText) view.findViewById(R.id.editText_original);
        sourceLanguage = (Spinner) view.findViewById(R.id.spinner_source_language);
        targetLanguage = (Spinner) view.findViewById(R.id.spinner_target_language);
        addToFavorites = (ToggleButton) view.findViewById(R.id.button_add_to_favorites);
        internetAvailableDialog = (LinearLayout) view.findViewById(R.id.view_internet_available);
        Button retryButton = (Button) view.findViewById(R.id.button_retry);

        handler = new Handler();

        // слушатель кнопки повторной попытки перевода или загрузки языков, появляется в случае проблем с соединением
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.languages != null) {
                    translate();
                } else if (InternetConnection.INTERNET_CONNECTION) {
                    loadNewLangs();
                    internetAvailableDialog.setVisibility(View.GONE);
                } else {
                    internetAvailableDialog.setVisibility(View.VISIBLE);
                }
            }
        });

        // слушатель кнопки добавления перевода в избранное
        addToFavorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (translation != null) {
                    if (isChecked) {
                        translation.setFavorite(true);
                        new DatabaseHelper(mContext).addToFavorites(translation);
                    } else {
                        new DatabaseHelper(mContext).removeFromFavorites(translation);
                    }
                }
            }
        });

        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapLanguage();
            }
        });

        // слушатель кнопки очистки окна ввода
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceText.setText("");
                translatedText.setText("");
                buttonsActive();
            }
        });

        //вызываем метод выполняющий первоначальные настройки приложения(загрузка языков, предыдущие направления перевода и тд)
        settingsApp();

        // слушатель изменения текста ввода
        sourceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addToFavorites.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                wasChanged();
                buttonsActive();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        buttonsActive();
        loadPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        settingsPreferences.edit().putString(Constants.SHARED_SOURCE_TEXT, sourceText.getText().toString()).apply();
        settingsPreferences.edit().putString(Constants.SHARED_TRANSLATED_TEXT, translatedText.getText().toString()).apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //метод необходим для того чтобы не было частых запросов, убирается старый коллбек и добавляется новый,
    private void wasChanged() {
        handler.removeCallbacks(translated);
        handler.postDelayed(translated, 2500);
    }

    private void translate() {
        if (InternetConnection.INTERNET_CONNECTION) {
            internetAvailableDialog.setVisibility(View.GONE);

            if (Constants.languages != null) {
                String sourceLang = Constants.languages.get(sourceLanguage.getSelectedItemPosition()).getCode();
                String targetLang = Constants.languages.get(targetLanguage.getSelectedItemPosition()).getCode();

                if ((sourceText.getText().length() != 0)) {
                    new TranslateTask().execute(sourceLang, targetLang, sourceText.getText().toString(), Constants.TRANSLATE_CODE);
                }

            } else {
                settingsApp();
            }

        } else {
            internetAvailableDialog.setVisibility(View.VISIBLE);
        }
    }


    // метод проверяющий активность кнопок удаления или добавления в избранное
    private void buttonsActive() {
        if (sourceText.length() != 0) {
            clearButton.setVisibility(View.VISIBLE);
            addToFavorites.setVisibility(View.VISIBLE);
        } else {
            clearButton.setVisibility(View.INVISIBLE);
            addToFavorites.setVisibility(View.INVISIBLE);
        }
    }

    // метод меняющий направления перевода
    private void swapLanguage() {

        //анимация перемещения спиннеров с языками
        leftAnim(targetLanguage);
        rightAnim(sourceLanguage);

        int sourceLanguagePosition = sourceLanguage.getSelectedItemPosition();
        int targetLanguagePosition = targetLanguage.getSelectedItemPosition();

        sourceLanguage.setSelection(targetLanguagePosition);
        targetLanguage.setSelection(sourceLanguagePosition);
        sourceText.setText(translatedText.getText());
        translate();
    }

    // метод устанавливающий адаптеры для спиннеров с доступными языками
    private void setAdapters(List<Language> langs) {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(mContext, langs);
        if (sourceLanguage.getAdapter() == null) {
            sourceLanguage.setAdapter(spinnerAdapter);
            sourceLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    settingsPreferences.edit().putInt(Constants.SHARED_SOURCE_LANG_POSITION, position).apply();
                    translate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        if (targetLanguage.getAdapter() == null) {
            targetLanguage.setAdapter(spinnerAdapter);
            targetLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    settingsPreferences.edit().putInt(Constants.SHARED_TARGET_LANG_POSITION, position).apply();
                    translate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    // первоначальные настройки
    private void settingsApp() {
        settingsPreferences = mContext.getSharedPreferences(Constants.SHARED_SETTINGS_PREFERENCES, MODE_PRIVATE);

        if (Constants.languages == null && InternetConnection.INTERNET_CONNECTION) {
            loadNewLangs();
        } else if (Constants.languages != null) {
            setAdapters(Constants.languages);
        }
    }

    // загрузка языков и установка адаптеров
    private void loadNewLangs() {

        new GetLangsTask(new GetLangsTask.AsyncResponse() {
            @Override
            public void processFinish(List<Language> output) {
                Constants.languages = output;
                //new DatabaseHelper(mContext).updateLanguages(output);
                setAdapters(Constants.languages);
                sourceLanguage.setSelection(settingsPreferences.getInt(Constants.SHARED_SOURCE_LANG_POSITION, 0));
                targetLanguage.setSelection(settingsPreferences.getInt(Constants.SHARED_TARGET_LANG_POSITION, 0));
            }
        }).execute();
    }

    // асинхронный запрос перевода
    private class TranslateTask extends AsyncTask<String, Integer, Translation> {

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Translation translation) {
            if (translation != null) {
                translatedText.setText(translation.getTranslatedText());
                settingsPreferences.edit().putString(Constants.SHARED_SOURCE_TEXT, translation.getSourceText()).apply();
                settingsPreferences.edit().putString(Constants.SHARED_TRANSLATED_TEXT, translation.getTranslatedText()).apply();
            } else internetAvailableDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Translation doInBackground(String... params) {
            if (InternetConnection.INTERNET_CONNECTION) {
                translation = null;
                try {
                    translation = new JSONPojoGetter().getTranslation(params[0], params[1], params[2]);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }

                if (!params[2].equals(settingsPreferences.getString(Constants.SHARED_SOURCE_TEXT, ""))) {
                    new DatabaseHelper(mContext).addToHistory(translation);
                }
            }
            return translation;
        }
    }

    void loadPreferences() {
        if (settingsPreferences != null) {
            sourceLanguage.setSelection(settingsPreferences.getInt(Constants.SHARED_SOURCE_LANG_POSITION, 0));
            targetLanguage.setSelection(settingsPreferences.getInt(Constants.SHARED_TARGET_LANG_POSITION, 0));
            sourceText.setText(settingsPreferences.getString(Constants.SHARED_SOURCE_TEXT, ""));
            translatedText.setText(settingsPreferences.getString(Constants.SHARED_TRANSLATED_TEXT, ""));
        }
    }

    public void leftAnim(View view) {
        Animation leftMoveAnim = AnimationUtils.loadAnimation(mContext, R.anim.left_move_view);
        view.startAnimation(leftMoveAnim);
    }

    public void rightAnim(View view) {
        Animation rightMoveAnim = AnimationUtils.loadAnimation(mContext, R.anim.right_move_view);
        view.startAnimation(rightMoveAnim);
    }

    Runnable translated = new Runnable() {
        @Override
        public void run() {
            translate();
        }
    };
}
