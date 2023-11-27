package com.example.ecorecicla.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecorecicla.R;
import com.google.android.material.button.MaterialButton;

public class RecoveryPassActivity extends AppCompatActivity {

    private MaterialButton btnBack, btnSendRecoveryPass, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_pass);

        managerButtons();
    }

    public void managerButtons() {
        btnBack = findViewById(R.id.btnBack);
        btnSendRecoveryPass = findViewById(R.id.btnSendRecoveryPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSendRecoveryPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}