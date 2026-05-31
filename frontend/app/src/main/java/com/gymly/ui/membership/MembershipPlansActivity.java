package com.gymly.ui.membership;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.MembershipData;
import com.gymly.data.model.MembershipPlan;
import com.gymly.data.model.SubscribeRequest;
import com.gymly.network.ApiClient;
import com.gymly.utils.ApiUtils;
import com.gymly.utils.UiUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembershipPlansActivity extends AppCompatActivity {

    private static final long MPESA_SIMULATION_DELAY_MS = 1500;

    private MembershipPlanAdapter adapter;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private RecyclerView recyclerPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_plans);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar);
        textEmpty = findViewById(R.id.textEmpty);
        recyclerPlans = findViewById(R.id.recyclerPlans);

        adapter = new MembershipPlanAdapter();
        adapter.setOnSubscribeClickListener(this::showMpesaDialog);
        recyclerPlans.setLayoutManager(new LinearLayoutManager(this));
        recyclerPlans.setAdapter(adapter);

        loadPlans();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadPlans() {
        setLoading(true);
        ApiClient.getMembershipService().getPlans().enqueue(new Callback<ApiResponse<List<MembershipPlan>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MembershipPlan>>> call,
                                   Response<ApiResponse<List<MembershipPlan>>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<MembershipPlan> plans = response.body().getData();
                    adapter.setPlans(plans);
                    textEmpty.setVisibility(plans == null || plans.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    UiUtils.showError(findViewById(android.R.id.content),
                            ApiUtils.getErrorMessage(response, getString(R.string.error_load_plans)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<MembershipPlan>>> call, Throwable t) {
                setLoading(false);
                UiUtils.showNetworkError(findViewById(android.R.id.content), MembershipPlansActivity.this);
            }
        });
    }

    private void showMpesaDialog(MembershipPlan plan) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.mpesa_dialog_title)
                .setMessage(getString(R.string.mpesa_dialog_message, plan.getPrice(), plan.getName()))
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_pay_mpesa, (dialog, which) -> simulateMpesaPayment(plan))
                .show();
    }

    private void simulateMpesaPayment(MembershipPlan plan) {
        AlertDialog loadingDialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.mpesa_processing_title)
                .setMessage(R.string.mpesa_processing_message)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadingDialog.dismiss();
            subscribeToPlan(plan);
        }, MPESA_SIMULATION_DELAY_MS);
    }

    private void subscribeToPlan(MembershipPlan plan) {
        setLoading(true);
        ApiClient.getMembershipService()
                .subscribe(new SubscribeRequest(plan.getId()))
                .enqueue(new Callback<ApiResponse<MembershipData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<MembershipData>> call,
                                           Response<ApiResponse<MembershipData>> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            showSuccessDialog(response.body().getData());
                        } else {
                            UiUtils.showError(findViewById(android.R.id.content),
                                    ApiUtils.getErrorMessage(response, getString(R.string.error_subscribe)));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<MembershipData>> call, Throwable t) {
                        setLoading(false);
                        UiUtils.showNetworkError(findViewById(android.R.id.content), MembershipPlansActivity.this);
                    }
                });
    }

    private void showSuccessDialog(MembershipData membership) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.mpesa_success_title)
                .setMessage(getString(R.string.mpesa_success_message,
                        membership.getPlanName(), membership.getEndDate()))
                .setPositiveButton(R.string.btn_ok, (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
