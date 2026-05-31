package com.gymly.ui.membership;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.MembershipPlan;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MembershipPlanAdapter extends RecyclerView.Adapter<MembershipPlanAdapter.PlanViewHolder> {

    public interface OnSubscribeClickListener {
        void onSubscribe(MembershipPlan plan);
    }

    private final List<MembershipPlan> plans = new ArrayList<>();
    private OnSubscribeClickListener listener;

    public void setPlans(List<MembershipPlan> plans) {
        this.plans.clear();
        if (plans != null) {
            this.plans.addAll(plans);
        }
        notifyDataSetChanged();
    }

    public void setOnSubscribeClickListener(OnSubscribeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membership_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        holder.bind(plans.get(position));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {

        private final TextView textPlanName;
        private final TextView textPlanDescription;
        private final TextView textPlanPrice;
        private final TextView textPlanDuration;
        private final MaterialButton btnSubscribe;

        PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlanName = itemView.findViewById(R.id.textPlanName);
            textPlanDescription = itemView.findViewById(R.id.textPlanDescription);
            textPlanPrice = itemView.findViewById(R.id.textPlanPrice);
            textPlanDuration = itemView.findViewById(R.id.textPlanDuration);
            btnSubscribe = itemView.findViewById(R.id.btnSubscribe);
        }

        void bind(MembershipPlan plan) {
            textPlanName.setText(plan.getName());
            textPlanDescription.setText(plan.getDescription());
            textPlanPrice.setText(itemView.getContext().getString(
                    R.string.membership_price_format, plan.getPrice()));
            textPlanDuration.setText(itemView.getContext().getString(
                    R.string.membership_duration_format, plan.getDurationDays()));
            btnSubscribe.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSubscribe(plan);
                }
            });
        }
    }
}
