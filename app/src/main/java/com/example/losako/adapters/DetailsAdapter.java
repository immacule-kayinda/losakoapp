package com.example.losako.adapters;

import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.losako.R;
import com.example.losako.models.PatientWithNotes;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    AppCompatImageButton playAudioButton;
    private List<PatientWithNotes> notes;
    private MediaPlayer mediaPlayer;


    public DetailsAdapter(List<PatientWithNotes> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientWithNotes note = notes.get(position);
        holder.author.setText(note.getAuthor());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateSent = sdf.format(note.getDateSent());
        holder.dateSent.setText(dateSent);
        holder.playAudioButton.setOnClickListener(v -> {
            playAudio(note.getFilePath());
        });
    }

    private void playAudio(String audioFilePath) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, dateSent;
        AppCompatImageButton playAudioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            dateSent = itemView.findViewById(R.id.datesended);
            // Ajoutez un écouteur de clic sur le bouton de lecture ici
            playAudioButton = itemView.findViewById(R.id.playAudio);
        }
    }
}
