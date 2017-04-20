package com.vbazh.translater_yandex.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.R;
import com.vbazh.translater_yandex.model.Translation;
import com.vbazh.translater_yandex.utils.DatabaseHelper;
import com.vbazh.translater_yandex.utils.OpenTranslateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 *
 */
public class TranslationsAdapter extends RecyclerView.Adapter<TranslationsAdapter.ViewHolder> {

    private Context mContext;
    private List<Translation> translations;
    private String nameFragment;

    public TranslationsAdapter(Context context, List<Translation> translations, String nameFragment) {
        this.mContext = context;
        this.translations = translations;
        this.nameFragment = nameFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sourceText;
        TextView targetText;
        ToggleButton addToFavorites;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceText = (TextView) itemView.findViewById(R.id.text_view_source);
            targetText = (TextView) itemView.findViewById(R.id.text_view_target);
            addToFavorites = (ToggleButton) itemView.findViewById(R.id.toggle_button_favorites);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_translation, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.targetText.setText(translations.get(position).getTranslatedText());
        holder.sourceText.setText(translations.get(position).getSourceText());
        holder.addToFavorites.setChecked(translations.get(position).getFavorite());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OpenTranslateEvent(translations.get(position)));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(position);
                return false;
            }
        });

        holder.addToFavorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    translations.get(position).setFavorite(true);
                    new DatabaseHelper(mContext).addToFavorites(translations.get(position));
                } else {
                    new DatabaseHelper(mContext).removeFromFavorites(translations.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public void showDeleteDialog(final int id) {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.text_delete)
                .setMessage(R.string.text_delete_translate)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (nameFragment.equals(Constants.HISTORY_FRAGMENT_NAME)) {
                            new DatabaseHelper(mContext).removeFromHistory(translations.get(id));
                        } else
                            new DatabaseHelper(mContext).removeFromFavorites(translations.get(id));
                        translations.remove(id);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.ic_delete)
                .show();
    }
}

