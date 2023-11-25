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

public class StatsFragment extends Fragment {
    private RecyclerView rvStatsCategory;
    private FrameLayout frameAnimation;
    private MaterialTextView tvMessage;
    private EntryData entryData;
    private Entry entry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        frameAnimation = rootView.findViewById(R.id.frameAnimation);
        tvMessage = rootView.findViewById(R.id.tvMessage);
        rvStatsCategory = rootView.findViewById(R.id.rvStatsCategory);
        entryData = new EntryData(requireContext());
        entry = entryData.getEntry();

        setupStatsRecyclerView();

        return rootView;
    }

    Map<String, Double> categoryTotals = new HashMap<>();
    private void setupStatsRecyclerView() {
        if (entry != null) {
            UserData userData = new UserData(getContext());
            User userId = userData.getCurrentUser();

            List<Object> allItems = new ArrayList<>();
            List<Stats> allStats = new ArrayList<>();

            processCategoryList(entry.getPaperList(), "PAPER", userId.getId(), categoryTotals, allItems, allStats);
            processCategoryList(entry.getElectronicList(), "ELECTRONIC", userId.getId(), categoryTotals, allItems, allStats);
            processCategoryList(entry.getGlassList(), "GLASS", userId.getId(), categoryTotals, allItems, allStats);
            processCategoryList(entry.getCardboardList(), "CARDBOARD", userId.getId(), categoryTotals, allItems, allStats);
            processCategoryList(entry.getTextilesList(), "TEXTILES", userId.getId(), categoryTotals, allItems, allStats);


            processPlasticCategory(entry.getPlastic(), userId.getId(), allItems, allStats);
            processSteelCategory(entry.getSteel(), userId.getId(), allItems, allStats);
            processBatteryCategory(entry.getBattery(), userId.getId(), allItems, allStats);

            if (allItems == null || allItems.size() == 0) {
                rvStatsCategory.setVisibility(View.GONE);
                frameAnimation.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.VISIBLE);
            } else {
                rvStatsCategory.setVisibility(View.VISIBLE);
                frameAnimation.setVisibility(View.GONE);
                tvMessage.setVisibility(View.GONE);
            }

            if (!allStats.isEmpty()) {
                String maxCategory = findMaxCategory(allStats);
                Log.e("MaxCategoryFragment", maxCategory);
                StatsAdapter statsAdapter = new StatsAdapter(getContext(), allItems, userId.getId(), maxCategory);
                rvStatsCategory.setLayoutManager(new LinearLayoutManager(requireContext()));
                rvStatsCategory.setAdapter(statsAdapter);
            }
        } else {
            // Imprimir un mensaje o realizar alguna acción en caso de que entry sea nulo
            Log.e("StatsFragment", "entry es nulo");
        }
    }

    private void processCategoryList(List<Category> categoryList, String categoryName, int userId, Map<String, Double> categoryTotals, List<Object> allItems, List<Stats> allStats) {
        double totalQuantity = 0;
        double totalPrice = 0;

        if (categoryList != null) {
            for (Category category : categoryList) {
                if (category.getUserId() == userId) {
                    totalQuantity += Double.parseDouble(category.getQuantity());
                    totalPrice += Double.parseDouble(category.getPrice());
                }
            }
        }

        Stats categoryStats = new Stats();
        if (totalQuantity > 0 || totalPrice > 0) {
            categoryStats.addCategoryStats(categoryName, totalQuantity, totalPrice);
            allItems.add(categoryStats);
            allStats.add(categoryStats);
        }
    }

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

    private String findMaxCategory(List<Stats> allStats) {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Stats stats : allStats) {
            Set<String> categories = stats.getCategories();
            for (String category : categories) {
                Stats.CategoryStats categoryStats = stats.getCategoryStats(category);
                double total = categoryStats.getTotalPrice();

                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + total);
            }
        }

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