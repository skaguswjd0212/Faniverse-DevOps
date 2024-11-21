package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageInput;
    private ImageButton sendButton;
    private Button completeTransactionButton;
    private TextView productNameTextView;

    private List<String> messageList;
    private MessageAdapter messageAdapter;

    private String chatRoomId;
    private String chatRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Intent에서 채팅방 ID 및 이름 받아오기
        chatRoomId = getIntent().getStringExtra("CHAT_ROOM_ID");
        chatRoomName = getIntent().getStringExtra("CHAT_ROOM_NAME");

        if (chatRoomId == null || chatRoomName == null) {
            Toast.makeText(this, "채팅방 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // UI 요소 연결
        messageListView = findViewById(R.id.messageListView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        completeTransactionButton = findViewById(R.id.btn_complete_transaction);
        productNameTextView = findViewById(R.id.product_name);

        // 채팅방 이름을 UI에 설정
        TextView sellerNameTextView = findViewById(R.id.seller_name);
        sellerNameTextView.setText(chatRoomName);

        // 메시지 리스트 초기화
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        // 메시지 전송 버튼 클릭 리스너
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // 거래 완료 버튼 클릭 리스너
        completeTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTransaction();
            }
        });

        // 판매자인지 확인하고 거래 완료 버튼 표시 여부 결정
        if (checkIfUserIsSeller()) {
            completeTransactionButton.setVisibility(View.VISIBLE);
        }
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            messageList.add(message);
            messageAdapter.notifyDataSetChanged();
            messageInput.setText("");
            messageListView.smoothScrollToPosition(messageList.size() - 1);
        } else {
            Toast.makeText(this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeTransaction() {
        String productName = productNameTextView.getText().toString();
        String updatedProductName = "[거래 중] " + productName;

        Intent intent = new Intent(ChatRoomActivity.this, DetailPage_buyer.class);
        intent.putExtra("PRODUCT_NAME", updatedProductName);
        startActivity(intent);
    }

    private boolean checkIfUserIsSeller() {
        // 실제 판매자 확인 로직 구현 필요
        return true;
    }
}
