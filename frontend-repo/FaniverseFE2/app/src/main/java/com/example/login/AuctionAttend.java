package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AuctionAttend extends AppCompatActivity {

    private Button btnAuction;
    private EditText maxAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_attend);

        // UI 요소들 찾기
        btnAuction = findViewById(R.id.btn_auction);
        maxAmountEditText = findViewById(R.id.edit_max_amount);

        btnAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 경매 금액 가져오기
                String maxAmount = maxAmountEditText.getText().toString();

                // 경매 참여 완료 신호와 함께 경매 금액을 전달
                Intent intent = new Intent();
                intent.putExtra("auction_bid_amount", maxAmount); // 경매 금액 전달
                setResult(RESULT_OK, intent);
                finish(); // 현재 액티비티를 종료하고 이전 액티비티로 돌아감
            }
        });
    }
}
