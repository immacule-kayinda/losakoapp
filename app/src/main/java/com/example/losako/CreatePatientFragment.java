package com.example.losako;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.losako.models.DatabaseHelper;
import com.example.losako.models.Patient;

public class CreatePatientFragment extends Fragment {


    public CreatePatientFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_patient, container, false);


        // Récupérer les vues
        EditText editTextNomPatient = view.findViewById(R.id.editTextNomPatient);
        EditText editTextPostnomPatient = view.findViewById(R.id.editTextPostnomPatient);
        EditText editTextPrenomPatient = view.findViewById(R.id.editTextPrenomPatient);
        EditText editTextAdressePatient = view.findViewById(R.id.editTextAdressePatient);
        EditText editTextNumeroTelephonePatient = view.findViewById(R.id.editTextNumeroTelephonePatient);
        EditText editTextSpecialisationPatient = view.findViewById(R.id.editTextSpecialisationPatient);
        ImageView backToHome = view.findViewById(R.id.arrow_back_homeFragment);
        // Récupérer les valeurs des champs
        String nomPatient = editTextNomPatient.getText().toString();
        String postnomPatient = editTextPostnomPatient.getText().toString();
        String prenomPatient = editTextPrenomPatient.getText().toString();
        String adressePatient = editTextAdressePatient.getText().toString();
        String numeroTelephonePatient = editTextNumeroTelephonePatient.getText().toString();
        String specialisationPatient = editTextSpecialisationPatient.getText().toString();
        Button buttonAddPatient = view.findViewById(R.id.buttonAddPatient);
        backToHome.setOnClickListener(v -> returnToHomeFragment());
        buttonAddPatient.setOnClickListener(v -> {
            // Appeler la méthode pour ajouter le nouveau patient
            addNewPatient(nomPatient, postnomPatient, prenomPatient, adressePatient, numeroTelephonePatient, specialisationPatient);
            returnToHomeFragment();
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
    }
}