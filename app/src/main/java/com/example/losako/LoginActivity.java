package com.example.losako;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.losako.models.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextView createAccountLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkIfLoggedIn();

        // Initialisez les vues
        TextInputLayout usernameTextInputLayout = findViewById(R.id.textInputLayout2);
        EditText usernameEditText = usernameTextInputLayout.getEditText();
        TextInputLayout passwordTextInputLayout = findViewById(R.id.textInputLayout3);
        EditText passwordEditText = passwordTextInputLayout.getEditText();
        Button connectButton = findViewById(R.id.button7);
        createAccountLink = findViewById(R.id.create_account_link);

        createAccountLink.setOnClickListener((view) -> {
            Intent intent = new Intent(LoginActivity.this, CreateAcountActivity.class);
            startActivity(intent);
        });

        // Ajoutez un écouteur d'événements au bouton
        connectButton.setOnClickListener(view -> {
            assert usernameEditText != null;
            String enteredUsername = usernameEditText.getText().toString();
            assert passwordEditText != null;
            String enteredPassword = passwordEditText.getText().toString();

            // Vérifiez si les entrées correspondent aux valeurs attendues
            if (checkUserLogin(enteredUsername, enteredPassword)) {
                Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                // Stockez les identifiants dans SharedPreferences
                saveCredentialsToPreferences(enteredUsername, enteredPassword);
                // Démarrer SecondActivity après une connexion réussie
                Intent intent = new Intent(LoginActivity.this, MainActivity.class); // Remplacez par le nom de votre activité de destination
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Numéro de téléphone ou mot de passe incorrects", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Reportez l'accès à la base de données
    private SQLiteDatabase getDb() {
        return DatabaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
    }

    // Optimisez la vérification de la connexion
    private void checkIfLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Utilisez commit pour une meilleure expérience utilisateur
    public void saveCredentialsToPreferences(String phoneNumber, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String personnelName = getNomPersonnel(phoneNumber, password);

        editor.putBoolean("isLoggedIn", true);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("password", password);
        editor.putString("nomPersonnel", personnelName);
        editor.apply();
    }
    public String getNomPersonnel(String phoneNumber, String password) {
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery("SELECT nom_personnel FROM personnel WHERE phone_number_personnel=? AND password=?", new String[]{phoneNumber, password});

        if (cursor.moveToFirst()) {
            String NomPersonnel = cursor.getString(0);
            cursor.close();
            return NomPersonnel;
        } else {
            cursor.close();
            return ""; // Retournez -1 ou une autre valeur indiquant qu'aucun enregistrement n'a été trouvé
        }
    }

    // Optimisez la requête à la base de données
    private boolean checkUserLogin(String phoneNumber, String password) {
        SQLiteDatabase db = getDb();
        String[] whereArgs = new String[]{phoneNumber, password};
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM personnel WHERE phone_number_personnel=? AND password=?", whereArgs);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

}
