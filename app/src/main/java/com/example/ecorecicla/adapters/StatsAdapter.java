package com.example.ecorecicla.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecorecicla.CategoryAdapter;
import com.example.ecorecicla.EntryData;
import com.example.ecorecicla.R;
import com.example.ecorecicla.UserData;
import com.example.ecorecicla.models.BatteryItem;
import com.example.ecorecicla.models.Category;
import com.example.ecorecicla.models.Entry;
import com.example.ecorecicla.models.PlasticItem;
import com.example.ecorecicla.models.Stats;
import com.example.ecorecicla.models.SteelItem;
import com.example.ecorecicla.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private final List<Object> items;
    private final Context context;
    private final int currentUserId;
    private final String maxCategory;
    private Entry entry;

    public StatsAdapter(Context context, List<Object> items, int currentUserId, String maxCategory) {
        this.context = context;
        this.items = items;
        this.currentUserId = currentUserId;
        this.maxCategory = maxCategory;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_adapter_item, parent, false);
        StatsViewHolder viewHolder = new StatsViewHolder(view, maxCategory);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Object clickedItem = items.get(position);
                    showDetails(clickedItem);
                }
            }
        });

        return viewHolder;
    }

    private void showDetails(Object clickedItem) {
        UserData userData = new UserData(context);
        User userId = userData.getCurrentUser();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        MaterialTextView tvSheet = bottomSheetDialog.findViewById(R.id.tvSheet);
        MaterialTextView tvTitle = bottomSheetDialog.findViewById(R.id.tvTitle);
        RecyclerView rvObjets = bottomSheetDialog.findViewById(R.id.rvObjets);

        if (clickedItem instanceof Stats && rvObjets != null) {
            Stats stats = (Stats) clickedItem;

            String selectedCategory = getSelectedCategory(stats);

            if (selectedCategory != null && entry != null) {
                List<Category> categoryList = processCategoryList((List<Category>) entry, userId.getId());
                CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
                rvObjets.setLayoutManager(new LinearLayoutManager(context));
                rvObjets.setAdapter(categoryAdapter);
            } else {
                tvSheet.setText("Category not found");
            }
        }

            /*
            if (stats.getCategories().contains("GLASS")) {
                if (entry != null) {
                    List<Category> glassItemList = processCategoryList(entry.getGlassList(), userId.getId());
                    CategoryAdapter categoryAdapter = new CategoryAdapter(glassItemList);
                    rvObjets.setLayoutManager(new LinearLayoutManager(context));
                    rvObjets.setAdapter(categoryAdapter);
                }
            } else if (stats.getCategories().contains("PAPER")) {
                if (entry != null) {
                    List<Category> paperItemList = processCategoryList(entry.getPaperList(), userId.getId());
                    CategoryAdapter categoryAdapter = new CategoryAdapter(paperItemList);
                    rvObjets.setLayoutManager(new LinearLayoutManager(context));
                    rvObjets.setAdapter(categoryAdapter);
                }
            } else if (stats.getCategories().contains("ELECTRONIC")) {
                if (entry != null) {
                    List<Category> electronicItemList = processCategoryList(entry.getElectronicList(), userId.getId());
                    CategoryAdapter categoryAdapter = new CategoryAdapter(electronicItemList);
                    rvObjets.setLayoutManager(new LinearLayoutManager(context));
                    rvObjets.setAdapter(categoryAdapter);
                }

            } else if (stats.getCategories().contains("CARDBOARD")) {
                if (entry != null) {
                    List<Category> cardboardItemList = processCategoryList(entry.getCardboardList(), userId.getId());
                    CategoryAdapter categoryAdapter = new CategoryAdapter(cardboardItemList);
                    rvObjets.setLayoutManager(new LinearLayoutManager(context));
                    rvObjets.setAdapter(categoryAdapter);
                }
            } else if (stats.getCategories().contains("TEXTILES")) {
                if (entry != null) {
                    List<Category> textilesItemList = processCategoryList(entry.getTextilesList(), userId.getId());
                    CategoryAdapter categoryAdapter = new CategoryAdapter(textilesItemList);
                    rvObjets.setLayoutManager(new LinearLayoutManager(context));
                    rvObjets.setAdapter(categoryAdapter);
                }
            } else if (stats.getCategories().contains("PLASTIC")) {
                processPlasticCategory(entry.getPlastic(), userId.getId());
                // Resto del código para configurar el RecyclerView y mostrar la información
            } else if (stats.getCategories().contains("STEEL")) {
                processSteelCategory(entry.getSteel(), userId.getId());
                // Resto del código para configurar el RecyclerView y mostrar la información
            }else if (stats.getCategories().contains("BATTERY")) {
                processBatteryCategory(entry.getBattery(), userId.getId());
                // Resto del código para configurar el RecyclerView y mostrar la información
            }else {
                tvSheet.setText("Category 'CATEGORY_NAME' not found");
            }

             */


        bottomSheetDialog.show();
    }

    private String getSelectedCategory(Stats stats) {
        for (String category : stats.getCategories()) {
            if (stats.getCategoryStats(category) != null) {
                return category;
            }
        }
        return null;
    }

    private List<Category> processCategoryList(List<Category> categoryList, int userId) {
        List<Category> categoryItemList = new ArrayList<>();

        if (categoryList != null) {
            for (Category category : categoryList) {
                if (category.getUserId() == userId) {
                    Category categoryItem = new Category(category.getId(), category.getUserId(), category.getQuantity(), category.getDate(),
                            category.getPrice(), category.getCategoryName());
                    categoryItemList.add(categoryItem);
                }
            }
        }

        return categoryItemList;
    }

    private void processPlasticCategory(Map<String, List<PlasticItem>> plasticMap, int userId) {
        for (List<PlasticItem> plasticItemList : plasticMap.values()) {
            for (PlasticItem plasticItem : plasticItemList) {
                if (plasticItem.getUserId() == userId) {
                    String date = plasticItem.getDate();
                    String quantity = plasticItem.getQuantity();
                    String price = plasticItem.getPrice();

                }
            }
        }
    }

    private void processSteelCategory(Map<String, List<SteelItem>> steelMap, int userId) {
        for (List<SteelItem> steelItemList : steelMap.values()) {
            for (SteelItem steelItem : steelItemList) {
                if (steelItem.getUserId() == userId) {
                    String date = steelItem.getDate();
                    String quantity = steelItem.getQuantity();
                    String price = steelItem.getPrice();
                }
            }
        }

    }

    private void processBatteryCategory(Map<String, List<BatteryItem>> batteryMap, int userId) {
        for (List<BatteryItem> batteryItemList : batteryMap.values()) {
            for (BatteryItem batteryItem : batteryItemList) {
                if (batteryItem.getUserId() == userId) {
                    String date = batteryItem.getDate();
                    String quantity = batteryItem.getQuantity();
                    String price = batteryItem.getPrice();
                }
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        Object currentItem = items.get(position);
        holder.bindItem(currentItem, maxCategory);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StatsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategory, tvSubTotal, tvPercentTotal, tvValTotal;
        private final ImageView ivIcon;
        private final FrameLayout frameAnimation;

        public StatsViewHolder(@NonNull View itemView, String maxCategory) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvSubTotal = itemView.findViewById(R.id.tvSubTotal);
            tvValTotal = itemView.findViewById(R.id.tvValTotal);
            tvPercentTotal = itemView.findViewById(R.id.tvPercentTotal);
            frameAnimation = itemView.findViewById(R.id.frameAnimation);

            frameAnimation.setVisibility(maxCategory.equals(tvCategory.getText().toString()) ? View.VISIBLE : View.GONE);
        }

        public void bindItem(Object item, String maxCategory) {
            if (item instanceof Stats) {
                bindStats((Stats) item, maxCategory);
            }
        }

        private void bindStats(Stats stats, String maxCategory) {
            Set<String> categories = stats.getCategories();
            for (String category : categories) {
                Stats.CategoryStats categoryStats = stats.getCategoryStats(category);
                String translatedCategoryName = translateCategoryName(category);

                tvCategory.setText(translatedCategoryName);

                double totalQuantity = categoryStats.getTotalQuantity();
                double totalPrice = categoryStats.getTotalPrice();
                double percentTotal = totalPrice / totalQuantity;

                tvSubTotal.setText(formatNumber(totalQuantity));
                tvValTotal.setText(formatNumber(totalPrice));
                tvPercentTotal.setText(formatNumber(percentTotal));
                Log.e("MaxCategory", maxCategory);
                frameAnimation.setVisibility(category.equals(maxCategory) ? View.VISIBLE : View.GONE);
            }
        }

        private String translateCategoryName(String categoryName) {
            switch (categoryName) {
                case "PLASTIC":
                    ivIcon.setImageResource(R.drawable.ic_plastic);
                    return "Plastico";
                case "PAPER":
                    ivIcon.setImageResource(R.drawable.ic_paper);
                    return "Papel";
                case "GLASS":
                    ivIcon.setImageResource(R.drawable.ic_glass);
                    return "Vidrio";
                case "ELECTRONIC":
                    ivIcon.setImageResource(R.drawable.ic_electronic);
                    return "Electronicos";
                case "CARDBOARD":
                    ivIcon.setImageResource(R.drawable.ic_card);
                    return "Cartón";
                case "STEEL":
                    ivIcon.setImageResource(R.drawable.ic_steel);
                    return "Metal";
                case "TEXTILES":
                    ivIcon.setImageResource(R.drawable.ic_textil);
                    return "Textiles";
                case "BATTERY":
                    ivIcon.setImageResource(R.drawable.ic_batteries);
                    return "Baterías";
                default:
                    return categoryName;
            }
        }

        private String formatNumber(double number) {
            DecimalFormat decimalFormat = new DecimalFormat("0.#");
            return decimalFormat.format(number);
        }

    }

}