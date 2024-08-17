package com.example.losako;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDollarAmount = findViewById(R.id.editTextDollarAmount);
        editTextExchangeRate = findViewById(R.id.editTextExchangeRate);
        textViewResult = findViewById(R.id.textViewResult);

        Button buttonConvert = findViewById(R.id.buttonConvert);
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double dollarAmount = Double.parseDouble(editTextDollarAmount.getText().toString());
                    double exchangeRate = Double.parseDouble(editTextExchangeRate.getText().toString());
                    double result = dollarAmount * exchangeRate;
                    textViewResult.setText(String.valueOf(result));
                } catch (NumberFormatException e) {
                    // Gérer l'erreur de format
                    Toast.makeText(MainActivity.this, "Veuillez entrer des valeurs valides", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}