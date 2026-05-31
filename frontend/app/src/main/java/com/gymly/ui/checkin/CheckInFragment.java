package com.gymly.ui.checkin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.AttendanceRecord;
import com.gymly.data.model.CheckInRequest;
import com.gymly.network.ApiClient;
import com.gymly.utils.ApiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView textHistoryEmpty;
    private RecyclerView recyclerHistory;
    private CheckInHistoryAdapter adapter;
    private MaterialButton btnScanQr;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        textHistoryEmpty = view.findViewById(R.id.textHistoryEmpty);
        recyclerHistory = view.findViewById(R.id.recyclerHistory);
        btnScanQr = view.findViewById(R.id.btnScanQr);

        adapter = new CheckInHistoryAdapter();
        recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerHistory.setAdapter(adapter);

        btnScanQr.setOnClickListener(v -> simulateQrScanAndCheckIn());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    private void simulateQrScanAndCheckIn() {
        btnScanQr.setEnabled(false);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.qr_scan_title)
                .setMessage(R.string.qr_scan_message)
                .setCancelable(false);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        handler.postDelayed(() -> {
            if (!isAdded()) {
                return;
            }
            dialog.dismiss();
            performCheckIn();
        }, 1500);
    }

    private void performCheckIn() {
        setLoading(true);
        ApiClient.getCheckInService()
                .checkIn(new CheckInRequest("GYM"))
                .enqueue(new Callback<ApiResponse<AttendanceRecord>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AttendanceRecord>> call,
                                           Response<ApiResponse<AttendanceRecord>> response) {
                        if (!isAdded()) {
                            return;
                        }
                        setLoading(false);
                        btnScanQr.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            showCheckInSuccess(response.body().getData());
                            loadHistory();
                        } else {
                            Toast.makeText(requireContext(),
                                    ApiUtils.getErrorMessage(response, getString(R.string.error_checkin)),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AttendanceRecord>> call, Throwable t) {
                        if (isAdded()) {
                            setLoading(false);
                            btnScanQr.setEnabled(true);
                            Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showCheckInSuccess(AttendanceRecord record) {
        String time = record.getCheckedInAt();
        if (time != null && time.length() >= 16) {
            time = time.substring(0, 16).replace('T', ' ');
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.checkin_success_title)
                .setMessage(getString(R.string.checkin_success_message, time))
                .setPositiveButton(R.string.btn_ok, null)
                .show();
    }

    private void loadHistory() {
        setLoading(true);
        ApiClient.getCheckInService().getHistory().enqueue(new Callback<ApiResponse<List<AttendanceRecord>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AttendanceRecord>>> call,
                                   Response<ApiResponse<List<AttendanceRecord>>> response) {
                if (!isAdded()) {
                    return;
                }
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<AttendanceRecord> records = response.body().getData();
                    adapter.setRecords(records);
                    boolean empty = records == null || records.isEmpty();
                    textHistoryEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(requireContext(),
                            ApiUtils.getErrorMessage(response, getString(R.string.error_load_checkin_history)),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AttendanceRecord>>> call, Throwable t) {
                if (isAdded()) {
                    setLoading(false);
                    Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
