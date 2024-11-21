package com.example.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class GeneralSales extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;

    private EditText editTitle, editCategory, editPrice, editDescription;
    private Button btnSelectImage, btnSubmit;
    private ImageView imagePreview;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_sales);

        // UI 요소 초기화
        editTitle = findViewById(R.id.product_name);
        editCategory = findViewById(R.id.product_category);
        editPrice = findViewById(R.id.product_price);
        editDescription = findViewById(R.id.product_description);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        imagePreview = findViewById(R.id.imagePreview);

        // 이미지 선택 버튼 클릭 리스너
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenImageChooser();
            }
        });

        // 제출 버튼 클릭 리스너
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneralSales.this, DetailPage_buyer.class);
                intent.putExtra("title", editTitle.getText().toString());
                intent.putExtra("category", editCategory.getText().toString());
                intent.putExtra("price", editPrice.getText().toString());
                intent.putExtra("description", editDescription.getText().toString());
                intent.putExtra("imageUri", imageUri != null ? imageUri.toString() : null); // 이미지 URI 전달
                startActivity(intent);
            }
        });
    }

    // 권한 체크 및 이미지 선택기 열기
    private void checkPermissionAndOpenImageChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openImageChooser();
        }
    }

    // 이미지 선택기 열기
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "외부 저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 이미지 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imagePreview.setImageBitmap(bitmap); // 선택한 이미지를 미리보기로 보여줌
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "이미지를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

