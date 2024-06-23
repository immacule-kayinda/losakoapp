package com.example.losako;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.losako.models.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAcountActivity extends AppCompatActivity {

    private TextInputLayout editTextNomPersonnelLayout, editTextPostnomPersonnelLayout, editTextPrenomPersonnelLayout, editTextAdressePersonnelLayout, editTextNumeroTelephonePersonnelLayout, editTextSpecialisationPersonnelLayout, passwordPersonnelEditTextLayout, confirmPasswordPersonnelEditTextLayout;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount); // Remplacez par le nom de votre fichier XML

        // Initialisation des vues
        editTextNomPersonnelLayout = findViewById(R.id.nomPersonnelLayout);
        editTextPostnomPersonnelLayout = findViewById(R.id.postnomPersonnelLayout);
        editTextPrenomPersonnelLayout = findViewById(R.id.prenomPersonnelLayout);
        editTextAdressePersonnelLayout = findViewById(R.id.adressePersonnelLayout);
        editTextNumeroTelephonePersonnelLayout = findViewById(R.id.numeroTelephonePersonnelLayout);
        editTextSpecialisationPersonnelLayout = findViewById(R.id.specialisationPersonnelLayout);
        passwordPersonnelEditTextLayout = findViewById(R.id.passwordPersonnelLayout);
        confirmPasswordPersonnelEditTextLayout = findViewById(R.id.confirmPasswordPersonnelLayout);

// Convertissez les TextInputLayout en EditText
        EditText editTextNomPersonnel = editTextNomPersonnelLayout.getEditText();
        EditText editTextPostnomPersonnel = editTextPostnomPersonnelLayout.getEditText();
        EditText editTextPrenomPersonnel = editTextPrenomPersonnelLayout.getEditText();
        EditText editTextAdressePersonnel = editTextAdressePersonnelLayout.getEditText();
        EditText numeroTelephonePersonnel = editTextNumeroTelephonePersonnelLayout.getEditText();
        EditText editTextSpecialisationPersonnel = editTextSpecialisationPersonnelLayout.getEditText();
        EditText passwordPersonnelEditText = passwordPersonnelEditTextLayout.getEditText();
        EditText confirmPasswordPersonnelEditText = confirmPasswordPersonnelEditTextLayout.getEditText();
        Button buttonAddPersonnel = findViewById(R.id.buttonAddPersonnel);

        buttonAddPersonnel.setOnClickListener(view -> {
            // Valider les données
            if (validateInputs()) {
                // Insérer les données dans la base de données
                insertPersonnelIntoDatabase();
                saveCredentialsToPreferences(numeroTelephonePersonnel.toString(), passwordPersonnelEditText.toString());
                Intent intent = new Intent(CreateAcountActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                showToast(this, "Entrez toutes les données, et entrez les correctement.");
            }
        });
    }


    private void insertPersonnelIntoDatabase() {
        // Obtenez une instance de votre helper de base de données
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
        EditText editTextNomPersonnel = editTextNomPersonnelLayout.getEditText();
        EditText editTextPostnomPersonnel = editTextPostnomPersonnelLayout.getEditText();
        EditText editTextPrenomPersonnel = editTextPrenomPersonnelLayout.getEditText();
        EditText editTextAdressePersonnel = editTextAdressePersonnelLayout.getEditText();
        EditText editTextNumeroTelephonePersonnel = editTextNumeroTelephonePersonnelLayout.getEditText();
        EditText editTextSpecialisationPersonnel = editTextSpecialisationPersonnelLayout.getEditText();
        EditText passwordPersonnelEditText = passwordPersonnelEditTextLayout.getEditText();

        // Préparez les données à insérer
        ContentValues contentValues = new ContentValues();
        assert editTextNomPersonnel != null;
        contentValues.put("nom_personnel", editTextNomPersonnel.getText().toString());
        assert editTextPostnomPersonnel != null;
        contentValues.put("postnom_personnel", editTextPostnomPersonnel.getText().toString());
        assert editTextPrenomPersonnel != null;
        contentValues.put("prenom_personnel", editTextPrenomPersonnel.getText().toString());
        assert editTextAdressePersonnel != null;
        contentValues.put("adress_personnel", editTextAdressePersonnel.getText().toString());
        assert editTextNumeroTelephonePersonnel != null;
        contentValues.put("phone_number_personnel", editTextNumeroTelephonePersonnel.getText().toString());
        assert editTextSpecialisationPersonnel != null;
        contentValues.put("specialisation", editTextSpecialisationPersonnel.getText().toString());
        assert passwordPersonnelEditText != null;
        contentValues.put("password", passwordPersonnelEditText.getText().toString());

        // Insérez les données
        long newRowId = db.insert("personnel", null, contentValues);
        // Fermez la transaction
        db.close();

        // Gérez le résultat de l'insertion (par exemple, affichez un message)
    }

    private boolean validateInputs() {

// Convertir les TextInputLayout en String
        String nomPersonnel = editTextNomPersonnelLayout.getEditText().getText().toString().trim();
        String postnomPersonnel = editTextPostnomPersonnelLayout.getEditText().getText().toString().trim();
        String prenomPersonnel = editTextPrenomPersonnelLayout.getEditText().getText().toString().trim();
        String adressePersonnel = editTextAdressePersonnelLayout.getEditText().getText().toString().trim();
        String numeroTelephonePersonnel = editTextNumeroTelephonePersonnelLayout.getEditText().getText().toString().trim();
        String specialisationPersonnel = editTextSpecialisationPersonnelLayout.getEditText().getText().toString().trim();
        String passwordPersonnel = passwordPersonnelEditTextLayout.getEditText().getText().toString().trim();
        String confirmPasswordPersonnel = confirmPasswordPersonnelEditTextLayout.getEditText().getText().toString().trim();


        // Vérifiez que tous les champs requis sont remplis
        if (TextUtils.isEmpty(nomPersonnel) || TextUtils.isEmpty(postnomPersonnel) ||
                TextUtils.isEmpty(prenomPersonnel) || TextUtils.isEmpty(adressePersonnel) ||
                TextUtils.isEmpty(numeroTelephonePersonnel) || TextUtils.isEmpty(specialisationPersonnel) ||
                TextUtils.isEmpty(passwordPersonnel) || TextUtils.isEmpty(confirmPasswordPersonnel)) {
            showToast(this, "Veuillez remplir tous les champs.");
            return false;
        }

        // Vérifiez que le numéro de téléphone est unique (à faire après l'insertion dans la base de données)
        // Vérifiez que le mot de passe et la confirmation de mot de passe correspondent
        if (!passwordPersonnel.equals(confirmPasswordPersonnel)) {
            showToast(this, "Les mots de passe ne correspondent pas.");
            return false;
        }

        // Vérifiez que le numéro de téléphone est unique
        try (Cursor cursor = DatabaseHelper.getInstance(this).getReadableDatabase()
                .rawQuery("SELECT COUNT(*) AS count FROM personnel WHERE phone_number_personnel =?", new String[]{numeroTelephonePersonnel})) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                if (count > 0) {
                    showToast(this, "Le numéro de téléphone est déjà utilisé.");
                    return false;
                }
            }

        } catch (Exception e) {
            Log.e("Validation Error", "Erreur lors de la vérification de l'unicité du numéro de téléphone.", e);
            showToast(this, "Une erreur est survenue lors de la vérification du numéro de téléphone.");
            return false;
        }



        // Si aucune erreur n'a été rencontrée, continuez avec l'insertion

        return true;
    }

    private void saveCredentialsToPreferences(String phoneNumber, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("password", password);
        editor.apply();
    }

}
