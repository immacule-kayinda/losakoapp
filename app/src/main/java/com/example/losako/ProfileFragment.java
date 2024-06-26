package com.example.losako;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.losako.databinding.FragmentProfileBinding;
import com.example.losako.models.DatabaseHelper;
import com.example.losako.models.Personnel;

public class ProfileFragment extends Fragment {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private FragmentProfileBinding binding;
    private int pressCount = 0;
    private long lastPressTime = 0;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle arguments if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        String sharedPhoneNumber = sharedPreferences.getString("phoneNumber", "0897481511");

        // Initialize dbHelper here
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(requireContext());
        Personnel personnel = dbHelper.getPersonnelByPhoneNumber(sharedPhoneNumber);


        TextView nomPersonnel = binding.nomPersonnel;
        String fullName = personnel.getPrenomPersonnel() + " " + personnel.getNomPersonnel();
        nomPersonnel.setText(fullName);
        TextView specialisation = binding.specialisation;
        specialisation.setText(personnel.getSpecialisationPersonnel());
        TextView adress = binding.adressePersonnel;
        TextView phoneNumber = binding.phoneNumber;
        TextView password = binding.password;
        adress.setText(personnel.getAdressPersonel());
        phoneNumber.setText(personnel.getPhoneNumberPersonnel());
        StringBuilder hashPassword = new StringBuilder();
        for (int i = 0; i < personnel.getPasswordPersonnel().length(); i++) {
            hashPassword.append("*");
        }
        password.setText(hashPassword.toString());
        Button logout = binding.logout;
        logout.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(requireActivity(), LoginActivity.class); // Remplacez par le nom de votre activité de destination
            startActivity(intent);
            requireActivity().finish();
        });


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPressTime > 1000) {
                    pressCount++;
                    if (pressCount == 2) {
                        requireActivity().finish();
                        pressCount = 0;
                    } else {
                        pressCount = 0;
                    }
                }
                lastPressTime = currentTime;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
