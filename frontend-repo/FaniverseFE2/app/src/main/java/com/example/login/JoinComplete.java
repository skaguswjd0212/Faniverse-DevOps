package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.login.api.ApiService;
import com.example.login.model.RegisterRequestDto;
import com.example.login.model.RegisterResponseDto;
import com.example.login.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinComplete extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private Button btn_joincomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_complete);

        sharedPreferences = getSharedPreferences("FaniversePrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        String nickname = sharedPreferences.getString("nickname", null);

        // 로그 출력
        Log.d("JoinComplete", "Email: " + email);
        Log.d("JoinComplete", "Password: " + password);
        Log.d("JoinComplete", "Nickname: " + nickname);


        // Retrofit API 호출
        ApiService apiService = RetrofitClient.getApiService();
        RegisterRequestDto registerRequest = new RegisterRequestDto(email, nickname, password);

        apiService.registerUser(registerRequest).enqueue(new Callback<RegisterResponseDto>() {
            @Override
            public void onResponse(Call<RegisterResponseDto> call, Response<RegisterResponseDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(JoinComplete.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    // 다음 액티비티로 이동할 수 있음
                } else {
                    Toast.makeText(JoinComplete.this, "회원가입 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseDto> call, Throwable t) {
                // 오류 메시지 로그 출력
                Log.e("JoinComplete", "오류 발생: " + t.getMessage());
                // 오류 토스트 메시지 출력
                Toast.makeText(JoinComplete.this, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 홈으로 이동
        btn_joincomplete = findViewById(R.id.btn_joincomplete);
        btn_joincomplete.setOnClickListener(view -> {
            Intent intent = new Intent(JoinComplete.this, HomeActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        });
    }
}