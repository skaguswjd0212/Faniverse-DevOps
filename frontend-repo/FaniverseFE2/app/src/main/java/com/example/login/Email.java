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

public class Email extends AppCompatActivity {

    private Button btn_email;
    private EditText editTextEmail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        btn_email = findViewById(R.id.btn_email);
        editTextEmail = findViewById(R.id.editText_Email);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("FaniversePrefs", MODE_PRIVATE);

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                if (!email.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.apply();
                    Intent intent = new Intent(Email.this, password.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Email.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
