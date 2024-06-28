package com.example.losako;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.losako.adapters.DetailsAdapter;
import com.example.losako.adapters.PatientAdapter;
import com.example.losako.models.DatabaseHelper;
import com.example.losako.models.Patient;
import com.example.losako.models.Personnel;
import com.example.losako.models.VocalNote;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_CODE_RECORD_AUDIO = 100;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final String LOG_TAG = "AudioRecordTest";
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    Patient patient;
    private String fileName = null;
    private ArrayList<Patient> patients;
    private PatientAdapter.OnItemClickListener itemClickListener;
    private CustomFloatingActionButton fabRecord;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String audioFilePath;
    private DatabaseHelper dbHelper;
    private long startTime;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private DetailsAdapter detailsAdapter;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Patient patient) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedPatient", patient);
        fragment.setArguments(args);
        return fragment;
    }

    public static String generateAlphanumericFileName(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return sb.toString() + ".mp3"; // Ajoutez l'extension de fichier souhaitée
    }

    private void startRecording(String fileName) {
        try {
            this.fileName = fileName;
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(fileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        if (getArguments() != null) {
            patient = (Patient) getArguments().getSerializable("selectedPatient");
        }
        RecyclerView recyclerView = view.findViewById(R.id.audio_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CustomFloatingActionButton makeAudio = view.findViewById(R.id.make_audio);
        TextView tvFullName = view.findViewById(R.id.tvFullName);
        TextView tvMaladie = view.findViewById(R.id.tvMaladieFragment);
        TextView tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        TextView tvDateVenu = view.findViewById(R.id.tvDataeArrive);

        tvFullName.setText(patient.getFullName());
        tvMaladie.setText(patient.getMaladiePatient());
        tvPhoneNumber.setText(patient.getPhoneNumberPatient());
        tvDateVenu.setText(patient.getFormatedDateVenuPatient());

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(requireContext());
        ArrayList<VocalNote> vocalNotes = dbHelper.getVocalNotesByPatientId(patient.getIdPatient());
        final float initialScale = 1f;
        final float finalScale = 1.5f;

        ImageView button = view.findViewById(R.id.back_button);
        button.setOnClickListener(v -> {
            navigateToHomeFragment();
        });
        itemClickListener = (parent, position) -> {
            Patient selectedPatient = patients.get(position);
            Log.e("Listener", patients.toString());
            // Faites quelque chose avec le patient sélectionné, par exemple naviguer vers un autre fragment
            //  navigateToDetailsFragment(selectedPatient);
        };


        makeAudio.setOnTouchListener((v, event) -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LoginData", MODE_PRIVATE);
            String phoneNumber = sharedPreferences.getString("phoneNumber", "000000000");

            Personnel personnel = dbHelper.getPersonnelByPhoneNumber(phoneNumber);

            File filesDir = getActivity().getExternalFilesDir(null);
            File outpoutFile = new File(filesDir, personnel.getNomPersonnel()
                    + patient.getNomPatient() + generateAlphanumericFileName(8) + ".3gp");
            String fileName = outpoutFile.getAbsolutePath();
            detailsAdapter = new DetailsAdapter(requireContext(), vocalNotes);
            ArrayList<VocalNote> updatedNotes = dbHelper.getVocalNotesByPatientId(patient.getIdPatient());
            if (detailsAdapter != null) { // Vérification pour éviter NullPointerException
                detailsAdapter.updateNotes(updatedNotes);
            } else {
                Log.e("DetailsFragment", "DetailsAdapter is null");
            }
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                REQUEST_RECORD_AUDIO_PERMISSION);
                    } else {
                        // La permission a déjà été accordée, vous pouvez démarrer l'enregistrement immédiatement
                        startRecording(fileName);
                        dbHelper.insertAudioRecord(patient.getIdPatient(), (int) personnel.getIdPersonnel(), fileName, personnel.getNomPersonnel());

                    }
                    ObjectAnimator scaleUpAnimatorX = ObjectAnimator.ofFloat(v, "scaleX", finalScale);
                    ObjectAnimator scaleUpAnimatorY = ObjectAnimator.ofFloat(v, "scaleY", finalScale);
                    scaleUpAnimatorX.setDuration(200); // Durée de l'animation
                    scaleUpAnimatorY.setDuration(200); // Durée de l'animation
                    scaleUpAnimatorY.start();
                    scaleUpAnimatorX.start();
                    scaleUpAnimatorY.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // Ajoutez ici toute logique supplémentaire à exécuter après l'animation
                        }
                    });
                    scaleUpAnimatorX.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // Ajoutez ici toute logique supplémentaire à exécuter après l'animation
                        }
                    });
                    v.performClick();


                    break;
                case MotionEvent.ACTION_UP:


                    // Arrêtez la tâche
                    // onRecord(false, fileName);
                    ObjectAnimator scaleDownAnimator = ObjectAnimator.ofFloat(v, "scaleX", initialScale);
                    ObjectAnimator scaleDownAnimatorY = ObjectAnimator.ofFloat(v, "scaleY", initialScale);
                    scaleDownAnimator.setDuration(200);
                    scaleDownAnimatorY.setDuration(200);
                    scaleDownAnimator.start();
                    scaleDownAnimatorY.start();

                    break;
            }
            return true;
        });
        DetailsAdapter adapter = new DetailsAdapter(requireContext(), vocalNotes);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void navigateToHomeFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission a été accordée, vous pouvez démarrer l'enregistrement
                    startRecording(fileName);
                } else {
                    // La permission a été refusée, informez l'utilisateur
                    Toast.makeText(getActivity(), "La permission d'enregistrement audio est nécessaire pour enregistrer.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
