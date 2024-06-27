package com.example.losako.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.losako.R;
import com.example.losako.models.VocalNote;

import java.io.IOException;
import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    private static final String LOG_TAG = "AudioPlayTest";
    private final ArrayList<VocalNote> notes;
    Context context;
    private int precPosition = 0;

    private AppCompatImageButton prevButton;
    private String fileName;
    private MediaPlayer mediaPlayer;
    private MediaPlayer player;
    private int currentPosition = 0;

    public DetailsAdapter(Context context, ArrayList<VocalNote> notes) {
        this.context = context;
        this.notes = notes;
    }

    private void startPlaying(String fileName, ViewHolder holder) {
        player = new MediaPlayer();
        try {
            player.setDataSource(this.fileName);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Libération des ressources à la fin de la lecture
                    stopPlaying();
                    holder.playAudioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_play_arrow_24, null)); // Réinitialiser l'icone pour la lecture
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    public void updateNotes(ArrayList<VocalNote> newNotes) {
        this.notes.clear();
        this.notes.addAll(newNotes);
        notifyDataSetChanged(); // Notifie le RecyclerView de la mise à jour
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocalNote note = notes.get(position);
        holder.author.setText(note.getAuthor());
        holder.dateSent.setText(note.getDateSent());
        holder.playAudioButton.setOnClickListener(v -> {
            fileName = note.getFilePath();
            if (player != null && this.precPosition == position) {
                if (player.isPlaying()) {
                    int currentPosition = player.getCurrentPosition();
                    player.pause();
                    holder.playAudioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_play_arrow_24, null));
                    this.currentPosition = currentPosition;
                } else {
                    resumePlaying(holder);
                }
            } else {
                if (player == null) {
                    startPlaying(fileName, holder);
                    holder.playAudioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_pause_24, context.getTheme()));
                    this.precPosition = position;
                    this.prevButton = holder.playAudioButton;

                } else {
                    stopPlaying();
                    this.prevButton.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_play_arrow_24, context.getTheme()));
                    startPlaying(fileName, holder);
                    holder.playAudioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_pause_24, context.getTheme()));
                }

            }
        });
    }

    private void resumePlaying(ViewHolder holder) {

        if (player != null && currentPosition > 0) {
            player.seekTo(currentPosition);
            player.start();
            holder.playAudioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_play_arrow_24, null)); // Mettre à jour l'icone pour indiquer la lecture
        }
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
