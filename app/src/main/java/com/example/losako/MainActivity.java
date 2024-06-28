package com.example.losako;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.losako.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled */) {
            @Override
            public void handleOnBackPressed() {
                // Votre logique personnalisée ici
                // Par exemple, vérifiez si l'utilisateur est connecté et fermez l'application
                SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    finishAffinity(); // Ferme toutes les activités de l'application
                    System.exit(0); // Assurez-vous que l'application est fermée complètement
                } else {
                    // Si l'utilisateur n'est pas connecté, laissez-le revenir à l'écran précédent
                    Toast.makeText(MainActivity.this, "Bonjour", Toast.LENGTH_LONG).show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profil) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else {
                replaceFragment(new HomeFragment());
            }

            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
