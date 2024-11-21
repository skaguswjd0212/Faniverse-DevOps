package com.example.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class PostCreationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonAddImage;
    private ImageView imageViewPreview;
    private Button buttonSubmit;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);

        // UI 요소 초기화
        editTextContent = findViewById(R.id.editText_content);
        buttonAddImage = findViewById(R.id.button_add_image);
        buttonSubmit = findViewById(R.id.button_submit);

        // 사진 추가 버튼 클릭 리스너 설정
        buttonAddImage.setOnClickListener(v -> openImageChooser());

        // 작성 완료 버튼 클릭 리스너 설정
        buttonSubmit.setOnClickListener(v -> submitPost());
    }

    private void openImageChooser() {
        // 이미지 선택을 위한 인텐트 호출
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // 선택된 이미지를 ImageView에 표시
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewPreview.setImageBitmap(bitmap);
                imageViewPreview.setVisibility(View.VISIBLE); // 이미지가 선택되면 ImageView를 표시
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitPost() {
        // 제목과 내용 가져오기
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        // 입력 검증
        if (title.isEmpty()) {
            Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 글과 사진을 서버로 전송하거나 데이터베이스에 저장하는 작업 수행
        // 여기서는 예시로 간단한 Toast 메시지로 처리
        Toast.makeText(this, "글이 작성되었습니다!", Toast.LENGTH_SHORT).show();

        // 글 작성 후 이전 화면으로 돌아가기
        finish();
    }
}
