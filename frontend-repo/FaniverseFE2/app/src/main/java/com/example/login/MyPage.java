package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MyPage extends AppCompatActivity {

    private TextView nicknameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // TextView 초기화
        nicknameTextView = findViewById(R.id.nickname);

        // SharedPreferences에서 username 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("FaniversePrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        // username이 null이 아닌 경우에만 설정
        if (username != null) {
            nicknameTextView.setText(username);
        }
    }
}
