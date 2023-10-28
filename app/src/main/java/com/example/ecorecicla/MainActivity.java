package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button tipsplastico;
    Button tipspapel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipsplastico = findViewById(R.id.button4);
        tipspapel = findViewById(R.id.button5);

        Intent tipsplastico2= new Intent(getApplicationContext(), TipsplasticActivity2.class);
        Intent tipspapel2= new Intent(getApplicationContext(), TipsPapelActivity2.class);

        tipsplastico .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(tipsplastico2);
            }
        });

        tipspapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(tipspapel2);}
        });

    }
}
