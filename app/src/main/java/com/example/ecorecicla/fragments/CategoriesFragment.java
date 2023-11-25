package com.example.ecorecicla.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecorecicla.activities.EntryCategoryActivity;
import com.example.ecorecicla.R;
import com.google.android.material.button.MaterialButton;

public class CategoriesFragment extends Fragment {

    private MaterialButton btnPlastic, btnPaper, btnElectronics, btnGlass, btnCard,
            btnSteel, btnTexiles, btnBatteries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        btnPlastic = rootView.findViewById(R.id.btnPlastic);
        btnPaper = rootView.findViewById(R.id.btnPaper);
        btnElectronics = rootView.findViewById(R.id.btnElectronics);
        btnGlass = rootView.findViewById(R.id.btnGlass);
        btnCard = rootView.findViewById(R.id.btnCard);
        btnSteel = rootView.findViewById(R.id.btnSteel);
        btnTexiles = rootView.findViewById(R.id.btnTexiles);
        btnBatteries = rootView.findViewById(R.id.btnBatteries);

        managerButtons();

        return rootView;
    }

    public void managerButtons() {

        btnPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.plastic));
                startActivity(intent);
            }
        });

        btnPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.paper));
                startActivity(intent);
            }
        });

        btnElectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.electronic));
                startActivity(intent);
            }
        });

        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.glass));
                startActivity(intent);
            }
        });

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.cardboard));
                startActivity(intent);
            }
        });

        btnSteel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.steel));
                startActivity(intent);
            }
        });

        btnTexiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.textiles));
                startActivity(intent);
            }
        });

        btnBatteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.batteries));
                startActivity(intent);
            }
        });

    }

}