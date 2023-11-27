package com.example.ecorecicla.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
        EntryData entryData = new EntryData(context);
        entry = entryData.getEntry();

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        MaterialTextView tvTitle = bottomSheetDialog.findViewById(R.id.tvTitle);
        ChipGroup chipGroup = bottomSheetDialog.findViewById(R.id.chipGroup);
        RecyclerView rvObjets = bottomSheetDialog.findViewById(R.id.rvObjets);

        if (clickedItem instanceof Stats && rvObjets != null) {
            Stats stats = (Stats) clickedItem;

            String selectedCategory = getSelectedCategory(stats);

            if (selectedCategory != null) {
                List<Category> selectedCategoryList;

                switch (selectedCategory) {
                    case "PLASTIC":
                        tvTitle.setText("Plásticos");
                        selectedCategoryList = processPlasticCategory(entry.getPlastic(), userId.getId());
                        break;
                    case "PAPER":
                        tvTitle.setText("Papel");
                        selectedCategoryList = processCategoryList(entry.getPaperList(), selectedCategory, userId.getId());
                        break;
                    case "ELECTRONIC":
                        tvTitle.setText("Electrónicos");
                        selectedCategoryList = processCategoryList(entry.getElectronicList(), selectedCategory, userId.getId());
                        break;
                    case "GLASS":
                        tvTitle.setText("Vidrio");
                        selectedCategoryList = processCategoryList(entry.getGlassList(), selectedCategory, userId.getId());
                        break;
                    case "CARDBOARD":
                        tvTitle.setText("Cartón");
                        selectedCategoryList = processCategoryList(entry.getCardboardList(), selectedCategory, userId.getId());
                        break;
                    case "STEEL":
                        tvTitle.setText("Aceros");
                        selectedCategoryList = processSteelCategory(entry.getSteel(), userId.getId());
                        break;
                    case "TEXTILES":
                        tvTitle.setText("Textiles");
                        selectedCategoryList = processCategoryList(entry.getTextilesList(), selectedCategory, userId.getId());
                        break;
                    case "BATTERY":
                        tvTitle.setText("Baterías");
                        selectedCategoryList = processBatteryCategory(entry.getBattery(), userId.getId());
                        break;
                    default:
                        selectedCategoryList = new ArrayList<>();
                        break;
                }

                CategoryAdapter categoryAdapter = new CategoryAdapter(selectedCategoryList, chipGroup);
                rvObjets.setLayoutManager(new LinearLayoutManager(context));
                categoryAdapter.addFilterChips();
                rvObjets.setAdapter(categoryAdapter);
            } else {
                Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }

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

    private List<Category> processCategoryList(List<Category> categoryList, String selectedCategory, int userId) {
        List<Category> categoryItemList = new ArrayList<>();

        if (categoryList != null) {
            for (Category category : categoryList) {
                if (category.getUserId() == userId && category.getCategoryName().equals(selectedCategory)) {
                    Log.d("CategoryList", "Adding category: " + category.getCategoryName() + ", Quantity: " + category.getQuantity() + ", Price: " + category.getPrice());
                    Category categoryItem = new Category(category.getId(), category.getUserId(), category.getQuantity(), category.getDate(),
                            category.getPrice(), category.getCategoryName());
                    categoryItemList.add(categoryItem);
                }
            }
        }

        return categoryItemList;
    }

    private List<Category> processPlasticCategory(Map<String, List<PlasticItem>> plasticMap, int userId) {
        List<Category> categoryItemList = new ArrayList<>();

        for (Map.Entry<String, List<PlasticItem>> entry : plasticMap.entrySet()) {
            String categoryName = entry.getKey();
            List<PlasticItem> plasticItemList = entry.getValue();

            for (PlasticItem plasticItem : plasticItemList) {
                if (plasticItem.getUserId() == userId) {
                    Category categoryItem = new Category(plasticItem.getId(), userId, plasticItem.getQuantity(), plasticItem.getDate(),
                            plasticItem.getPrice(), categoryName);
                    categoryItemList.add(categoryItem);
                }
            }
        }

        return categoryItemList;
    }

    private List<Category> processSteelCategory(Map<String, List<SteelItem>> steelcMap, int userId) {
        List<Category> categoryItemList = new ArrayList<>();

        for (Map.Entry<String, List<SteelItem>> entry : steelcMap.entrySet()) {
            String categoryName = entry.getKey();
            List<SteelItem> steelItemList = entry.getValue();

            for (SteelItem steelItem : steelItemList) {
                if (steelItem.getUserId() == userId) {
                    Category categoryItem = new Category(steelItem.getId(), userId, steelItem.getQuantity(), steelItem.getDate(),
                            steelItem.getPrice(), categoryName);
                    categoryItemList.add(categoryItem);
                }
            }
        }

        return categoryItemList;
    }

    private List<Category> processBatteryCategory(Map<String, List<BatteryItem>> batteryMap, int userId) {
        List<Category> categoryItemList = new ArrayList<>();

        for (Map.Entry<String, List<BatteryItem>> entry : batteryMap.entrySet()) {
            String categoryName = entry.getKey();
            List<BatteryItem> batteryItemList = entry.getValue();

            for (BatteryItem batteryItem : batteryItemList) {
                if (batteryItem.getUserId() == userId) {
                    Category categoryItem = new Category(batteryItem.getId(), userId, batteryItem.getQuantity(), batteryItem.getDate(),
                            batteryItem.getPrice(), categoryName);
                    categoryItemList.add(categoryItem);
                }
            }
        }

        return categoryItemList;
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
        private final MaterialButton btnDetails;

        public StatsViewHolder(@NonNull View itemView, String maxCategory) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvSubTotal = itemView.findViewById(R.id.tvSubTotal);
            tvValTotal = itemView.findViewById(R.id.tvValTotal);
            tvPercentTotal = itemView.findViewById(R.id.tvPercentTotal);
            frameAnimation = itemView.findViewById(R.id.frameAnimation);
            btnDetails = itemView.findViewById(R.id.btnDetails);

            frameAnimation.setVisibility(maxCategory.equals(tvCategory.getText().toString()) ? View.VISIBLE : View.GONE);

            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleItemClick();
                }
            });
        }

        private void handleItemClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Object clickedItem = items.get(position);
                showDetails(clickedItem);
            }
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