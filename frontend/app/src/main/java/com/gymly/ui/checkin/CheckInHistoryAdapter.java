package com.gymly.ui.checkin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.AttendanceRecord;

import java.util.ArrayList;
import java.util.List;

public class CheckInHistoryAdapter extends RecyclerView.Adapter<CheckInHistoryAdapter.ViewHolder> {

    private final List<AttendanceRecord> records = new ArrayList<>();

    public void setRecords(List<AttendanceRecord> items) {
        records.clear();
        if (items != null) {
            records.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkin_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(records.get(position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textDateTime;
        private final TextView textType;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            textType = itemView.findViewById(R.id.textType);
        }

        void bind(AttendanceRecord record) {
            textDateTime.setText(formatDateTime(record.getCheckedInAt()));
            textType.setText(itemView.getContext().getString(
                    R.string.checkin_type_format, record.getCheckInType()));
        }

        private String formatDateTime(String iso) {
            if (iso == null || iso.length() < 16) {
                return iso != null ? iso : "";
            }
            return iso.substring(0, 16).replace('T', ' ');
        }
    }
}
