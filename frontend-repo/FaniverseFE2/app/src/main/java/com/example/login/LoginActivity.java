package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.api.ApiService;
import com.example.login.model.LoginRequestDto;
import com.example.login.model.LoginResponseDto;
import com.example.login.network.RetrofitClient;

import kotlinx.coroutines.flow.SharedFlow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // API 호출
                ApiService apiService = RetrofitClient.getApiService();
                LoginRequestDto loginRequest = new LoginRequestDto(email, password);

                apiService.login(loginRequest).enqueue(new Callback<LoginResponseDto>() {
                    @Override
                    public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                            /*
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("userName", response.body().getUsername()); // username 전달
                            startActivity(intent);
                            finish(); */

                            // SharedPreferences에 사용자 정보 저장
                            SharedPreferences sharedPreferences = getSharedPreferences("FaniversePrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putLong("userId", response.body().getId());
                            editor.putString("email", response.body().getEmail());
                            editor.putString("username", response.body().getUsername());
                            editor.apply();

                            // HomeActivity로 이동
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                        Log.e("LoginActivity", "로그인 오류 발생: " + t.getMessage());
                        Toast.makeText(LoginActivity.this, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}



