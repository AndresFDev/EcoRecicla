package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnCategories, btnStats, btnTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navButtons();

    }

    public void navButtons() {

        btnCategories = findViewById(R.id.btnCategories);
        btnStats = findViewById(R.id.btnStats);
        btnTips = findViewById(R.id.btnTips);

        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
                startActivity(categoriesIntent);
            }
        });

         btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatsIntent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(StatsIntent);
            }
        });
/*
        btnTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TipsActivity.class);
                startActivity(intent);
            }
        }); */

    }

}