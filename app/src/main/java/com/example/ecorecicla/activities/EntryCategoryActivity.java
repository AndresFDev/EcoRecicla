package com.example.ecorecicla.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ecorecicla.session.SessionManager;
import com.example.ecorecicla.models.Category;
import com.example.ecorecicla.mappers.CategoryMapper;
import com.example.ecorecicla.models.EntryData;
import com.example.ecorecicla.R;
import com.example.ecorecicla.models.UserData;
import com.example.ecorecicla.models.BatteryItem;
import com.example.ecorecicla.models.Entry;
import com.example.ecorecicla.models.PlasticItem;
import com.example.ecorecicla.models.SteelItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntryCategoryActivity extends AppCompatActivity {
    private TextInputLayout tilWeight, tilType, tilDate, tilPrice;
    private TextInputEditText etWeight, etCollectDate, etPrice;
    private MaterialAutoCompleteTextView etType;
    private MaterialButton btnBack, btnSaveData;
    private MaterialTextView tvCategory, tvType;
    private UserData userData;
    private ArrayAdapter<String> subcategoryAdapter;
    private List<String> subcategories = new ArrayList<>();
    private final List<BatteryItem> batteryItemList = new ArrayList<>();
    private final List<SteelItem> steelItemList = new ArrayList<>();
    private final List<PlasticItem> plasticItemList = new ArrayList<>();
    private EntryData entryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_category);

        initializeViews();

        Intent intent = getIntent();
        String categoryName = "";
        if (intent != null) {
            String title = intent.getStringExtra("title");
            tvCategory.setText(title);

            categoryName = CategoryMapper.mapCategory(title.toUpperCase());

            if (Arrays.asList("PLASTIC", "STEEL", "BATTERY").contains(categoryName)) {

                switch (categoryName) {
                    case "PLASTIC":
                        tvType.setText("Tipo de plástico");
                        break;
                    case "STEEL":
                        tvType.setText("Tipo de metal");
                        break;
                    case "BATTERY":
                        tvType.setText("Tipo de batería");
                        break;
                }

                tvType.setVisibility(View.VISIBLE);
                tilType.setVisibility(View.VISIBLE);

                subcategories = getSubcategoriesForCategory(categoryName);
                subcategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, subcategories);
                etType.setAdapter(subcategoryAdapter);
            } else {
                tvType.setVisibility(View.GONE);
                tilType.setVisibility(View.GONE);
            }
        }

        setupButtons();
        datePicker();
    }

    private void initializeViews() {
        tvCategory = findViewById(R.id.tvCategory);
        tilWeight = findViewById(R.id.tilWeight);
        etWeight = findViewById(R.id.etWeight);
        tvType = findViewById(R.id.tvType);
        tilType = findViewById(R.id.tilType);
        etType = findViewById(R.id.etType);
        tilDate = findViewById(R.id.tilDate);
        etCollectDate = findViewById(R.id.etCollectDate);
        tilPrice = findViewById(R.id.tilPrice);
        etPrice = findViewById(R.id.etPrice);
        btnBack = findViewById(R.id.btnBack);
        btnSaveData = findViewById(R.id.btnSaveData);
    }

    private void setupButtons() {
        btnBack.setOnClickListener(v -> finish());
        btnSaveData.setOnClickListener(v -> newEntry());
    }

    private List<String> getSubcategoriesForCategory(String category) {
        switch (category) {
            case "PLASTIC":
                return Arrays.asList("PET (Polietileno Tereftalato)", "HDPE (Polietileno de Alta Densidad)", "PVC (Policloruro de Vinilo)", "LDPE (Polietileno de Baja Densidad)", "PP (Polipropileno)", "PS (Poliestireno)", "Otros");
            case "STEEL":
                return Arrays.asList("Acero", "Aluminio", "Cobre", "Niquel");
            case "BATTERY":
                return Arrays.asList("Alcalinas", "Recargables", "Plomo-ácido (Vehículos)");
            default:
                return new ArrayList<>();
        }
    }

    private int getNextPlasticItemId(String subCategory) {
        List<PlasticItem> plasticItemList = entryData.loadPlasticItemList(subCategory);

        int currentId = plasticItemList.stream()
                .mapToInt(PlasticItem::getId)
                .max()
                .orElse(0);

        return currentId + 1;
    }

    private int getNextSteelItemId(String subCategory) {
        List<SteelItem> steelItemList = entryData.loadSteelItemList(subCategory);

        int currentId = steelItemList.stream()
                .mapToInt(SteelItem::getId)
                .max()
                .orElse(0);

        return currentId + 1;
    }

    private int getNextBatteryItemId(String subCategory) {
        List<BatteryItem> batteryItemList = entryData.loadBatteryItemList(subCategory);

        int currentId = batteryItemList.stream()
                .mapToInt(BatteryItem::getId)
                .max()
                .orElse(0);

        return currentId + 1;
    }

    private void newEntry() {
        SessionManager sessionManager = new SessionManager(this);
        userData = new UserData(this, sessionManager);
        entryData = new EntryData(this);
        int idUser = userData.getCurrentUser().getId();
        String spanishTitle = getIntent().getStringExtra("title");
        String categoryName = CategoryMapper.mapCategory(spanishTitle.toUpperCase());
        String subCategory = etType.getText().toString();
        String quantity = etWeight.getText().toString();
        String date = etCollectDate.getText().toString();
        String price = etPrice.getText().toString();

        int validationCode = entryData.validateEntryFields(quantity, categoryName, date, price);

        if (validationCode != EntryData.SUCCESS) {
            handleEntryResult(validationCode);
            return;
        }

        Entry entry = entryData.getEntry();
        int id;

        switch (categoryName) {
            case "PLASTIC":
                id = getNextPlasticItemId(subCategory);
                PlasticItem plasticItem = new PlasticItem(id, idUser, quantity, date, price, subCategory);
                entryData.addPlasticEntry(subCategory, plasticItem);
                plasticItemList.add(plasticItem);
                break;
            case "PAPER":
                if (entry.getPaperList() == null) {
                    entry.setPaperList(new ArrayList<>());
                }
                id = entry.getPaperList() != null ? entry.getPaperList().size() + 1 : 1;
                Category paperEntry = new Category(id, idUser, quantity, date, price, categoryName);
                entry.getPaperList().add(paperEntry);
                break;
            case "ELECTRONIC":
                if (entry.getElectronicList() == null) {
                    entry.setElectronicList(new ArrayList<>());
                }
                id = entry.getElectronicList() != null ? entry.getElectronicList().size() + 1 : 1;
                Category electronicEntry = new Category(id, idUser, quantity, date, price, categoryName);
                entry.getElectronicList().add(electronicEntry);
                break;
            case "GLASS":
                if (entry.getGlassList() == null) {
                    entry.setGlassList(new ArrayList<>());
                }
                id = entry.getGlassList() != null ? entry.getGlassList().size() + 1 : 1;
                Category glassEntry = new Category(id, idUser, quantity, date, price, categoryName);
                entry.getGlassList().add(glassEntry);
                break;
            case "CARDBOARD":
                if (entry.getCardboardList() == null) {
                    entry.setCardboardList(new ArrayList<>());
                }
                id = entry.getCardboardList() != null ? entry.getCardboardList().size() + 1 : 1;
                Category cardboardEntry = new Category(id, idUser, quantity, date, price, categoryName);
                entry.getCardboardList().add(cardboardEntry);
                break;
            case "STEEL":
                id = getNextSteelItemId(subCategory);
                SteelItem steelItem = new SteelItem(id, idUser, quantity, date, price, subCategory);
                entryData.addSteelEntry(subCategory, steelItem);
                steelItemList.add(steelItem);
                break;
            case "TEXTILES":
                if (entry.getTextilesList() == null) {
                    entry.setTextilesList(new ArrayList<>());
                }
                id = entry.getTextilesList() != null ? entry.getTextilesList().size() + 1 : 1;
                Category textilesEntry = new Category(id, idUser, quantity, date, price, categoryName);
                entry.getTextilesList().add(textilesEntry);
                break;
            case "BATTERY":
                id = getNextBatteryItemId(subCategory);
                BatteryItem batteryItem = new BatteryItem(id, idUser, quantity, date, price, subCategory);
                entryData.addBatteryEntry(subCategory, batteryItem);
                batteryItemList.add(batteryItem);
                break;
            default:
                Log.w("EntryCategoryActivity", "Categoría no esperada: " + categoryName);
                break;

        }

        Snackbar.make(findViewById(android.R.id.content), "¿Estás seguro de guardar la información?", Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setAction("Aceptar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        entryData.saveEntryData();
                        entryData.loadEntryData();
                        handleEntryResult(EntryData.SUCCESS);
                    }
                })
                .show();
    }

    private void setupTextWatchers() {
        addTextWatcher(tilWeight);
        addTextWatcher(tilType);
        addTextWatcher(tilDate);
        addTextWatcher(tilPrice);
    }

    private void addTextWatcher(TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario realizar ninguna acción antes de que cambie el texto
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Limpiar el error cuando el usuario comienza a editar la contraseña
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario realizar ninguna acción después de que cambie el texto
            }
        });
    }

    private void handleEntryResult(int entryResult) {
        Log.d("EntryCategoryActivity", "handleEntryResult: " + entryResult);
        setupTextWatchers();
        String errorMessage = "";
        switch (entryResult) {
            case EntryData.SUCCESS:
                Intent intent = new Intent(EntryCategoryActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "¡Guardado con exito!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case EntryData.MISSING_FIELDS:
                errorMessage = "Campos obligatorios no proporcionados";
                showError(tilWeight, errorMessage);
                showError(tilType, errorMessage);
                showError(tilDate, errorMessage);
                showError(tilPrice, errorMessage);
                break;

            case EntryData.EMPTY_QUANTITY:
                errorMessage = "Cantida en Kg vacio";
                showError(tilWeight, errorMessage);
                break;

            case EntryData.INVALID_QUANTITY:
                errorMessage = "La cantidad no puede ser 0";
                showError(tilWeight, errorMessage);
                break;

            case EntryData.EMPTY_DATE:
                errorMessage = "Fecha vacía";
                showError(tilDate, errorMessage);
                break;

            case EntryData.FORMAT_DATE:
                errorMessage = "Formato de fecho incorrecta (DD/MM/YYYY)";
                showError(tilDate, errorMessage);
                break;

            case EntryData.EMPTY_PRICE:
                errorMessage = "Valor vacío";
                showError(tilPrice, errorMessage);
                break;

            case EntryData.INVALID_PRICE:
                errorMessage = "Valor no puede ser 0";
                showError(tilPrice, errorMessage);
                break;
        }
        if (!errorMessage.isEmpty()) {
            Toast.makeText(EntryCategoryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }

    public void datePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha de recolección")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            try {
                long selectedDateInMillies = selection;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDateText = dateFormat.format(new Date(selectedDateInMillies));

                // Resto del código para manejar la fecha seleccionada
                tilDate.getEditText().setText(selectedDateText);
            } catch (Exception e) {
                e.printStackTrace();
                // Maneja la excepción según tus necesidades.
            }
        });

        tilDate.setEndIconOnClickListener(view -> {
            datePicker.show(getSupportFragmentManager(), datePicker.toString());
        });
    }
}
