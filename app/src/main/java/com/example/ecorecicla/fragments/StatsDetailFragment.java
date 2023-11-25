package com.example.ecorecicla.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecorecicla.R;
import com.example.ecorecicla.models.BatteryItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class StatsDetailFragment extends Fragment {

    private ChipGroup chipContainer;
    public static StatsDetailFragment newInstance(BatteryItem batteryItem) {

        StatsDetailFragment fragment = new StatsDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("batteryItem", batteryItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats_detail, container, false);

        chipContainer = rootView.findViewById(R.id.chipContainer);


        BatteryItem batteryItem = getArguments().getParcelable("batteryItem");

        // Verificar si batteryItem no es nulo antes de acceder a getSubcategory()
        if (batteryItem != null) {
            String subCategory = batteryItem.getSubCategory();
            Set<String> uniqueSubcategories = new HashSet<>();

            if (subCategory != null && !subCategory.isEmpty() && uniqueSubcategories.add(batteryItem.getSubCategory())) {
                Chip chip = new Chip(getContext());
                chip.setText(batteryItem.getSubCategory());
                chipContainer.addView(chip);
            } else {
                // No hay subcategorías, realiza otras acciones o muestra un mensaje, si es necesario

                // Otros ajustes según tus necesidades
            }
        } else {
            // Manejar el caso en que batteryItem sea nulo
            Log.e("StatsDetailFragment", "BatteryItem es nulo");
        }

        return rootView;
    }
}