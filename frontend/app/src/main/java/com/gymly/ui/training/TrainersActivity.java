package com.gymly.ui.training;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.BookPtRequest;
import com.gymly.data.model.PtBookingData;
import com.gymly.data.model.Trainer;
import com.gymly.network.ApiClient;
import com.gymly.utils.ApiUtils;
import com.gymly.utils.UiUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainersActivity extends AppCompatActivity {

    private TrainerAdapter adapter;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private RecyclerView recyclerTrainers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar);
        textEmpty = findViewById(R.id.textEmpty);
        recyclerTrainers = findViewById(R.id.recyclerTrainers);

        adapter = new TrainerAdapter();
        adapter.setOnBookClickListener(this::showBookingDialog);
        recyclerTrainers.setLayoutManager(new LinearLayoutManager(this));
        recyclerTrainers.setAdapter(adapter);

        loadTrainers();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadTrainers() {
        setLoading(true);
        ApiClient.getTrainerService().getTrainers().enqueue(new Callback<ApiResponse<List<Trainer>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Trainer>>> call,
                                   Response<ApiResponse<List<Trainer>>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Trainer> trainers = response.body().getData();
                    adapter.setTrainers(trainers);
                    textEmpty.setVisibility(trainers == null || trainers.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    UiUtils.showError(findViewById(android.R.id.content),
                            ApiUtils.getErrorMessage(response, getString(R.string.error_load_trainers)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Trainer>>> call, Throwable t) {
                setLoading(false);
                UiUtils.showNetworkError(findViewById(android.R.id.content), TrainersActivity.this);
            }
        });
    }

    private void showBookingDialog(Trainer trainer) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book_pt, null);
        TextView textDialogTrainerName = dialogView.findViewById(R.id.textDialogTrainerName);
        MaterialButton btnPickDate = dialogView.findViewById(R.id.btnPickDate);
        MaterialButton btnPickTime = dialogView.findViewById(R.id.btnPickTime);
        TextInputEditText editNotes = dialogView.findViewById(R.id.editNotes);

        textDialogTrainerName.setText(trainer.getFullName());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);

        final int[] year = {calendar.get(Calendar.YEAR)};
        final int[] month = {calendar.get(Calendar.MONTH)};
        final int[] day = {calendar.get(Calendar.DAY_OF_MONTH)};
        final int[] hour = {calendar.get(Calendar.HOUR_OF_DAY)};
        final int[] minute = {calendar.get(Calendar.MINUTE)};

        updateDateButton(btnPickDate, year[0], month[0], day[0]);
        updateTimeButton(btnPickTime, hour[0], minute[0]);

        btnPickDate.setOnClickListener(v -> new DatePickerDialog(this,
                (view, y, m, d) -> {
                    year[0] = y;
                    month[0] = m;
                    day[0] = d;
                    updateDateButton(btnPickDate, y, m, d);
                }, year[0], month[0], day[0]).show());

        btnPickTime.setOnClickListener(v -> new TimePickerDialog(this,
                (view, h, min) -> {
                    hour[0] = h;
                    minute[0] = min;
                    updateTimeButton(btnPickTime, h, min);
                }, hour[0], minute[0], true).show());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.book_pt_dialog_title)
                .setView(dialogView)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_confirm_book, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String notes = editNotes.getText() != null ? editNotes.getText().toString().trim() : "";
            String sessionDate = String.format(Locale.US, "%04d-%02d-%02d", year[0], month[0] + 1, day[0]);
            String startTime = String.format(Locale.US, "%02d:%02d:00", hour[0], minute[0]);
            dialog.dismiss();
            bookSession(trainer, sessionDate, startTime, notes.isEmpty() ? null : notes);
        }));

        dialog.show();
    }

    private void updateDateButton(MaterialButton button, int year, int month, int day) {
        button.setText(getString(R.string.pt_date_format, day, month + 1, year));
    }

    private void updateTimeButton(MaterialButton button, int hour, int minute) {
        button.setText(getString(R.string.pt_time_format, hour, minute));
    }

    private void bookSession(Trainer trainer, String sessionDate, String startTime, String notes) {
        setLoading(true);
        ApiClient.getTrainerService()
                .bookSession(new BookPtRequest(trainer.getId(), sessionDate, startTime, notes))
                .enqueue(new Callback<ApiResponse<PtBookingData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PtBookingData>> call,
                                           Response<ApiResponse<PtBookingData>> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            showSuccessDialog(response.body().getData());
                        } else {
                            UiUtils.showError(findViewById(android.R.id.content),
                                    ApiUtils.getErrorMessage(response, getString(R.string.error_book_pt)));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PtBookingData>> call, Throwable t) {
                        setLoading(false);
                        UiUtils.showNetworkError(findViewById(android.R.id.content), TrainersActivity.this);
                    }
                });
    }

    private void showSuccessDialog(PtBookingData booking) {
        String time = booking.getStartTime();
        if (time != null && time.length() >= 5) {
            time = time.substring(0, 5);
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.pt_booking_success_title)
                .setMessage(getString(R.string.pt_booking_success_message,
                        booking.getTrainerName(),
                        booking.getSessionDate(),
                        time,
                        booking.getStatus()))
                .setPositiveButton(R.string.btn_ok, null)
                .show();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
