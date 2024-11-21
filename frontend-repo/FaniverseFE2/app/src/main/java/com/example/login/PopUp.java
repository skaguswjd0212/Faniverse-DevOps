package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PopUp extends AppCompatActivity {

    private Button btn_reservation, btn_soldout, btn_edit, btn_delete, btn_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up);

        // 팝업 창의 크기 및 배경 설정
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // 버튼 초기화
        btn_reservation = findViewById(R.id.btn_reservation);
        btn_soldout = findViewById(R.id.btn_soldout);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_out = findViewById(R.id.btn_out);

        // "예약중" 버튼 클릭 리스너
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity의 TextView에 [예약중] 추가
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "[예약중]");
                setResult(RESULT_OK, resultIntent);
                finish(); // 팝업 닫기
            }
        });

        // "판매완료" 버튼 클릭 리스너
        btn_soldout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity의 TextView에 [판매완료] 추가
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "[판매완료]");
                setResult(RESULT_OK, resultIntent);
                finish(); // 팝업 닫기
            }
        });

        // "게시글 수정" 버튼 클릭 리스너
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 수정 화면으로 이동하는 로직 추가
                Intent intent = new Intent(PopUp.this, EditPost.class); // 게시글 수정 액티비티
                startActivity(intent);
                finish(); // 팝업 닫기
            }
        });

        // "삭제" 버튼 클릭 리스너
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 삭제 로직 추가
                deletePost(); // 실제 삭제 로직은 deletePost()에서 구현
                Toast.makeText(PopUp.this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 팝업 닫기
            }
        });

        // "닫기" 버튼 클릭 리스너
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 팝업 닫기
            }
        });
    }

    // 게시글 삭제 기능 구현
    private void deletePost() {
        // 여기에 게시글을 실제로 삭제하는 로직을 구현하세요
        // 예: 서버 API 호출 또는 로컬 데이터베이스에서 삭제 등
    }
}
