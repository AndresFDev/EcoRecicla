package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EntryCategoryActivity extends AppCompatActivity {
    private TextInputLayout tl_3;
    private MaterialButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_category);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datePicker();
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