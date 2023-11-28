package com.example.ecorecicla;

import java.util.HashMap;
import java.util.Map;

public class CategoryMapper {
    private static final Map<String, String> categoryMap = new HashMap<>();

    static {
        categoryMap.put("PLÁSTICO", "PLASTIC");
        categoryMap.put("PAPEL", "PAPER");
        categoryMap.put("ELECTRÓNICOS", "ELECTRONIC");
        categoryMap.put("VIDRIO", "GLASS");
        categoryMap.put("CARTÓN", "CARDBOARD");
        categoryMap.put("METAL", "STEEL");
        categoryMap.put("TEXTILES", "TEXTILES");
        categoryMap.put("BATERÍAS", "BATTERY");
    }

    public static String mapCategory(String spanishCategory) {
        return categoryMap.getOrDefault(spanishCategory, spanishCategory);
    }
}
