package com.example.losako;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        // Démarrer une opération longue dans un thread en arrière-plan

        // Lancer MainActivity après 5000 millisecondes
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 2000);
    }
}
