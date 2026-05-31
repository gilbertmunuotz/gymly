package com.gymly.ui.classes;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymly.R;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.BookClassRequest;
import com.gymly.data.model.ClassBookingData;
import com.gymly.data.model.GymClass;
import com.gymly.network.ApiClient;
import com.gymly.utils.ApiUtils;
import com.gymly.utils.UiUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassesFragment extends Fragment {

    private GymClassAdapter adapter;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private RecyclerView recyclerClasses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        textEmpty = view.findViewById(R.id.textEmpty);
        recyclerClasses = view.findViewById(R.id.recyclerClasses);

        adapter = new GymClassAdapter();
        adapter.setOnBookClickListener(this::confirmAndBook);
        recyclerClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerClasses.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClasses();
    }

    private void loadClasses() {
        setLoading(true);
        ApiClient.getClassService().getClasses().enqueue(new Callback<ApiResponse<List<GymClass>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GymClass>>> call,
                                   Response<ApiResponse<List<GymClass>>> response) {
                if (!isAdded()) {
                    return;
                }
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<GymClass> classes = response.body().getData();
                    adapter.setClasses(classes);
                    boolean empty = classes == null || classes.isEmpty();
                    textEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
                    recyclerClasses.setVisibility(empty ? View.GONE : View.VISIBLE);
                } else {
                    UiUtils.showError(requireView(),
                            ApiUtils.getErrorMessage(response, getString(R.string.error_load_classes)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GymClass>>> call, Throwable t) {
                if (isAdded()) {
                    setLoading(false);
                    UiUtils.showNetworkError(requireView(), requireContext());
                }
            }
        });
    }

    private void confirmAndBook(GymClass gymClass) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.book_class_dialog_title)
                .setMessage(getString(R.string.book_class_dialog_message, gymClass.getName()))
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_confirm_book, (dialog, which) -> bookClass(gymClass))
                .show();
    }

    private void bookClass(GymClass gymClass) {
        setLoading(true);
        ApiClient.getClassService()
                .bookClass(new BookClassRequest(gymClass.getId()))
                .enqueue(new Callback<ApiResponse<ClassBookingData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<ClassBookingData>> call,
                                           Response<ApiResponse<ClassBookingData>> response) {
                        if (!isAdded()) {
                            return;
                        }
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            showBookingSuccess(response.body().getData());
                            loadClasses();
                        } else {
                            UiUtils.showError(requireView(),
                                    ApiUtils.getErrorMessage(response, getString(R.string.error_book_class)));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<ClassBookingData>> call, Throwable t) {
                        if (isAdded()) {
                            setLoading(false);
                            UiUtils.showNetworkError(requireView(), requireContext());
                        }
                    }
                });
    }

    private void showBookingSuccess(ClassBookingData booking) {
        String day = booking.getDayOfWeek();
        String time = booking.getStartTime();
        if (time != null && time.length() >= 5) {
            time = time.substring(0, 5);
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.booking_success_title)
                .setMessage(getString(R.string.booking_success_message,
                        booking.getClassName(), day, time, booking.getLocation()))
                .setPositiveButton(R.string.btn_ok, null)
                .show();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) {
            recyclerClasses.setVisibility(View.GONE);
            textEmpty.setVisibility(View.GONE);
        }
    }
}
