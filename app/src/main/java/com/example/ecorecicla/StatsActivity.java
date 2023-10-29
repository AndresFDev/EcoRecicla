package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class StatsActivity extends AppCompatActivity {
    private MaterialButton btnCategories, btnStats, btnTips, btnPlastic, btnPaper, btnGlass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        navButtons();
        managerButtons();

    }

    public void managerButtons() {
        btnPlastic = findViewById(R.id.btnPlastic);
        btnPaper = findViewById(R.id.btnPaper);
        btnGlass = findViewById(R.id.btnGlass);

        btnPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsDetailActivity.class);
                intent.putExtra("title", getString(R.string.plastic));
                startActivity(intent);
            }
        });

        btnPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsDetailActivity.class);
                intent.putExtra("title", getString(R.string.paper));
                startActivity(intent);
            }
        });

        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsDetailActivity.class);
                intent.putExtra("title", getString(R.string.glass));
                startActivity(intent);
            }
        });
    }

    public void navButtons() {

        btnCategories = findViewById(R.id.btnCategories);
        btnStats = findViewById(R.id.btnStats);
        btnTips = findViewById(R.id.btnTips);

        btnStats.setIconTint(getColorStateList(R.color.white));
        btnStats.setTextColor(getColorStateList(R.color.white));

        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
                startActivity(categoriesIntent);
            }
        });

        btnTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TipsActivity.class);
                startActivity(intent);
            }
        });

    }
}