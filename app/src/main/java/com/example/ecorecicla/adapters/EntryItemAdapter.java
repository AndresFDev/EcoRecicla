package com.example.ecorecicla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecorecicla.models.EntryItem;

import java.util.List;

public class EntryItemAdapter extends ArrayAdapter<EntryItem> {
    public EntryItemAdapter(Context context, List<EntryItem> entryItems) {
        super(context, 0, entryItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EntryItem entryItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        if (entryItem != null) {
        }

        return convertView;
    }
}

