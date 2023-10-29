package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EntryCategoryActivity extends AppCompatActivity {
    private TextInputLayout tl_3;
    private MaterialButton btnBack, btnSendData;
    private TextView tvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_category);

        tvCategory = findViewById(R.id.tvCategory);

        String title = getIntent().getStringExtra("title");
        tvCategory.setText(title);

        managerButtons();
        datePicker();
    }

    public void managerButtons() {
        btnBack = findViewById(R.id.btnBack);
        btnSendData = findViewById(R.id.btnSendData);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                Toast.makeText(getApplicationContext(), "¡Guardado exitoso!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
    }

    public void datePicker(){
        tl_3 = findViewById(R.id.tl_3);
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()//.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Seleccionar fecha").build();

        datePicker.addOnPositiveButtonClickListener(seleccion ->{
            long selectedDateInMillies = seleccion;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());
            String selectedDateText = dateFormat.format(new Date(selectedDateInMillies));
            tl_3.getEditText().setText(selectedDateText);
        });

        tl_3.setEndIconOnClickListener(view -> {
            datePicker.show(getSupportFragmentManager(), datePicker.toString());
        });
    }
}