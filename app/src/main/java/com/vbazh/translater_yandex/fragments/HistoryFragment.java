package com.vbazh.translater_yandex.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.R;
import com.vbazh.translater_yandex.adapters.TranslationsAdapter;
import com.vbazh.translater_yandex.model.Language;
import com.vbazh.translater_yandex.model.Translation;
import com.vbazh.translater_yandex.utils.DatabaseHelper;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * история
 */

public class HistoryFragment extends Fragment {

    private TranslationsAdapter translationAdapter;
    private List<Translation>translations;
    private LinearLayout noTranslationLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        noTranslationLayout = (LinearLayout) view.findViewById(R.id.layout_no_translations);
        final RecyclerView translationsRecycler = (RecyclerView) view.findViewById(R.id.recycler_translations_history);
        translations =  new DatabaseHelper(getContext()).getHistory();
        showEmptyLogo();
        translationAdapter = new TranslationsAdapter(getContext(), translations, Constants.HISTORY_FRAGMENT_NAME);
        translationsRecycler.setAdapter(translationAdapter);
        translationsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.button_delete_translations);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        return view;
    }

    public void showDeleteDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.text_delete)
                .setMessage(R.string.text_clear_history)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new DatabaseHelper(getContext()).removeAllHistory();
                        translations.clear();
                        translationAdapter.notifyDataSetChanged();
                        showEmptyLogo();
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_delete)
                .show();
    }

    //картинка вместо пустого списка
    public void showEmptyLogo() {
        if (translations.isEmpty()) {
            noTranslationLayout.setVisibility(View.VISIBLE);
        } else noTranslationLayout.setVisibility(View.GONE);
    }




}
