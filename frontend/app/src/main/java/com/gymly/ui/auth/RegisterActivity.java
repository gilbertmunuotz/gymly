package com.gymly.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gymly.R;
import com.gymly.data.local.SessionManager;
import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.AuthData;
import com.gymly.data.model.RegisterRequest;
import com.gymly.network.ApiClient;
import com.gymly.ui.MainActivity;
import com.gymly.utils.ApiUtils;
import com.gymly.utils.UiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editFullName;
    private TextInputEditText editEmail;
    private TextInputEditText editPhone;
    private TextInputEditText editPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        TextView textLogin = findViewById(R.id.textLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        textLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String fullName = getText(editFullName);
        String email = getText(editEmail);
        String phone = getText(editPhone);
        String password = getText(editPassword);

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_fill_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, R.string.error_password_length, Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        RegisterRequest request = new RegisterRequest(fullName, email, password, phone.isEmpty() ? null : phone);
        ApiClient.getAuthService().register(request).enqueue(new Callback<ApiResponse<AuthData>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthData>> call, Response<ApiResponse<AuthData>> response) {
                setLoading(false);
                handleAuthResponse(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthData>> call, Throwable t) {
                setLoading(false);
                UiUtils.showNetworkError(findViewById(android.R.id.content), RegisterActivity.this);
            }
        });
    }

    private void handleAuthResponse(Response<ApiResponse<AuthData>> response) {
        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
            AuthData data = response.body().getData();
            SessionManager.getInstance().saveSession(
                    data.getToken(), data.getUserId(), data.getFullName(), data.getEmail());
            UiUtils.showSuccess(this, R.string.register_success);
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        } else {
            UiUtils.showError(findViewById(android.R.id.content),
                    ApiUtils.getErrorMessage(response, getString(R.string.error_register_failed)));
        }
    }

    private void setLoading(boolean loading) {
        btnRegister.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
