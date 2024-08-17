package com.example.losako;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.losako.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDollarAmount;
    private EditText editTextExchangeRate;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDollarAmount = findViewById(R.id.editTextDollarAmount);
        editTextExchangeRate = findViewById(R.id.editTextExchangeRate);
        textViewResult = findViewById(R.id.textViewResult);

        Button buttonConvert = findViewById(R.id.buttonConvert);
        buttonConvert.setOnClickListener(view -> {
            try {
                double dollarAmount = Double.parseDouble(editTextDollarAmount.getText().toString());
                double exchangeRate = Double.parseDouble(editTextExchangeRate.getText().toString());
                double result = dollarAmount * exchangeRate;
                textViewResult.setText(String.valueOf(result));
            } catch (NumberFormatException e) {
                // Gérer l'erreur de format
                Toast.makeText(MainActivity.this, "Veuillez entrer des valeurs valides", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
