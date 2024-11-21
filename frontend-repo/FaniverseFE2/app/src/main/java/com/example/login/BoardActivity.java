package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    public static List<Post> postList;
    public static PostAdapter postAdapter;
    private ImageView backButton;
    private TextView toolbarTitle;
    private String loggedInUserName = "Logged In User"; // 예시: 실제 로그인한 사용자 이름으로 대체해야 함
    private Button btnWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        btnWrite = findViewById(R.id.btn_write_post);

        // Toolbar 제목 설정
        toolbarTitle = findViewById(R.id.toolbar_title);
        Intent intent = getIntent();
        String communityName = intent.getStringExtra("communityName");
        int communityId = intent.getIntExtra("communityId", -1); // 전달된 communityId 가져오기
        if (communityName != null) {
            toolbarTitle.setText(communityName);
        }

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티를 종료하고 이전 액티비티로 돌아갑니다.
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 예제 데이터를 데이터베이스에서 가져오도록 수정
        postList = getPostsByCommunityId(communityId); // 커뮤니티 ID에 따른 게시글 목록 가져오기

        postAdapter = new PostAdapter(this, postList, loggedInUserName);
        recyclerView.setAdapter(postAdapter);

        // 업로드 버튼 클릭 리스너 설정
        btnWrite.setOnClickListener(view -> showPopup(view));
    }

    private List<Post> getPostsByCommunityId(int communityId) {
        List<Post> posts = new ArrayList<>();

        // 데이터베이스 또는 서버에서 게시글을 가져오는 로직 추가
        // 이 부분은 데이터베이스 또는 서버와 연동하는 방식에 따라 달라집니다.

        // 예제 데이터로 대체:
        long currentTime = System.currentTimeMillis();
        if (communityId == 1) {
            posts.add(new Post(1, "skeiwn23", "뉴진스 공패 개예쁘다.. 팜하니 상의 정보 아는 사람", 21, 4, currentTime - 130000, R.drawable.ic_newjeans));
            posts.add(new Post(1, "민지플레이션", "아 바이에른뮌헨 인스타 공계에\n<선수들도 놀란 뉴진스 민지의 독수리슛> 제목으로 시축 영상\n따로 올라온 거 개웃기다", 182, 52, currentTime - 180000, R.drawable.ic_newjeans_soccer));
        } else if (communityId == 2) {
            posts.add(new Post(2, "재민니", "에스파는\n방시혁을\n찢어", 6, 18, currentTime - 720000, 0));
        }

        return posts;
    }

    private void showPopup(View anchorView) {
        // 팝업 레이아웃을 인플레이트
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_write, null);

        // PopupWindow를 생성하고 설정
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 일반 판매 버튼
        Button btnGeneralSales = popupView.findViewById(R.id.btn_write);
        btnGeneralSales.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, PostCreationActivity.class);
            startActivity(intent);
            popupWindow.dismiss(); // 팝업 닫기
        });

        popupWindow.showAsDropDown(anchorView, 0, -anchorView.getHeight()-190);
    }
}