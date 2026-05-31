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
import com.gymly.data.model.LoginRequest;
import com.gymly.network.ApiClient;
import com.gymly.ui.MainActivity;
import com.gymly.utils.ApiUtils;
import com.gymly.utils.UiUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SessionManager.getInstance().isLoggedIn()) {
            goToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        TextView textRegister = findViewById(R.id.textRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        textRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = getText(editEmail);
        String password = getText(editPassword);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        LoginRequest request = new LoginRequest(email, password);
        ApiClient.getAuthService().login(request).enqueue(new Callback<ApiResponse<AuthData>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthData>> call, Response<ApiResponse<AuthData>> response) {
                setLoading(false);
                handleAuthResponse(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthData>> call, Throwable t) {
                setLoading(false);
                UiUtils.showNetworkError(findViewById(android.R.id.content), LoginActivity.this);
            }
        });
    }

    private void handleAuthResponse(Response<ApiResponse<AuthData>> response) {
        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
            AuthData data = response.body().getData();
            SessionManager.getInstance().saveSession(
                    data.getToken(), data.getUserId(), data.getFullName(), data.getEmail());
            UiUtils.showSuccess(this, R.string.login_success);
            goToMain();
        } else {
            UiUtils.showError(findViewById(android.R.id.content),
                    ApiUtils.getErrorMessage(response, getString(R.string.error_login_failed)));
        }
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
