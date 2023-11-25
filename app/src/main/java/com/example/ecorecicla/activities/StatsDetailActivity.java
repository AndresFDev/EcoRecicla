package com.example.ecorecicla.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ecorecicla.R;
import com.google.android.material.button.MaterialButton;

public class StatsDetailActivity extends AppCompatActivity {

    private MaterialButton btnBack;
    TextView tvTitleDatail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_detail);

        tvTitleDatail = findViewById(R.id.tvTitleDatail);

        String title = getIntent().getStringExtra("title");
        tvTitleDatail.setText(title);

        managerButtons();
    }

    public void managerButtons() {
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}