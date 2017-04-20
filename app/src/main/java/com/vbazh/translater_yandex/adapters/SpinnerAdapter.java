package com.vbazh.translater_yandex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vbazh.translater_yandex.R;
import com.vbazh.translater_yandex.model.Language;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private List<Language>languages;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<Language>languages) {
        this.languages = languages;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_language, null);
        TextView name = (TextView) view.findViewById(R.id.text_view_name);
        name.setText(languages.get(i).getName());
        return view;
    }
}