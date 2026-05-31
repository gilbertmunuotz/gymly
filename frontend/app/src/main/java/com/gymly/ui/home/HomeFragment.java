package com.gymly.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gymly.R;
import com.gymly.data.local.SessionManager;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.MembershipData;
import com.gymly.network.ApiClient;
import com.gymly.ui.membership.MembershipPlansActivity;
import com.gymly.ui.training.TrainersActivity;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView textMembershipStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textWelcome = view.findViewById(R.id.textWelcome);
        textWelcome.setText(getString(R.string.welcome_user,
                SessionManager.getInstance().getUserName()));

        textMembershipStatus = view.findViewById(R.id.textMembershipStatus);
        MaterialButton btnViewPlans = view.findViewById(R.id.btnViewPlans);
        btnViewPlans.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), MembershipPlansActivity.class)));

        MaterialButton btnBookTraining = view.findViewById(R.id.btnBookTraining);
        btnBookTraining.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), TrainersActivity.class)));

        loadActiveMembership();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadActiveMembership();
    }

    private void loadActiveMembership() {
        ApiClient.getMembershipService().getActiveMembership()
                .enqueue(new Callback<ApiResponse<MembershipData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<MembershipData>> call,
                                           Response<ApiResponse<MembershipData>> response) {
                        if (!isAdded()) {
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            MembershipData data = response.body().getData();
                            textMembershipStatus.setVisibility(View.VISIBLE);
                            textMembershipStatus.setText(getString(
                                    R.string.home_active_membership,
                                    data.getPlanName(),
                                    data.getEndDate()));
                        } else {
                            textMembershipStatus.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<MembershipData>> call, Throwable t) {
                        if (isAdded()) {
                            textMembershipStatus.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
