package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class CategoriesActivity extends AppCompatActivity {

    private MaterialButton btn_plastic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        managerButtons();

    }

    public void managerButtons() {
        btn_plastic = findViewById(R.id.btn_plastic);

        btn_plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}