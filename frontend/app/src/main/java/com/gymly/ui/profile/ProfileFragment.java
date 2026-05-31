package com.gymly.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gymly.R;
import com.gymly.data.local.SessionManager;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.MembershipData;
import com.gymly.data.model.UpdateProfileRequest;
import com.gymly.data.model.UserProfile;
import com.gymly.network.ApiClient;
import com.gymly.ui.auth.LoginActivity;
import com.gymly.utils.ApiUtils;
import com.gymly.utils.UiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ProgressBar progressBar;
    private TextInputEditText editFullName;
    private TextInputEditText editEmail;
    private TextInputEditText editPhone;
    private TextView textMembershipStatus;
    private MaterialButton btnSaveProfile;
    private MaterialButton btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        editFullName = view.findViewById(R.id.editFullName);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);
        textMembershipStatus = view.findViewById(R.id.textMembershipStatus);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnSaveProfile.setOnClickListener(v -> saveProfile());
        btnLogout.setOnClickListener(v -> confirmLogout());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
        loadMembershipStatus();
    }

    private void loadProfile() {
        setLoading(true);
        ApiClient.getProfileService().getProfile().enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call,
                                   Response<ApiResponse<UserProfile>> response) {
                if (!isAdded()) {
                    return;
                }
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    bindProfile(response.body().getData());
                } else {
                    UiUtils.showError(requireView(),
                            ApiUtils.getErrorMessage(response, getString(R.string.error_load_profile)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                if (isAdded()) {
                    setLoading(false);
                    UiUtils.showNetworkError(requireView(), requireContext());
                }
            }
        });
    }

    private void bindProfile(UserProfile profile) {
        editFullName.setText(profile.getFullName());
        editEmail.setText(profile.getEmail());
        editPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
    }

    private void loadMembershipStatus() {
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
                            textMembershipStatus.setText(getString(
                                    R.string.profile_active_membership,
                                    data.getPlanName(),
                                    data.getEndDate()));
                        } else {
                            textMembershipStatus.setText(R.string.profile_no_membership);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<MembershipData>> call, Throwable t) {
                        if (isAdded()) {
                            textMembershipStatus.setText(R.string.profile_no_membership);
                        }
                    }
                });
    }

    private void saveProfile() {
        String fullName = editFullName.getText() != null
                ? editFullName.getText().toString().trim() : "";
        String phone = editPhone.getText() != null
                ? editPhone.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            Toast.makeText(requireContext(), R.string.error_fill_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        ApiClient.getProfileService()
                .updateProfile(new UpdateProfileRequest(fullName, phone.isEmpty() ? null : phone))
                .enqueue(new Callback<ApiResponse<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserProfile>> call,
                                           Response<ApiResponse<UserProfile>> response) {
                        if (!isAdded()) {
                            return;
                        }
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            UserProfile profile = response.body().getData();
                            SessionManager.getInstance().updateUserName(profile.getFullName());
                            bindProfile(profile);
                            UiUtils.showSuccess(requireContext(), R.string.profile_saved);
                        } else {
                            UiUtils.showError(requireView(),
                                    ApiUtils.getErrorMessage(response, getString(R.string.error_save_profile)));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                        if (isAdded()) {
                            setLoading(false);
                            UiUtils.showNetworkError(requireView(), requireContext());
                        }
                    }
                });
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_message)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_logout, (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        SessionManager.getInstance().clearSession();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSaveProfile.setEnabled(!loading);
    }
}
