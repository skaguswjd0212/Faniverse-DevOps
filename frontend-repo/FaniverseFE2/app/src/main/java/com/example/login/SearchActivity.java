package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.api.ApiService;
import com.example.login.model.ProductDetailsResponse;
import com.example.login.network.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private ImageView backButton, searchButton, filterIcon;
    private EditText searchEditText;
    private List<String> recentSearches;
    private LinearLayout recentSearchLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backButton = findViewById(R.id.back_button);
        searchButton = findViewById(R.id.search_button);
        filterIcon = findViewById(R.id.filter_icon);
        searchEditText = findViewById(R.id.search_edit_text);
        recentSearchLayout = findViewById(R.id.recent_search_layout);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);

        // Retrofit 인스턴스 생성
        apiService = RetrofitClient.getApiService();

        // api로 최근 검색어 불러오기
        fetchRecentSearches();

        // 검색 버튼 클릭 시
        searchButton.setOnClickListener(v -> {
            String searchWord = searchEditText.getText().toString();
            if (!searchWord.isEmpty()) {
                // 검색어 서버에 저장
                addRecentSearch(searchWord);

                Log.d("SearchActivity", "현재 저장된 검색어: " + recentSearches);

                // AfterSearchActivity로 이동하여 검색 결과 조회
                Intent intent = new Intent(SearchActivity.this, AfterSearchActivity.class);
                intent.putExtra("searchWord", searchWord);
                startActivity(intent);
            } else {
                Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 필터 아이콘 클릭 시
        filterIcon.setOnClickListener(v -> {
            String searchWord = searchEditText.getText().toString();
            if (!TextUtils.isEmpty(searchWord)) {
                Intent intent = new Intent(SearchActivity.this, SearchFilterActivity.class);
                intent.putExtra("searchWord", searchWord); // 검색어를 intent에 추가
                startActivity(intent);
            } else {
                Toast.makeText(SearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 뒤로 가기 버튼 클릭 시 HomeActivity로 이동
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }


    // 최근 검색어 조회
    private void fetchRecentSearches() {
        // shimmer 시작
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recentSearchLayout.setVisibility(View.GONE);

        apiService.getRecentSearches().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recentSearches = new ArrayList<>(response.body());
                    //recentSearches.removeIf(String::isEmpty);
                    displayRecentSearches(recentSearches);
                    // shimmer 중지 및 숨기기
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recentSearchLayout.setVisibility(View.VISIBLE);
                    Log.d("SearchActivity", "최근 검색어 불러오기 성공: " + recentSearches);
                } else {
                    Log.d("SearchActivity", "최근 검색어 불러오기 실패: 응답이 null이거나 성공적이지 않음");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // 실패 시 shimmer 중지 및 숨기기
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recentSearchLayout.setVisibility(View.VISIBLE);
                Log.e("SearchActivity", "최근 검색어 불러오기 실패", t);

            }
        });
    }


    // 최근 검색어 저장
    private void addRecentSearch(String searchWord) {
        if (searchWord.isEmpty()) {
            Log.d("SearchActivity", "빈 문자열은 최근 검색어로 저장할 수 없습니다.");
            return;
        }

        Log.d("addRecentSearch", "최근 검색어 목록: " + recentSearches);

        if (recentSearches.contains(searchWord)) {
            recentSearches.remove(searchWord);
            Log.d("addRecentSearch", "이미 존재하는 최근 검색어 삭제 후: " + recentSearches);
        }

        if (!recentSearches.contains(searchWord)) {
            apiService.addRecentSearch(searchWord).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("SearchActivity", "최근 검색어 저장 성공: " + searchWord);
                        recentSearches.add(0, searchWord);
                        if (recentSearches.size() > 5) {
                            recentSearches = recentSearches.subList(0, 5);
                        }
                        Log.d("SearchActivity", "현재 저장된 최근 검색어: " + recentSearches);
                        displayRecentSearches(recentSearches);
                        fetchRecentSearches(); // 추가된 후 최근 검색어 다시 로드
                    } else {
                        Log.d("SearchActivity", "최근 검색어 저장 실패: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SearchActivity", "최근 검색어 저장 실패", t);
                }
            });
        } else {
            Log.d("SearchActivity", "이미 존재하는 최근 검색어: " + searchWord);
        }
    }


    // 최근 검색어 목록 UI에 표시
    private void displayRecentSearches(List<String> searches) {
        recentSearchLayout.removeAllViews();

        List<String> reversedSearches = new ArrayList<>(searches);
        Collections.reverse(reversedSearches);

        for (String search : reversedSearches) {
            View searchItemView = LayoutInflater.from(this).inflate(R.layout.recent_search_item, recentSearchLayout, false);
            TextView searchTextView = searchItemView.findViewById(R.id.search_text);
            ImageView deleteIcon = searchItemView.findViewById(R.id.delete_icon);

            searchTextView.setText(search);

            /*
            deleteIcon.setOnClickListener(v -> {
                // 최근 검색어 삭제
                recentSearchLayout.removeView(searchItemView);
                recentSearches.remove(search);
            });
             */

            searchItemView.setOnClickListener(v -> {
                Intent intent = new Intent(SearchActivity.this, AfterSearchActivity.class);
                intent.putExtra("searchWord", search);
                startActivity(intent);

                addRecentSearch(search);

            });

            recentSearchLayout.addView(searchItemView);
        }
    }
}
