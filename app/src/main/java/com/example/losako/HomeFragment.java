package com.example.losako;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.losako.adapters.PatientAdapter;
import com.example.losako.models.DatabaseHelper;
import com.example.losako.models.Patient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private ArrayList<Patient> patients;
    private PatientAdapter.OnItemClickListener itemClickListener;
    private RecyclerView recyclerViewPatients;
    PatientAdapter patientAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewPatients = view.findViewById(R.id.recycler_view_patients);
        recyclerViewPatients.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }
    DatabaseHelper dbHelper;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.dbHelper = DatabaseHelper.getInstance(requireContext());

        FloatingActionButton floatingActionButton = view.findViewById(R.id.create_patient);
        TextInputLayout searchInputLayout = view.findViewById(R.id.search_edit_text);
        EditText searchEditText = searchInputLayout.getEditText();
        // Configuration initiale du RecyclerView
        patients = dbHelper.getAllPatients();

        // Ajoutez un écouteur d'événements pour le champ de recherche
        assert searchEditText != null;
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPatients(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Implémentez l'interface AdapterView.OnItemClickListener
        itemClickListener = (parent, position) -> {
            Patient selectedPatient = patients.get(position);
            Log.e("Listener", patients.toString());
            // Faites quelque chose avec le patient sélectionné, par exemple naviguer vers un autre fragment
            navigateToDetailsFragment(selectedPatient);
        };
        floatingActionButton.setOnClickListener(v -> navigateToCreatePatient());
        patientAdapter = new PatientAdapter(requireContext(), patients, itemClickListener);
        recyclerViewPatients.setAdapter(patientAdapter);

        // Centralisez l'observation des données ici
        dbHelper.getPatientsLiveData().observe(getViewLifecycleOwner(), newPatients -> requireActivity().runOnUiThread(() -> {
            // Mettez à jour les données de l'adaptateur existant plutôt que de créer un nouveau
            patientAdapter.setFilteredData(newPatients);
        }));
    }
    private void filterPatients(String searchText) {
        ArrayList<Patient> filteredPatients = new ArrayList<>();
        ArrayList<Patient> patients = dbHelper.getAllPatients();
        for (Patient patient : patients) {
            if (patient.getNomPatient().toLowerCase().contains(searchText.toLowerCase())) {
                filteredPatients.add(patient);
            }
        }
        ((PatientAdapter) Objects.requireNonNull(recyclerViewPatients.getAdapter())).setFilteredData(filteredPatients);
    }

    private void navigateToCreatePatient() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        CreatePatientFragment createPatientFragment = new CreatePatientFragment();
        transaction.replace(R.id.fragment_container, createPatientFragment);
        transaction.commit();
    }


    private int pressCount = 0;
    private long lastPressTime = 0;


    private void navigateToDetailsFragment(Patient selectedPatient) {
        // Navigation vers le fragment de détails et passage des données
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedPatient", selectedPatient);
        detailsFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
