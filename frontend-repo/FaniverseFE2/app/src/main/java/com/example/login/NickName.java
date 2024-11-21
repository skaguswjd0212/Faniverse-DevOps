package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NickName extends AppCompatActivity {

    private EditText editTextNickname;
    private Button btn_nickname;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        editTextNickname = findViewById(R.id.editText_nickname);
        btn_nickname = findViewById(R.id.btn_nickname);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("FaniversePrefs", Context.MODE_PRIVATE);

        btn_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = editTextNickname.getText().toString().trim();

                if (nickname.isEmpty()) {
                    Toast.makeText(NickName.this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 닉네임을 SharedPreferences에 저장
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nickname", nickname);
                    editor.apply();

                    // 다음 액티비티로 이동
                    Intent intent = new Intent(NickName.this, JoinComplete.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
