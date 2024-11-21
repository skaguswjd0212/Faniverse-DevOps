package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.api.ApiService;
import com.example.login.model.ProductDetailsResponse;
import com.example.login.network.RetrofitClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFilterActivity extends AppCompatActivity {

    //private TextView highPrice, lowPrice, latest;
    private Button closeButton;
    //private String selectedSortOption = null; // 선택된 필터 옵션을 저장하는 변수

    private String searchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);

        closeButton = findViewById(R.id.filter_close_button);

        // Intent로부터 검색어 가져오기
        searchWord = getIntent().getStringExtra("searchWord");
        Log.d("SearchFilterActivity", "검색어: " + searchWord);

        findViewById(R.id.high_price).setOnClickListener(v -> applyFilter("price_high"));
        findViewById(R.id.low_price).setOnClickListener(v -> applyFilter("price_low"));
        findViewById(R.id.latest).setOnClickListener(v -> applyFilter("latest"));

        closeButton.setOnClickListener(v -> finish());


    }

    private void applyFilter(String sortBy) {
        // AfterSearchActivity로 필터와 검색어 전달
        Intent resultIntent = new Intent(this, AfterSearchActivity.class);
        resultIntent.putExtra("searchWord", searchWord);
        resultIntent.putExtra("sortBy", sortBy);
        startActivity(resultIntent);
        finish();
    }

}

