package com.example.login;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.api.ApiService;
import com.example.login.model.KeywordDto;
import com.example.login.model.WishlistProductDto;
import com.example.login.network.RetrofitClient;
import com.google.android.flexbox.FlexboxLayout;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestActivity extends AppCompatActivity {

    private LinearLayout homeLayout;
    private LinearLayout interestLayout;
    private LinearLayout chatLayout;
    private LinearLayout communityLayout;
    private LinearLayout mypageLayout;
    private Button btnInterest;
    private ProgressBar keywordProgressBar;
    private TextView keywordsCountTextView;
    private ProgressBar wishlistProgressBar1;
    private ProgressBar wishlistProgressBar2;
    private TextView wishlistCountTextView;
    private ProgressBar removeWishlistProgressBar;

    private List<KeywordDto> keywords;

    private List<WishlistProductDto> wishlistProducts;
    private WishlistAdapter firstWishlistAdapter;
    private WishlistAdapter secondWishlistAdapter;
    private List<WishlistProductDto> firstWishlistProducts;
    private List<WishlistProductDto> secondWishlistProducts;

    private RecyclerView firstRecyclerView;
    private RecyclerView secondRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        keywordProgressBar = findViewById(R.id.progressBar_keyword);
        keywordsCountTextView = findViewById(R.id.tv_total_keywords);

        wishlistProgressBar1 = findViewById(R.id.progressBar_first);
        wishlistProgressBar2 = findViewById(R.id.progressBar_second);
        wishlistCountTextView = findViewById(R.id.tv_total_saved);
        removeWishlistProgressBar = findViewById(R.id.loading_progress_bar);

        firstRecyclerView = findViewById(R.id.recycler_view_wishlist_first);
        firstRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        firstWishlistProducts = new ArrayList<>();
        firstWishlistAdapter = new WishlistAdapter(firstWishlistProducts, this, new WishlistAdapter.RemoveItemListener() {
            @Override
            public void onRemoveWishlistItem(long wishlistId, int position) {
                new AlertDialog.Builder(InterestActivity.this)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", (dialog, which) -> {
                            removeWishlistItem(wishlistId, position * 2);
                        })
                        .setNegativeButton("취소", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });
        firstRecyclerView.setAdapter(firstWishlistAdapter);

        secondRecyclerView = findViewById(R.id.recycler_view_wishlist_second);
        secondRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        secondWishlistProducts = new ArrayList<>();
        secondWishlistAdapter = new WishlistAdapter(secondWishlistProducts, this, new WishlistAdapter.RemoveItemListener() {
            @Override
            public void onRemoveWishlistItem(long wishlistId, int position) {
                new AlertDialog.Builder(InterestActivity.this)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", (dialog, which) -> {
                            removeWishlistItem(wishlistId, position * 2 + 1);
                        })
                        .setNegativeButton("취소", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });
        secondRecyclerView.setAdapter(secondWishlistAdapter);

        // 사용자 키워드 조회 호출
        fetchUserKeywords();

        // 위시리스트 상품 조회 호출
        fetchWishlistProducts();

        // 브로드캐스트 리시버 등록
        LocalBroadcastManager.getInstance(this).registerReceiver(keywordReceiver, new IntentFilter("KEYWORD_ADDED"));

        btnInterest = findViewById(R.id.btn_interest_keyword);

        // 홈 레이아웃 클릭 이벤트 설정
        homeLayout = findViewById(R.id.home_layout);
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // 관심 레이아웃 클릭 이벤트 설정
        interestLayout = findViewById(R.id.interest_layout);
        interestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this, InterestActivity.class);
                startActivity(intent);
            }
        });

        // 채팅 레이아웃 클릭 이벤트 설정
        chatLayout = findViewById(R.id.chat_layout);
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this,  ChatHome.class);
                startActivity(intent);
            }
        });

        // 커뮤니티 레이아웃 클릭 이벤트 설정
        communityLayout = findViewById(R.id.community_layout);
        communityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this,  CommunityActivity.class);
                startActivity(intent);
            }
        });

        // 마이페이지 레이아웃 클릭 이벤트 설정
        mypageLayout = findViewById(R.id.mypage_layout);
        mypageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this,  MyPage.class);
                startActivity(intent);
            }
        });

        // 업로드 버튼 클릭 리스너 설정
        btnInterest.setOnClickListener(view -> showPopup(view));
    }


    // 키워드

    // 키워드 팝업 처리
    private void showPopup(View anchorView) {
        // 팝업 레이아웃을 인플레이트
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_keyword, null);

        // PopupWindow를 생성하고 설정
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 키워드 등록 버튼
        Button btnKeywordRegister = popupView.findViewById(R.id.btn_keyword);
        btnKeywordRegister.setOnClickListener(v -> {
            Intent intent = new Intent(InterestActivity.this, KeywordRegistrationActivity.class);
            startActivity(intent);
            popupWindow.dismiss(); // 팝업 닫기
        });

        popupWindow.showAsDropDown(anchorView, 0, -anchorView.getHeight()-190);
    }


    // 키워드 수신 후 처리
    private BroadcastReceiver keywordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 새로 등록한 키워드 수신
            String newKeyword = intent.getStringExtra("newKeyword");
            fetchUserKeywords(); // 키워드 목록 다시 가져와서 업데이트
        }
    };

    // 키워드 데이터 가져오기
    private void fetchUserKeywords() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<KeywordDto>> call = apiService.getUserKeywords();

        FlexboxLayout keywordsContainer = findViewById(R.id.keywords_container);
        keywordsContainer.removeAllViews();
        keywordProgressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<KeywordDto>>() {
            @Override
            public void onResponse(Call<List<KeywordDto>> call, Response<List<KeywordDto>> response) {
                if (response.isSuccessful()) {
                    List<KeywordDto> keywords = response.body();
                    displayKeywords(keywords);

                } else {
                    Log.e("InterestActivity", "키워드 로딩 실패: " + response.code() + " - " + response.message());
                    Toast.makeText(InterestActivity.this, "키워드 로딩 실패",Toast.LENGTH_SHORT).show();
                }
                keywordProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<KeywordDto>> call, Throwable t) {
                Log.e("InterestActivity", "오류 발생: " + t.getMessage());
                Toast.makeText(InterestActivity.this, "키워드 로딩 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                keywordProgressBar.setVisibility(View.GONE);
            }
        });
    }

    // 키워드 목록 화면에 표시
    private void displayKeywords(List<KeywordDto> keywords) {
        FlexboxLayout keywordsContainer = findViewById(R.id.keywords_container);
        keywordsContainer.removeAllViews(); // 기존 키워드 클리어

        // 키워드 개수
        keywordsCountTextView.setText("전체" + " " + keywords.size());

        ArrayList<String> keywordWords = new ArrayList<>();

        for (KeywordDto keyword : keywords) {

            keywordWords.add(keyword.getWord());

            TextView keywordView = new TextView(this);

            keywordView.setText("#" + keyword.getWord());
            keywordView.setTextSize(17);
            keywordView.setPadding(10, 8, 10, 8);
            keywordView.setTextColor(Color.BLACK);
            keywordView.setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE);
            keywordView.setEllipsize(null);

            // 배경(둥근 모서리)
            GradientDrawable background = new GradientDrawable();
            background.setColor(Color.parseColor("#f0f0f0"));
            background.setCornerRadius(16);
            keywordView.setBackground(background);
            keywordView.setPadding(16, 8, 16, 8);

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(8, 8, 8, 8);
            keywordView.setLayoutParams(layoutParams);

            // 키워드 클릭 시 처리 (KeywordProductListActivity로 이동)
            keywordView.setOnClickListener(v -> {
                Intent intent = new Intent(InterestActivity.this, KeywordProductListActivity.class);
                intent.putExtra("keyword", keyword.getWord()); // 키워드 word 전달
                intent.putExtra("keywordId", keyword.getId()); // 키워드 id 전달
                intent.putStringArrayListExtra("existingKeywords", keywordWords);
                startActivity(intent);
            });

            keywordsContainer.addView(keywordView); // 키워드 추가
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(keywordReceiver);
        super.onDestroy(); // 부모 클래스의 onDestroy 호출
    }



    // 위시리스트

    private void fetchWishlistProducts() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<WishlistProductDto>> call = apiService.getWishlistProducts();

        wishlistProgressBar1.setVisibility(View.VISIBLE);
        wishlistProgressBar2.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<WishlistProductDto>>() {
            @Override
            public void onResponse(Call<List<WishlistProductDto>> call, Response<List<WishlistProductDto>> response) {
                if (response.isSuccessful()) {
                    List<WishlistProductDto> wishlistProducts = response.body();

                    if (wishlistProducts != null) {
                        for (WishlistProductDto product : wishlistProducts) {
                            Log.d("InterestActivity", "fetch 시 wishlistId: " + product.getWishlistId());

                            Log.d("InterestActivity", "userId: " + product.getUserId());
                            Log.d("InterestActivity", "productId: " + product.getProductId());
                            Log.d("InterestActivity", "title: " + product.getTitle());
                            Log.d("InterestActivity", "content: " + product.getContent());
                            Log.d("InterestActivity", "category: " + product.getCategory());
                            Log.d("InterestActivity", "imageUrl: " + product.getImageUrl());
                            Log.d("InterestActivity", "price: " + product.getPrice());

                        }
                    } else {
                        Log.d("InterestActivity", "위시리스트 상품이 없습니다.");
                    }
                    displayWishlistProducts(wishlistProducts);
                } else {
                    Log.e("InterestActivity", "위시리스트 로딩 실패: " + response.code() + " - " + response.message());
                    Toast.makeText(InterestActivity.this, "위시리스트 로딩 실패", Toast.LENGTH_SHORT).show();
                }
                wishlistProgressBar1.setVisibility(View.GONE);
                wishlistProgressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<WishlistProductDto>> call, Throwable t) {
                Log.e("InterestActivity", "오류 발생: " + t.getMessage());
                Toast.makeText(InterestActivity.this, "위시리스트 로딩 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                wishlistProgressBar1.setVisibility(View.GONE);
                wishlistProgressBar2.setVisibility(View.GONE);
            }
        });
    }

    private void displayWishlistProducts(List<WishlistProductDto> wishlistProducts) {

        // 첫번째 RecyclerView에 표시할 위시리스트 아이템 설정
        List<WishlistProductDto> firstHalfProducts = getFirstHalfProducts(wishlistProducts);
        firstWishlistProducts.clear();
        firstWishlistProducts.addAll(firstHalfProducts);
        firstWishlistAdapter.notifyDataSetChanged();

        // 두번째 RecyclerView에 표시할 위시리스트 아이템 설정
        List<WishlistProductDto> secondHalfProducts = getSecondHalfProducts(wishlistProducts);
        secondWishlistProducts.clear();
        secondWishlistProducts.addAll(secondHalfProducts);
        secondWishlistAdapter.notifyDataSetChanged();

        wishlistCountTextView.setText("전체 " + wishlistProducts.size());
    }

    private List<WishlistProductDto> getFirstHalfProducts(List<WishlistProductDto> wishlistProducts) {
        List<WishlistProductDto> firstHalf = new ArrayList<>();
        for (int i = 0; i < wishlistProducts.size(); i++) {
            if (i % 2 == 0) {
                firstHalf.add(wishlistProducts.get(i)); // 홀수번째 인덱스 (0부터 시작)
            }
        }
        return firstHalf;
    }

    private List<WishlistProductDto> getSecondHalfProducts(List<WishlistProductDto> wishlistProducts) {
        List<WishlistProductDto> secondHalf = new ArrayList<>();
        for (int i = 0; i < wishlistProducts.size(); i++) {
            if (i % 2 != 0) {
                secondHalf.add(wishlistProducts.get(i)); // 짝수번째 인덱스
            }
        }
        return secondHalf;
    }

    private void removeWishlistItem(long wishlistId, int position) {
        removeWishlistProgressBar.setVisibility(View.VISIBLE);
        removeWishlistProgressBar.setIndeterminate(true);

        Log.d("InterestActivity", "wishlistId: " + wishlistId);
        Log.d("InterestActivity", "firstWishlistAdapter in removeWishlistItem: " + firstWishlistAdapter);
        Log.d("InterestActivity", "secondWishlistAdapter in removeWishlistItem: " + secondWishlistAdapter);

        ApiService apiService = RetrofitClient.getApiService();
        Call<String> call = apiService.removeWishlistItem(wishlistId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    removeWishlistProgressBar.setVisibility(View.GONE);

                    if (position % 2 == 0) {
                        int firstPosition = position / 2;
                        if (firstPosition < firstWishlistProducts.size()) {
                            firstWishlistProducts.remove(firstPosition);
                            firstWishlistAdapter.notifyItemRemoved(firstPosition);
                        }
                    } else {
                        int secondPosition = position / 2;
                        if (secondPosition < secondWishlistProducts.size()) {
                            secondWishlistProducts.remove(secondPosition);
                            secondWishlistAdapter.notifyItemRemoved(secondPosition);
                        }
                    }
                    wishlistCountTextView.setText("전체 " + (firstWishlistProducts.size() + secondWishlistProducts.size()));
                    Toast.makeText(InterestActivity.this, "관심상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("InterestActivity", "삭제 성공: " + response.message());
                } else {
                    removeWishlistProgressBar.setVisibility(View.GONE);
                    Log.e("InterestActivity", "위시리스트 상품 삭제 실패: " + response.code() + " - " + response.message());
                    // errorBody 확인
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("InterestActivity", "Error Body: " + errorBody);
                        } catch (IOException e) {
                            Log.e("InterestActivity", "Error reading error body: " + e.getMessage());
                        }
                    }
                    Toast.makeText(InterestActivity.this, "관심상품을 삭제하지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                removeWishlistProgressBar.setVisibility(View.GONE);
                Log.e("InterestActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(InterestActivity.this, "네트워크 오류 발생. 다시 시도해주세요.: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}