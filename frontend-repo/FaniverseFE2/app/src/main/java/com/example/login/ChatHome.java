package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatHome extends AppCompatActivity {

    private LinearLayout homeLayout;
    private LinearLayout interestLayout;
    private LinearLayout chatLayout;
    private LinearLayout communityLayout;
    private LinearLayout mypageLayout;

    private ListView chatListView;
    private List<ChatRoom> chatRoomList;
    private ChatRoomAdapter chatRoomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);

        chatListView = findViewById(R.id.chatListView);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 가상의 채팅방 생성
        chatRoomList = new ArrayList<>();
        chatRoomList.add(new ChatRoom("1", "뉴진스 가방(끈 조절 가능)","뉴진스사랑", R.drawable.ic_cat1));
        chatRoomList.add(new ChatRoom("2", "양의지 미니 등신대","두산안방마님", R.drawable.ic_goods7 ));
        chatRoomList.add(new ChatRoom("2", "뉴진스 민지 공방 포카", "민지바라기", R.drawable.ic_goods1));

        chatRoomAdapter = new ChatRoomAdapter(this, chatRoomList);
        chatListView.setAdapter(chatRoomAdapter);

        // 채팅방 클릭 리스너
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatRoom selectedRoom = chatRoomList.get(position);

                // 선택된 채팅방으로 이동
                Intent intent = new Intent(ChatHome.this, ChatRoomActivity.class);
                intent.putExtra("CHAT_ROOM_ID", selectedRoom.getId());
                intent.putExtra("CHAT_ROOM_NAME", selectedRoom.getName());
                startActivity(intent);
            }
        });

        // 홈 레이아웃 클릭 이벤트 설정
        homeLayout = findViewById(R.id.home_layout);
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatHome.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // 관심 레이아웃 클릭 이벤트 설정
        interestLayout = findViewById(R.id.interest_layout);
        interestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatHome.this, InterestActivity.class);
                startActivity(intent);
            }
        });

        // 채팅 레이아웃 클릭 이벤트 설정
        chatLayout = findViewById(R.id.chat_layout);
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatHome.this,  ChatHome.class);
                startActivity(intent);
            }
        });

        // 커뮤니티 레이아웃 클릭 이벤트 설정
        communityLayout = findViewById(R.id.community_layout);
        communityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatHome.this,  CommunityActivity.class);
                startActivity(intent);
            }
        });

        // 마이페이지 레이아웃 클릭 이벤트 설정
        mypageLayout = findViewById(R.id.mypage_layout);
        mypageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatHome.this,  MyPage.class);
                startActivity(intent);
            }
        });
    }
}