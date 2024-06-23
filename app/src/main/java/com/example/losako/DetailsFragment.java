package com.example.losako;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.losako.adapters.DetailsAdapter;
import com.example.losako.models.DatabaseHelper;
import com.example.losako.models.Patient;
import com.example.losako.models.PatientWithNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    Patient patient;
    boolean isRecording = false;
    private CustomFloatingActionButton fabRecord;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private DatabaseHelper dbHelper;

    private long startTime;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        int patientId = -1;
        if (getArguments() != null) {
            patient = (Patient) getArguments().getSerializable("selectedPatient");
        }
        RecyclerView recyclerView = view.findViewById(R.id.audio_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CustomFloatingActionButton makeAudio = view.findViewById(R.id.make_audio);
        TextView tvNom = view.findViewById(R.id.tvLastName);
        TextView tvPrenom = view.findViewById(R.id.tvFirstName);
        TextView tvPostNom = view.findViewById(R.id.tvMiddleName);



        tvNom.setText(patient.getNomPatient());
        tvPostNom.setText(patient.getPostnomPatient());
        tvPrenom.setText(patient.getPrenomPatient());

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(requireContext());
        Map<Integer, PatientWithNotes> patientWithNotesMap = dbHelper.getPatientWithNotes(patient.getIdPatient());
        final float initialScale = 1f; // Taille initiale
        final float finalScale = 1.5f; // Taille finale (plus grande)

        makeAudio.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Commencez la tâche
                    startRecording();
                    ObjectAnimator scaleUpAnimator = ObjectAnimator.ofFloat(v, "scaleX", finalScale);
                    scaleUpAnimator.setDuration(200); // Durée de l'animation
                    scaleUpAnimator.start();
                    scaleUpAnimator.addListener(new AnimatorListenerAdapter() {
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
                    stopRecording();
                    ObjectAnimator scaleDownAnimator = ObjectAnimator.ofFloat(v, "scaleX", initialScale);
                    scaleDownAnimator.setDuration(200); // Durée de l'animation
                    scaleDownAnimator.start();
                    break;
            }
            return true;
        });
        DetailsAdapter adapter = new DetailsAdapter((List<PatientWithNotes>) patientWithNotesMap.values());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void playAudio(String audioFilePath) {
        try {
            // Libérer toute ressource média précédemment allouée
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            // Initialiser le MediaPlayer avec le chemin du fichier audio
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare(); // Préparer le MediaPlayer avant de démarrer la lecture
            mediaPlayer.start(); // Démarrer la lecture de l'audio

            // Gestion des erreurs
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // Obtenez le répertoire de fichiers externes de l'application pour le stockage sûr
            File directory = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (directory != null) {
                audioFilePath = directory.getAbsolutePath() + "/myaudio.3gp"; // Construisez le chemin complet du fichier
            } else {
                throw new IOException("Impossible d'accéder au répertoire de fichiers externes de l'application.");
            }

            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            startTime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LoginData", MODE_PRIVATE);
            String author = sharedPreferences.getString("nomPersonnel", "Doctor");

            dbHelper.insertAudioRecord(patient.getIdPatient(), 1, audioFilePath, author);
            // Save the audio file path in the database


            // Optionally, log the duration of the recording
            Log.d("RecordingDuration", "Duration: " + duration);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) { // Vérifiez le code de requête
            boolean permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (permissionGranted) {
                // Les permissions ont été accordées, vous pouvez maintenant enregistrer l'audio
                // Par exemple, vous pouvez mettre à jour l'interface utilisateur pour indiquer que l'utilisateur peut commencer à enregistrer
                Toast.makeText(getActivity(), "Permissions accordées, vous pouvez maintenant enregistrer.", Toast.LENGTH_LONG).show();
            } else {
                // Les permissions ont été refusées
                // Informez l'utilisateur ou gérez le cas où l'enregistrement n'est pas possible
                Toast.makeText(getActivity(), "Permissions refusées, l'enregistrement n'est pas possible.", Toast.LENGTH_LONG).show();
            }
        }
    }

}