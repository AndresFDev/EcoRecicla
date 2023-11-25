package com.example.ecorecicla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecorecicla.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    public CustomArrayAdapter(Context context, List<String> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_item, parent, false);
        }

        MaterialTextView textView = convertView.findViewById(R.id.customTextView);
        textView.setText(getItem(position));

        return convertView;
    }
}