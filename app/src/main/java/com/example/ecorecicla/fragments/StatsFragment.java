package com.example.ecorecicla.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ecorecicla.EntryData;
import com.example.ecorecicla.R;
import com.example.ecorecicla.UserData;
import com.example.ecorecicla.adapters.StatsAdapter;
import com.example.ecorecicla.models.BatteryItem;
import com.example.ecorecicla.models.Category;
import com.example.ecorecicla.models.Entry;
import com.example.ecorecicla.models.PlasticItem;
import com.example.ecorecicla.models.Stats;
import com.example.ecorecicla.models.SteelItem;
import com.example.ecorecicla.models.User;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Importaciones omitidas por brevedad

public class StatsFragment extends Fragment {
    private RecyclerView rvStatsCategory;
    private FrameLayout frameAnimation;
    private MaterialTextView tvMessage;
    private EntryData entryData;
    private Entry entry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño de la interfaz de usuario para este fragmento
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        // Inicializar vistas y datos
        frameAnimation = rootView.findViewById(R.id.frameAnimation);
        tvMessage = rootView.findViewById(R.id.tvMessage);
        rvStatsCategory = rootView.findViewById(R.id.rvStatsCategory);
        entryData = new EntryData(requireContext());
        entry = entryData.getEntry();

        // Configurar la vista de estadísticas
        setupStatsRecyclerView();

