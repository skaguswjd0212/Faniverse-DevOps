package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class password extends AppCompatActivity {

    private Button btn_password;
    private EditText editTextPassword;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        sharedPreferences = getSharedPreferences("FaniversePrefs", MODE_PRIVATE); // SharedPreferences 초기화
        btn_password = findViewById(R.id.btn_password);
        editTextPassword = findViewById(R.id.editText_Password);

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editTextPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(password.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", password);
                    editor.apply();

                    Intent intent = new Intent(password.this, NickName.class);
                    startActivity(intent);
                }
            }
        });
    }
}
