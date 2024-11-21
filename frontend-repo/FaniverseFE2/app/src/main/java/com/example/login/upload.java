package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class upload extends AppCompatActivity {

    private Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_upload = findViewById(R.id.btn_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    private void showPopup(View anchorView) {
        // 팝업 레이아웃을 인플레이트
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_sales_option, null);

        // PopupWindow를 생성하고 설정
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 팝업 위치 설정: 버튼 바로 위에 나타나도록
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        // 팝업 창을 측정하여 정확한 높이 값을 얻음
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();

        // 팝업 위치를 조정하여 + 버튼 바로 위에 나타나도록 설정
        popupWindow.showAtLocation(anchorView, 0, location[0], location[1] - popupHeight);

        // 일반 판매 버튼
        Button btnGeneralSales = popupView.findViewById(R.id.btn_general_sales);
        btnGeneralSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(upload.this, GeneralSales.class);
                startActivity(intent);
                popupWindow.dismiss(); // 팝업 닫기
            }
        });

        // 경매 판매 버튼
        Button btnAuctionSales = popupView.findViewById(R.id.btn_auction_sales);
        btnAuctionSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(upload.this, AuctionSales.class);
                startActivity(intent);
                popupWindow.dismiss(); // 팝업 닫기
            }
        });
    }
}