        return rootView;
    }

    // Método principal para configurar la vista de estadísticas
    private void setupStatsRecyclerView() {
        if (entry != null) {
            UserData userData = new UserData(getContext());
            User userId = userData.getCurrentUser();

            // Listas para almacenar elementos y estadísticas
            List<Object> allItems = new ArrayList<>();
            List<Stats> allStats = new ArrayList<>();

            // Procesar diferentes categorías de elementos
            processCategoryList(entry.getPaperList(), "PAPER", userId.getId(), allItems, allStats);
            processCategoryList(entry.getElectronicList(), "ELECTRONIC", userId.getId(), allItems, allStats);
            processCategoryList(entry.getGlassList(), "GLASS", userId.getId(), allItems, allStats);
            processCategoryList(entry.getCardboardList(), "CARDBOARD", userId.getId(), allItems, allStats);
            processCategoryList(entry.getTextilesList(), "TEXTILES", userId.getId(), allItems, allStats);

            processPlasticCategory(entry.getPlastic(), userId.getId(), allItems, allStats);
            processSteelCategory(entry.getSteel(), userId.getId(), allItems, allStats);
            processBatteryCategory(entry.getBattery(), userId.getId(), allItems, allStats);

            // Manejar casos de listas vacías o no vacías
            if (allItems == null || allItems.size() == 0) {
                handleEmptyStats();
            } else {
                handleNonEmptyStats(allItems, allStats, userId.getId());
            }
        } else {
            // Manejar entrada nula
            Log.e("StatsFragment", "entry es nulo");
        }
    }

    // Método para procesar una lista de categorías
    private void processCategoryList(List<Category> categoryList, String categoryName, int userId, List<Object> allItems, List<Stats> allStats) {
        // Variables para acumular cantidades y precios
        double totalQuantity = 0;
        double totalPrice = 0;

        // Procesar cada elemento en la lista de categorías
        if (categoryList != null) {
            for (Category category : categoryList) {
                if (category.getUserId() == userId) {
                    totalQuantity += Double.parseDouble(category.getQuantity());
                    totalPrice += Double.parseDouble(category.getPrice());
                }
            }
        }

        // Crear estadísticas para la categoría y agregar a las listas
        Stats categoryStats = new Stats();
        if (totalQuantity > 0 || totalPrice > 0) {
            categoryStats.addCategoryStats(categoryName, totalQuantity, totalPrice);
            allItems.add(categoryStats);
            allStats.add(categoryStats);
        }
    }

    // Métodos similares para procesar otras categorías (plástico, acero, batería)...
    private void processPlasticCategory(Map<String, List<PlasticItem>> plasticMap, int userId, List<Object> allItems, List<Stats> allStats) {
        double totalQuantityPlastic = 0;
        double totalPricePlastic = 0;

        for (List<PlasticItem> plasticItemList : plasticMap.values()) {
            for (PlasticItem plasticItem : plasticItemList) {
                if (plasticItem.getUserId() == userId) {
                    totalQuantityPlastic += Double.parseDouble(plasticItem.getQuantity());
                    totalPricePlastic += Double.parseDouble(plasticItem.getPrice());
                }
            }
        }

        Stats plasticStats = new Stats();
        if (totalQuantityPlastic > 0 || totalPricePlastic > 0) {
            plasticStats.addCategoryStats("PLASTIC", totalQuantityPlastic, totalPricePlastic);
            allItems.add(plasticStats);
            allStats.add(plasticStats);
        }
    }

    private void processSteelCategory(Map<String, List<SteelItem>> steelMap, int userId, List<Object> allItems, List<Stats> allStats) {
        double totalQuantitySteel = 0;
        double totalPriceSteel = 0;

        for (List<SteelItem> steelItemList : steelMap.values()) {
            for (SteelItem steelItem : steelItemList) {
                if (steelItem.getUserId() == userId) {
                    totalQuantitySteel += Double.parseDouble(steelItem.getQuantity());
                    totalPriceSteel += Double.parseDouble(steelItem.getPrice());
                }
            }
        }

        Stats steelStats = new Stats();
        if (totalQuantitySteel > 0 || totalPriceSteel > 0) {
            steelStats.addCategoryStats("STEEL", totalQuantitySteel, totalPriceSteel);
            allItems.add(steelStats);
            allStats.add(steelStats);
        }
    }

    private void processBatteryCategory(Map<String, List<BatteryItem>> batteryMap, int userId, List<Object> allItems, List<Stats> allStats) {
        double totalQuantityBattery = 0;
        double totalPriceBattery = 0;

        for (List<BatteryItem> batteryItemList : batteryMap.values()) {
            for (BatteryItem batteryItem : batteryItemList) {
                if (batteryItem.getUserId() == userId) {
                    totalQuantityBattery += Double.parseDouble(batteryItem.getQuantity());
                    totalPriceBattery += Double.parseDouble(batteryItem.getPrice());
                }
            }
        }

        Stats batteryStats = new Stats();
        if (totalQuantityBattery > 0 || totalPriceBattery > 0) {
            batteryStats.addCategoryStats("BATTERY", totalQuantityBattery, totalPriceBattery);
            allItems.add(batteryStats);
            allStats.add(batteryStats);
        }
    }

    // Método para manejar casos de estadísticas vacías
    private void handleEmptyStats() {
        rvStatsCategory.setVisibility(View.GONE);
        frameAnimation.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
    }

    // Método para manejar casos de estadísticas no vacías
    private void handleNonEmptyStats(List<Object> allItems, List<Stats> allStats, int userId) {
        rvStatsCategory.setVisibility(View.VISIBLE);
        frameAnimation.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);

        // Encontrar la categoría con el valor máximo
        String maxCategory = findMaxCategory(allStats);
        Log.e("MaxCategoryFragment", maxCategory);

        // Configurar el adaptador para la vista de reciclaje
        StatsAdapter statsAdapter = new StatsAdapter(getContext(), allItems, userId, maxCategory);
        rvStatsCategory.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvStatsCategory.setAdapter(statsAdapter);
    }

    // Método para encontrar la categoría con el valor máximo
    private String findMaxCategory(List<Stats> allStats) {
        // Mapa para almacenar totales de categorías
        Map<String, Double> categoryTotals = new HashMap<>();

        // Calcular totales de categorías desde las estadísticas
        for (Stats stats : allStats) {
            Set<String> categories = stats.getCategories();
            for (String category : categories) {
                Stats.CategoryStats categoryStats = stats.getCategoryStats(category);
                double total = categoryStats.getTotalPrice();

                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + total);
            }
        }

        // Encontrar la categoría con el total máximo
        String maxCategory = "";
        double maxTotal = 0;

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            String category = entry.getKey();
            double total = entry.getValue();

            if (total > maxTotal) {
                maxTotal = total;
                maxCategory = category;
            }
        }

        return maxCategory;
    }
}