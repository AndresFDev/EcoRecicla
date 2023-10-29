package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TipsDetailActivity extends AppCompatActivity {

    private MaterialButton btnBack;
    private MaterialTextView tvTips, tvDays;
    private LinearProgressIndicator piDaysOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_detail);

        btnBack = findViewById(R.id.btnBack);
        tvTips = findViewById(R.id.tvTips);
        tvDays = findViewById(R.id.tvDays);

        piDaysOff = findViewById(R.id.piDaysOff);

        String title = getIntent().getStringExtra("title");
        tvTips.setText(title);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("GMT-5:00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        dateFormat.setTimeZone(timeZone);

        String dayOfWeek = dateFormat.format(date);

        switch (dayOfWeek) {

            case "lunes":
                String currentDay = "1";
                updateProgress(currentDay);
                tvDays.setText("6");
                break;
            case "martes":
                currentDay = "2";
                updateProgress(currentDay);
                tvDays.setText("5");
                break;
            case "miercoles":
                currentDay = "3";
                updateProgress(currentDay);
                tvDays.setText("4");
                break;
            case "jueves":
                currentDay = "4";
                updateProgress(currentDay);
                tvDays.setText("3");
                break;
            case "viernes":
                currentDay = "5";
                updateProgress(currentDay);
                tvDays.setText("2");
                break;
            case "sábado":
                currentDay = "6";
                updateProgress(currentDay);
                tvDays.setText("1");
                break;
            default:
                currentDay = "7";
                updateProgress(currentDay);
                tvDays.setText("Último día");
                break;
        }

    }

    private void updateProgress(String currentDay) {
        double day = Float.parseFloat(currentDay);
        double totalDays = 7f;
        double progressPercentage = (day / totalDays) * 100;
        int progressFinal = (int) Math.round(progressPercentage);
        piDaysOff.setProgressCompat(progressFinal, true);
    }
}