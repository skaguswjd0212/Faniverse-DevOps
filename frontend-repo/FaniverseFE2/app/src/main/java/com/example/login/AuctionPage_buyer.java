package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AuctionPage_buyer extends AppCompatActivity {

    private Button btnAuctionAttend;
    private static final int AUCTION_ATTEND_REQUEST_CODE = 1;
    private static final int DELAY_MILLIS = 2000; // 2초 지연 시간

    private TextView productNameTextView, productCategoryTextView, productPriceTextView, productDescriptionTextView, auctionEndDateTextView, maxBidAmountTextView;
    private String maxBidAmount = "0"; // 현재 최대 경매 금액

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_page_buyer);

        // "경매 참여하기" 버튼을 레이아웃에서 찾기
        btnAuctionAttend = findViewById(R.id.btn_auctionattend);

        // UI 요소들 찾기
        productNameTextView = findViewById(R.id.product_name);
        productCategoryTextView = findViewById(R.id.product_category);
        productPriceTextView = findViewById(R.id.product_price);
        productDescriptionTextView = findViewById(R.id.product_description);
        auctionEndDateTextView = findViewById(R.id.auction_end_date);
        maxBidAmountTextView = findViewById(R.id.max_bid_amount); // 최대 경매 금액 TextView

        // Intent로 전달된 데이터를 수신
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String productCategory = intent.getStringExtra("product_category");
        String productStartPrice = intent.getStringExtra("product_start_price");
        String productDescription = intent.getStringExtra("product_description");
        String auctionEndDate = intent.getStringExtra("auction_end_date");

        // UI에 데이터 반영
        productNameTextView.setText(productName);
        productCategoryTextView.setText("카테고리: " + productCategory);
        productPriceTextView.setText("최소 가격: ₩" + productStartPrice);
        productDescriptionTextView.setText("상품 설명: " + productDescription);
        auctionEndDateTextView.setText("경매 마감 날짜: " + auctionEndDate);
        maxBidAmountTextView.setText("현재 최대 경매 금액: ₩" + maxBidAmount);

        // 버튼 클릭 시 AuctionAttend 액티비티로 이동
        btnAuctionAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuctionPage_buyer.this, AuctionAttend.class);
                startActivityForResult(intent, AUCTION_ATTEND_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUCTION_ATTEND_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String bidAmount = data.getStringExtra("auction_bid_amount");
            if (bidAmount != null && !bidAmount.isEmpty()) {
                maxBidAmountTextView.setText("현재 최대 경매 금액: ₩" + bidAmount);

                // 버튼 텍스트 변경 및 비활성화
                btnAuctionAttend.setText("경매 참여 완료");
                btnAuctionAttend.setEnabled(false);
                btnAuctionAttend.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // 버튼 색상 변경

                // 2초 후에 HomeActivity로 이동
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent homeIntent = new Intent(AuctionPage_buyer.this, HomeActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeIntent);
                        finish(); // 현재 액티비티 종료
                    }
                }, DELAY_MILLIS);
            }
        }
    }
}
