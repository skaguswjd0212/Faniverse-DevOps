package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.model.KeywordDto;
import com.example.login.network.RetrofitClient;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeywordRegistrationActivity extends AppCompatActivity {

    private EditText etKeyword;
    private Button btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_register);

        // UI 요소 초기화
        etKeyword = findViewById(R.id.et_keyword);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.loading_progress_bar);

        // 등록 버튼 클릭 리스너 설정
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addKeyword();
            }
        });
    }

    private void addKeyword() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        String keyword = etKeyword.getText().toString().trim();
        if (!keyword.isEmpty()) {
            KeywordDto keywordDto = new KeywordDto(null, keyword);

            Call<KeywordDto> call = RetrofitClient.getApiService().addKeyword(keywordDto);
            call.enqueue(new Callback<KeywordDto>() {
                @Override
                public void onResponse(Call<KeywordDto> call, Response<KeywordDto> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        // 새 키워드를 브로드캐스트로 전송
                        Intent intent = new Intent("KEYWORD_ADDED");
                        intent.putExtra("newKeyword", keyword);
                        LocalBroadcastManager.getInstance(KeywordRegistrationActivity.this).sendBroadcast(intent);
                        Toast.makeText(KeywordRegistrationActivity.this, "키워드가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(KeywordRegistrationActivity.this, "키워드를 등록하지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<KeywordDto> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("addKeyword", "네트워크 오류 발생: " + t.getMessage(), t);
                    Toast.makeText(KeywordRegistrationActivity.this, "네트워크 오류 발생. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "키워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
