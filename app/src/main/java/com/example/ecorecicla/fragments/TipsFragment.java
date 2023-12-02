package com.example.ecorecicla.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecorecicla.R;
import com.example.ecorecicla.adapters.TipGridAdapter;
import com.example.ecorecicla.models.TipList;
import com.example.ecorecicla.models.Tip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class TipsFragment extends Fragment {

    private TextView tvDays;
    private LinearProgressIndicator piDaysOff;
    private List<Tip> tipList;
    private RecyclerView rvTips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tips, container, false);

        // Inicializar las vistas
        piDaysOff = rootView.findViewById(R.id.piDaysOff);
        tvDays = rootView.findViewById(R.id.tvDays);
        rvTips = rootView.findViewById(R.id.rvTips);

        // Configurar la barra de progreso
        setupProgressBar();
        // Configurar y cargar la lista de consejos
        setupTips();

        return rootView;
    }

    private void setupProgressBar() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5:00"));
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int daysElapsed = currentDayOfWeek - 1;
        int progressFinal = (daysElapsed * 100) / 7;

        if (currentDayOfWeek == Calendar.SUNDAY) {
            tvDays.setText("Último día");
            progressFinal = 100;
        } else {
            tvDays.setText(String.valueOf(7 - daysElapsed));
        }

        piDaysOff.setProgressCompat(progressFinal, true);
        startProgressBarAnimation(progressFinal);
    }

    private void startProgressBarAnimation(int progressFinal) {
        ObjectAnimator animation = ObjectAnimator.ofInt(piDaysOff, "progress", 0, progressFinal);
        animation.setDuration(2500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    private void setupTips() {
        // Cargar la lista de consejos desde el archivo JSON
        loadTipsFromJSON();
        // Configurar y mostrar la lista de consejos en el RecyclerView
        setupRecyclerViewTips();
    }

    private void loadTipsFromJSON() {
        try {
            InputStream inputStream = requireActivity().getAssets().open("tips.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Utilizar Gson para convertir el JSON en objetos Java
            Gson gson = new Gson();
            TipList tipListData = gson.fromJson(json, TipList.class);

            // Verificar si la lista de consejos es válida y asignarla
            tipList = (tipListData != null) ? tipListData.getTips() : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            tipList = new ArrayList<>();
        }
    }

    private void setupRecyclerViewTips() {
        if (tipList != null && !tipList.isEmpty()) {
            // Configurar el RecyclerView con un diseño de cuadrícula
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            rvTips.setLayoutManager(gridLayoutManager);

            // Crear un adaptador para la lista de consejos y asignarlo al RecyclerView
            TipGridAdapter adapter = new TipGridAdapter(tipList, getContext());
            rvTips.setAdapter(adapter);
        } else {
            // Mostrar un mensaje si la lista de consejos está vacía
            Toast.makeText(getContext(), "Lista de consejos no disponible", Toast.LENGTH_LONG).show();
        }
    }
}