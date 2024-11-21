package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AuctionSales extends AppCompatActivity {

    private EditText titleEditText, categoryEditText, startPriceEditText, descriptionEditText, auctionEndDateEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_sales);

        // UI 요소들 찾기
        titleEditText = findViewById(R.id.title_edit);
        categoryEditText = findViewById(R.id.category_edit);
        startPriceEditText = findViewById(R.id.edit_start_price);
        descriptionEditText = findViewById(R.id.product_description_edit);
        auctionEndDateEditText = findViewById(R.id.edit_auction_end_date);

        // "등록하기" 버튼 클릭 이벤트 설정
        saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 데이터 가져오기
                String title = titleEditText.getText().toString();
                String category = categoryEditText.getText().toString();
                String startPrice = startPriceEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String auctionEndDate = auctionEndDateEditText.getText().toString();

                // Intent를 사용해 데이터를 AuctionPage_buyer로 전달
                Intent intent = new Intent(AuctionSales.this, AuctionPage_buyer.class);
                intent.putExtra("product_name", title);
                intent.putExtra("product_category", category);
                intent.putExtra("product_start_price", startPrice);
                intent.putExtra("product_description", description);
                intent.putExtra("auction_end_date", auctionEndDate);
                startActivity(intent);
            }
        });
    }
}
