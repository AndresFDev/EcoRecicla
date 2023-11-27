package com.example.ecorecicla;

import android.content.Context;
import android.util.Log;

import com.example.ecorecicla.models.BatteryItem;
import com.example.ecorecicla.models.Category;
import com.example.ecorecicla.models.Entry;
import com.example.ecorecicla.models.PlasticItem;
import com.example.ecorecicla.models.SteelItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryData {
    private static final String FILE_PATH = "entryData.json";
    private Context context;
    private Entry entry;

    // Constructor
    public EntryData(Context context) {
        this.context = context;
        this.entry = createDefaultEntry();
        loadEntryData();
    }

    // Método para obtener la entrada actual
    public Entry getEntry() {
        return entry;
    }

    // Constantes para los códigos de validación
    public static final int SUCCESS = 0;
    public static final int EMPTY_QUANTITY = 1;
    public static final int INVALID_QUANTITY = 2;
    public static final int EMPTY_SUB_CATEGORY = 3;
    public static final int EMPTY_DATE = 4;
    public static final int FORMAT_DATE = 5;
    public static final int EMPTY_PRICE = 6;
    public static final int INVALID_PRICE = 7;
    public static final int MISSING_FIELDS = 8;
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    // Método para validar los campos de una entrada
    public int validateEntryFields(String quantity, String subcategory, String date, String price) {
        if (quantity.isEmpty() && subcategory.isEmpty() && date.isEmpty() && price.isEmpty()) {
            return MISSING_FIELDS;
        }

        if (quantity.isEmpty()) {
            return EMPTY_QUANTITY;
        }

        if (Double.parseDouble(quantity) <= 0) {
            return INVALID_QUANTITY;
        }

        if (subcategory.isEmpty()) {
            return EMPTY_SUB_CATEGORY;
        }

        if (date.isEmpty()) {
            return EMPTY_DATE;
        }

        if (!isValidFormat(date, DATE_FORMAT)) {
            return FORMAT_DATE;
        }

        if (price.isEmpty()) {
            return EMPTY_PRICE;
        }

        if (Double.parseDouble(price) <= 0) {
            return INVALID_PRICE;
        }

        return SUCCESS;
    }

    // Método para verificar el formato de una fecha
    public boolean isValidFormat(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);

        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Métodos para agregar entradas de plástico, acero y batería
    public void addPlasticEntry(String subCategory, PlasticItem plasticItem) {
        if (entry.getPlastic() == null) {
            entry.setPlastic(new HashMap<>());
        }

        List<PlasticItem> plasticItemList = loadPlasticItemList(subCategory);

        if (plasticItemList == null) {
            plasticItemList = new ArrayList<>();
        }

        plasticItemList.add(plasticItem);
        entry.getPlastic().put(subCategory, plasticItemList);
    }

    public void addSteelEntry(String subCategory, SteelItem steelItem) {
        if (entry.getSteel() == null) {
            entry.setSteel(new HashMap<>());
        }

        List<SteelItem> steelItemList = loadSteelItemList(subCategory);

        if (steelItemList == null) {
            steelItemList = new ArrayList<>();
        }

        steelItemList.add(steelItem);
        entry.getSteel().put(subCategory, steelItemList);
    }

    public void addBatteryEntry(String subCategory, BatteryItem batteryItem) {
        if (entry.getBattery() == null) {
            entry.setBattery(new HashMap<>());
        }

        List<BatteryItem> batteryItemList = loadBatteryItemList(subCategory);

        if (batteryItemList == null) {
            batteryItemList = new ArrayList<>();
        }

        batteryItemList.add(batteryItem);
        entry.getBattery().put(subCategory, batteryItemList);
    }

    // Métodos para cargar listas de plástico, acero y batería
    public List<PlasticItem> loadPlasticItemList(String subCategory) {
        List<PlasticItem> plasticItemList = new ArrayList<>();

        if (entry.getPlastic() != null) {
            List<PlasticItem> subCategoryList = entry.getPlastic().get(subCategory);

            if (subCategoryList != null) {
                plasticItemList.addAll(subCategoryList);
            }

        }

        return plasticItemList;
    }

    public List<SteelItem> loadSteelItemList(String subCategory) {
        List<SteelItem> steelItemList = new ArrayList<>();

        if (entry.getSteel() != null) {
            List<SteelItem> subCategoryList = entry.getSteel().get(subCategory);

            if (subCategoryList != null) {
                steelItemList.addAll(subCategoryList);
            }
        }

        return steelItemList;
    }

    public List<BatteryItem> loadBatteryItemList(String subCategory) {
        List<BatteryItem> batteryItemList = new ArrayList<>();

        if (entry.getBattery() != null) {
            List<BatteryItem> subCategoryList = entry.getBattery().get(subCategory);

            if (subCategoryList != null) {
                batteryItemList.addAll(subCategoryList);
            }
        }

        return batteryItemList;
    }

    // Método para cargar datos de entrada desde un archivo JSON
    public void loadEntryData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFileStreamPath(FILE_PATH)))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            Gson gson = new Gson();
            Entry loadedEntry = gson.fromJson(jsonBuilder.toString(), Entry.class);
            Log.d("EntryData", "Data cargada correctamente desde " + FILE_PATH);

            if (loadedEntry != null) {
                entry = loadedEntry; // Reemplaza la entrada actual con la cargada
            }
        } catch (IOException e) {
            Log.e("EntryData", "Error al cargar data desde " + FILE_PATH + ": " + e.getMessage());
        }
    }

    // Método para guardar datos de entrada en un archivo JSON
    public void saveEntryData() {
        try (FileOutputStream outputStream = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(entry, writer);
            Log.d("EntryData", "Data guardada correctamente en " + FILE_PATH);
        } catch (IOException e) {
            Log.e("EntryData", "Error al guardar data en " + FILE_PATH + ": " + e.getMessage());
        }
    }

    // Método para crear una entrada predeterminada
    private Entry createDefaultEntry() {
        Map<String, List<PlasticItem>> plastic = new HashMap<>();
        List<Category> paperList = new ArrayList<>();
        List<Category> electronicList = new ArrayList<>();
        List<Category> glassList = new ArrayList<>();
        List<Category> cardboardList = new ArrayList<>();
        Map<String, List<SteelItem>> steel = new HashMap<>();
        List<Category> textilesList = new ArrayList<>();
        Map<String, List<BatteryItem>> battery = new HashMap<>();

        return new Entry(plastic, paperList, electronicList, glassList, cardboardList, steel, textilesList, battery);
    }
}