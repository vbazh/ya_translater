package com.vbazh.translater_yandex.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.R;
import com.vbazh.translater_yandex.adapters.TranslationsAdapter;
import com.vbazh.translater_yandex.model.Translation;
import com.vbazh.translater_yandex.utils.DatabaseHelper;

import java.util.List;

/**
 * избранное
 */

public class FavoritesFragment extends Fragment {

    private TranslationsAdapter translationsAdapter;
    private List<Translation> translations;
    private LinearLayout noTranslationLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        noTranslationLayout = (LinearLayout) view.findViewById(R.id.layout_no_translations);
        RecyclerView translationsRecycler = (RecyclerView) view.findViewById(R.id.recycler_translations_favorites);
        translations = new DatabaseHelper(getContext()).getFavorites();

        showEmptyLogo();

        translationsAdapter = new TranslationsAdapter(view.getContext(), translations, Constants.FAVORITES_FRAGMENT_NAME);
        translationsRecycler.setAdapter(translationsAdapter);
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

    public void showDeleteDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.text_delete)
                .setMessage(R.string.text_clear_favorites)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new DatabaseHelper(getContext()).removeAllFavorites();
                        translations.clear();
                        translationsAdapter.notifyDataSetChanged();
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
