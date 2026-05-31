package com.gymly.ui.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.GymClass;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GymClassAdapter extends RecyclerView.Adapter<GymClassAdapter.ClassViewHolder> {

    public interface OnBookClickListener {
        void onBook(GymClass gymClass);
    }

    private static final Map<String, String> DAY_LABELS = new HashMap<>();

    static {
        DAY_LABELS.put("MON", "Monday");
        DAY_LABELS.put("TUE", "Tuesday");
        DAY_LABELS.put("WED", "Wednesday");
        DAY_LABELS.put("THU", "Thursday");
        DAY_LABELS.put("FRI", "Friday");
        DAY_LABELS.put("SAT", "Saturday");
        DAY_LABELS.put("SUN", "Sunday");
    }

    private final List<GymClass> classes = new ArrayList<>();
    private OnBookClickListener listener;

    public void setClasses(List<GymClass> classes) {
        this.classes.clear();
        if (classes != null) {
            this.classes.addAll(classes);
        }
        notifyDataSetChanged();
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gym_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.bind(classes.get(position));
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {

        private final TextView textClassName;
        private final TextView textClassDescription;
        private final TextView textClassSchedule;
        private final TextView textClassTrainer;
        private final TextView textClassLocation;
        private final TextView textClassSpots;
        private final MaterialButton btnBook;

        ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            textClassName = itemView.findViewById(R.id.textClassName);
            textClassDescription = itemView.findViewById(R.id.textClassDescription);
            textClassSchedule = itemView.findViewById(R.id.textClassSchedule);
            textClassTrainer = itemView.findViewById(R.id.textClassTrainer);
            textClassLocation = itemView.findViewById(R.id.textClassLocation);
            textClassSpots = itemView.findViewById(R.id.textClassSpots);
            btnBook = itemView.findViewById(R.id.btnBook);
        }

        void bind(GymClass gymClass) {
            textClassName.setText(gymClass.getName());
            textClassDescription.setText(gymClass.getDescription());

            String day = DAY_LABELS.getOrDefault(gymClass.getDayOfWeek(), gymClass.getDayOfWeek());
            String startTime = formatTime(gymClass.getStartTime());
            String endTime = formatTime(gymClass.getEndTime());
            textClassSchedule.setText(itemView.getContext().getString(
                    R.string.class_schedule_format, day, startTime, endTime));

            textClassTrainer.setText(itemView.getContext().getString(
                    R.string.class_trainer_format, gymClass.getTrainerName()));
            textClassLocation.setText(itemView.getContext().getString(
                    R.string.class_location_format, gymClass.getLocation()));

            textClassSpots.setText(itemView.getContext().getString(
                    R.string.class_spots_format,
                    gymClass.getSpotsRemaining(),
                    gymClass.getMaxCapacity()));

            if (gymClass.isBookedByUser()) {
                btnBook.setText(R.string.btn_already_booked);
                btnBook.setEnabled(false);
            } else if (gymClass.getSpotsRemaining() <= 0) {
                btnBook.setText(R.string.btn_class_full);
                btnBook.setEnabled(false);
            } else {
                btnBook.setText(R.string.btn_book_class);
                btnBook.setEnabled(true);
                btnBook.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onBook(gymClass);
                    }
                });
            }
        }

        private String formatTime(String time) {
            if (time == null || time.length() < 5) {
                return time;
            }
            return time.substring(0, 5);
        }
    }
}
