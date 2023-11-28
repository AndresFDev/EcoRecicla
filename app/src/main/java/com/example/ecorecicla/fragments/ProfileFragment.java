package com.example.ecorecicla.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ecorecicla.EntryData;
import com.example.ecorecicla.R;
import com.example.ecorecicla.SessionManager;
import com.example.ecorecicla.UserData;
import com.example.ecorecicla.models.BatteryItem;
import com.example.ecorecicla.models.Category;
import com.example.ecorecicla.models.PlasticItem;
import com.example.ecorecicla.models.SteelItem;
import com.example.ecorecicla.models.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private LinearLayoutCompat llData;
    private ImageView ivUserImageProfile;
    private MaterialTextView tvUserName;
    private PieChart pieChart;
    private MaterialButton btnEditProfile;
    private int idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        llData = rootView.findViewById(R.id.llData);
        ivUserImageProfile = rootView.findViewById(R.id.ivUserImageProfile);
        tvUserName = rootView.findViewById(R.id.tvUserName);
        pieChart = rootView.findViewById(R.id.pieChart);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToEditProfile();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
        boolean hasData = graphicTime();

        if (!hasData) {
            llData.setVisibility(View.GONE);
        }
    }

    // Cargar datos del usuario actual
    private void loadUserData() {
        SessionManager sessionManager = new SessionManager(getContext());
        UserData userData = new UserData(getContext(), sessionManager);
        User user = userData.getCurrentUser();
        idUser = user.getId();
        tvUserName.setText(user.getUserName());

        if (user.getUserImage() != null) {
            loadUserImage(user.getUserImage(), ivUserImageProfile);
        }
    }

    // Cargar la imagen del usuario utilizando Glide
    private void loadUserImage(String imageUrl, ImageView imageView) {
        if (!imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_close)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_person);
        }
    }

    // Crear y mostrar el gráfico circular (Pie Chart)
    private boolean graphicTime() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        EntryData entryData = new EntryData(getContext());
        com.example.ecorecicla.models.Entry entry = entryData.getEntry();

        Map<String, List<PlasticItem>> plasticMap = entry.getPlastic();
        Map<String, List<SteelItem>> steelMap = entry.getSteel();
        Map<String, List<BatteryItem>> batteryMap = entry.getBattery();

        List<Category> paperList = entry.getPaperList();
        List<Category> electronicList = entry.getElectronicList();
        List<Category> glassList = entry.getGlassList();
        List<Category> cardboardList = entry.getCardboardList();
        List<Category> textilesList = entry.getTextilesList();

        if (plasticMap != null) {
            entries.addAll(createEntriesFromPlasticMap(plasticMap, "Plástico", idUser));
        }

        if (paperList != null) {
            entries.addAll(createEntriesFromList(paperList, "Papel", idUser));
        }

        if (electronicList != null) {
            entries.addAll(createEntriesFromList(electronicList, "Electronicos", idUser));
        }

        if (glassList != null) {
            entries.addAll(createEntriesFromList(glassList, "Vidrio", idUser));
        }

        if (cardboardList != null) {
            entries.addAll(createEntriesFromList(cardboardList, "Cartón", idUser));
        }

        if (steelMap != null) {
            entries.addAll(createEntriesFromSteelMap(steelMap, "Metal", idUser));
        }

        if (textilesList != null) {
            entries.addAll(createEntriesFromList(textilesList, "Textiles", idUser));
        }

        if (batteryMap != null) {
            entries.addAll(createEntriesFromBatteryMap(batteryMap, "Baterías", idUser));
        }

        PieDataSet dataSet = new PieDataSet(entries, " ");
        dataSet.setColors(getColors());
        dataSet.setValueTextSize(16f);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(pieData);

        // Configuración del aspecto del gráfico
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextColor(Color.WHITE);
        legend.setDrawInside(false);
        legend.setEnabled(true);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.setDrawEntryLabels(true);
                pieChart.invalidate();
            }

            @Override
            public void onNothingSelected() {
                pieChart.setDrawEntryLabels(false);
                pieChart.invalidate();
            }
        });

        pieChart.invalidate();

        boolean hasData = entries.size() > 0;

        pieChart.invalidate();

        return hasData;
    }

    // Crear entradas para categorías de plástico en el gráfico
    private List<PieEntry> createEntriesFromPlasticMap(Map<String, List<PlasticItem>> itemMap, String categoryName, int idUser) {
        List<PieEntry> entries = new ArrayList<>();

        double totalQuantity = 0;
        for (List<PlasticItem> itemList : itemMap.values()) {
            totalQuantity += calculateTotalQuantity(itemList, idUser);
        }

        if(totalQuantity > 0) {
            entries.add(new PieEntry((float) totalQuantity, categoryName));
        }

        return entries;
    }

    // Crear entradas para categorías de acero en el gráfico
    private List<PieEntry> createEntriesFromSteelMap(Map<String, List<SteelItem>> itemMap, String categoryName, int idUser) {
        List<PieEntry> entries = new ArrayList<>();

        double totalQuantity = 0;
        for (List<SteelItem> itemList : itemMap.values()) {
            totalQuantity += calculateTotalQuantity(itemList, idUser);

        }

        if(totalQuantity > 0) {
            entries.add(new PieEntry((float) totalQuantity, categoryName));
        }
        Log.e("SteelDataEntries", String.valueOf(totalQuantity));
        return entries;
    }

    // Crear entradas para categorías de batería en el gráfico
    private List<PieEntry> createEntriesFromBatteryMap(Map<String, List<BatteryItem>> itemMap, String categoryName, int idUser) {
        List<PieEntry> entries = new ArrayList<>();

        double totalQuantity = 0;
        for (List<BatteryItem> itemList : itemMap.values()) {
            totalQuantity += calculateTotalQuantity(itemList, idUser);
        }

        if(totalQuantity > 0) {
            entries.add(new PieEntry((float) totalQuantity, categoryName));
        }

        return entries;
    }

    // Crear entradas para otras categorías en el gráfico
    private List<PieEntry> createEntriesFromList(List<Category> itemList, String categoryName, int userId) {
        List<PieEntry> entries = new ArrayList<>();

        double totalQuantity = calculateTotalQuantity(itemList, userId);

        if (totalQuantity > 0) {
            entries.add(new PieEntry((float) totalQuantity, categoryName));
        }

        return entries;
    }

    // Calcular la cantidad total de los elementos en la lista
    private double calculateTotalQuantity(List<? extends Object> itemList, int userId) {
        double totalQuantity = 0;

        for (Object item : itemList) {
            if (item instanceof Category) {
                Category category = (Category) item;
                int categoryUserId = category.getUserId();
                if (categoryUserId == userId) {
                    totalQuantity += Float.parseFloat(category.getQuantity());
                }
            } else if (item instanceof PlasticItem) {
                PlasticItem plasticItem = (PlasticItem) item;
                int plasticItemUserId = plasticItem.getUserId();
                if (plasticItemUserId == userId) {
                    totalQuantity += Float.parseFloat(plasticItem.getQuantity());
                }
            } else if (item instanceof SteelItem) {
                SteelItem steelItem = (SteelItem) item;
                int steelItemUserId = steelItem.getUserId();
                if (steelItemUserId == userId) {
                    totalQuantity += Float.parseFloat(steelItem.getQuantity());
                }
            } else if (item instanceof BatteryItem) {
                BatteryItem batteryItem = (BatteryItem) item;
                int batteryItemUserId = batteryItem.getUserId();
                if (batteryItemUserId == userId) {
                    totalQuantity += Float.parseFloat(batteryItem.getQuantity());
                }
            }
        }

        return totalQuantity;
    }

    // Obtener colores para las secciones del gráfico
    private ArrayList<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF5733")); // Papel
        colors.add(Color.parseColor("#FFC300")); // Cartón
        colors.add(Color.parseColor("#36A2EB")); // Vidrio
        colors.add(Color.parseColor("#4CAF50")); // Plástico
        colors.add(Color.parseColor("#9C27B0")); // Electrónicos
        colors.add(Color.parseColor("#FF9800")); // Metal
        colors.add(Color.parseColor("#795548")); // Textil
        colors.add(Color.parseColor("#607D8B")); // Baterías
        return colors;
    }

    // Navegar a la pantalla de edición de perfil
    private void navigateToEditProfile() {
        EditProfileFragment editProfileFragment = new EditProfileFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_main, editProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}