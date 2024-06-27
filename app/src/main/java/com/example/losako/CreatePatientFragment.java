package com.example.losako;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.losako.models.DatabaseHelper;
import com.example.losako.models.Patient;
import com.google.android.material.textfield.TextInputLayout;

public class CreatePatientFragment extends Fragment {


    public CreatePatientFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_patient, container, false);


        // Récupérer les vues
        TextInputLayout editTextNomPatient = view.findViewById(R.id.nomPatientLayout);
        TextInputLayout editTextPostnomPatient = view.findViewById(R.id.postnomPatientLayout);
        TextInputLayout editTextPrenomPatient = view.findViewById(R.id.prenomPatientLayout);
        TextInputLayout editTextAdressePatient = view.findViewById(R.id.adressePatientLayout);
        TextInputLayout editTextNumeroTelephonePatient = view.findViewById(R.id.numeroTelephonePatientLayout);
        TextInputLayout editTextSpecialisationPatient = view.findViewById(R.id.specialisationPatientLayout);
        ImageView backToHome = view.findViewById(R.id.arrow_back_homeFragment);
        // Récupérer les valeurs des champs
        EditText nomPatient = editTextNomPatient.getEditText();
        EditText postnomPatient = editTextPostnomPatient.getEditText();
        EditText prenomPatient = editTextPrenomPatient.getEditText();
        EditText adressePatient = editTextAdressePatient.getEditText();
        EditText numeroTelephonePatient = editTextNumeroTelephonePatient.getEditText();
        EditText specialisationPatient = editTextSpecialisationPatient.getEditText();
        Button buttonAddPatient = view.findViewById(R.id.buttonAddPatient);
        backToHome.setOnClickListener(v -> returnToHomeFragment());
        buttonAddPatient.setOnClickListener(v -> {
            // Appeler la méthode pour ajouter le nouveau patient
            assert numeroTelephonePatient != null;
            assert specialisationPatient != null;
            assert postnomPatient != null;
            assert prenomPatient != null;
            assert nomPatient != null;
            assert adressePatient != null;
            addNewPatient(nomPatient.getText().toString(), postnomPatient.getText().toString(), prenomPatient.getText().toString(), adressePatient.getText().toString(), numeroTelephonePatient.getText().toString(), specialisationPatient.getText().toString());
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs. Nom patient: " + nomPatient.getText().toString(), Toast.LENGTH_SHORT).show();


            // Rafraîchissez les données des patients
//            returnToHomeFragment();
        });

        return view;
    }

    public void returnToHomeFragment() {
        // Navigation vers le fragment de détails et passage des données
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addNewPatient(String nom, String postnom, String prenom, String address, String phoneNumber, String maladie) {
        Patient newPatient = new Patient(nom, postnom, prenom, address, phoneNumber, maladie);
        DatabaseHelper.getInstance(requireContext()).addPatient(newPatient);
        DatabaseHelper.getInstance(requireContext()).refreshAllPatients();
    }
}