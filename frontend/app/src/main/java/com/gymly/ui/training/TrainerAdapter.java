package com.gymly.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.Trainer;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder> {

    public interface OnBookClickListener {
        void onBook(Trainer trainer);
    }

    private final List<Trainer> trainers = new ArrayList<>();
    private OnBookClickListener listener;

    public void setTrainers(List<Trainer> trainers) {
        this.trainers.clear();
        if (trainers != null) {
            this.trainers.addAll(trainers);
        }
        notifyDataSetChanged();
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainer, parent, false);
        return new TrainerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerViewHolder holder, int position) {
        holder.bind(trainers.get(position));
    }

    @Override
    public int getItemCount() {
        return trainers.size();
    }

    class TrainerViewHolder extends RecyclerView.ViewHolder {

        private final TextView textTrainerName;
        private final TextView textTrainerSpecialty;
        private final TextView textTrainerBio;
        private final MaterialButton btnBookSession;

        TrainerViewHolder(@NonNull View itemView) {
            super(itemView);
            textTrainerName = itemView.findViewById(R.id.textTrainerName);
            textTrainerSpecialty = itemView.findViewById(R.id.textTrainerSpecialty);
            textTrainerBio = itemView.findViewById(R.id.textTrainerBio);
            btnBookSession = itemView.findViewById(R.id.btnBookSession);
        }

        void bind(Trainer trainer) {
            textTrainerName.setText(trainer.getFullName());
            textTrainerSpecialty.setText(trainer.getSpecialty());
            textTrainerBio.setText(trainer.getBio());
            btnBookSession.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBook(trainer);
                }
            });
        }
    }
}
