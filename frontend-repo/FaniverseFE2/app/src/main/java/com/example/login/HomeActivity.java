package com.example.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.api.ApiService;
import com.example.login.model.ProductListResponseWrapper;
import com.example.login.model.ProductListResponse;
import com.example.login.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ImageView searchIcon;
    private ImageView detailIcon;
    private LinearLayout homeLayout;
    private LinearLayout interestLayout;
    private LinearLayout chatLayout;
    private LinearLayout communityLayout;
    private LinearLayout mypageLayout;
    private RecyclerView favoritesContainer;

    private RecyclerView recentItemsRecyclerView;
    private RecentItemsAdapter recentItemsAdapter;

    private ImageView bannerImageView;
    private int[] bannerImages = {R.drawable.ic_banner, R.drawable.ic_banner2, R.drawable.ic_banner3}; // 이미지 배열
    private int currentBannerIndex = 0;

    private static final int MAX_FAVORITES = 3; // 최대 즐겨찾기 개수
    private static final int BANNER_CHANGE_INTERVAL = 3000; // 3초 간격

    private BroadcastReceiver favoritesUpdateReceiver;
    private FavoritesAdapter favoritesAdapter;
    private Handler bannerHandler = new Handler(Looper.getMainLooper());

    private Button btnUpload;

    private Runnable bannerRunnable = new Runnable() {
        @Override
        public void run() {
            // 배너 이미지 변경
            currentBannerIndex = (currentBannerIndex + 1) % bannerImages.length;
            bannerImageView.setImageResource(bannerImages[currentBannerIndex]);

            // 3초 후 다시 실행
            bannerHandler.postDelayed(this, BANNER_CHANGE_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // UI 요소 초기화
        initializeUIElements();

        // 클릭 이벤트 설정
        setClickListeners();

        // RecyclerView 설정
        setupRecyclerView();

        // 즐겨찾기 항목 업데이트
        updateFavoriteBoards();

        // 3초마다 배너 이미지를 변경하는 작업 시작
        bannerHandler.postDelayed(bannerRunnable, BANNER_CHANGE_INTERVAL);

        // BroadcastReceiver 설정
        favoritesUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateFavoriteBoards();
            }
        };

        // RecyclerView 설정
        recentItemsRecyclerView = findViewById(R.id.recent_items_recycler_view);
        recentItemsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        IntentFilter filter = new IntentFilter("com.example.login.UPDATE_FAVORITES");
        ContextCompat.registerReceiver(this, favoritesUpdateReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);

        // 업로드 버튼 클릭 리스너 설정
        btnUpload.setOnClickListener(view -> showPopup(view));

        // API 호출 예제
        fetchDataFromApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (favoritesUpdateReceiver != null) {
            unregisterReceiver(favoritesUpdateReceiver);
        }
        // 배너 이미지 변경 작업 중지
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    private void initializeUIElements() {
        searchIcon = findViewById(R.id.search_icon);
        detailIcon = findViewById(R.id.image_detail);
        homeLayout = findViewById(R.id.home_layout);
        interestLayout = findViewById(R.id.interest_layout);
        chatLayout = findViewById(R.id.chat_layout);
        communityLayout = findViewById(R.id.community_layout);
        mypageLayout = findViewById(R.id.mypage_layout);
        favoritesContainer = findViewById(R.id.favorites_container);
        bannerImageView = findViewById(R.id.banner_image);
        btnUpload = findViewById(R.id.btn_upload);
        recentItemsRecyclerView = findViewById(R.id.recent_items_recycler_view);
        recentItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recentItemsAdapter = new RecentItemsAdapter(new ArrayList<>());
        recentItemsRecyclerView.setAdapter(recentItemsAdapter);

        // recentItemsRecyclerView 설정
        recentItemsRecyclerView = findViewById(R.id.recent_items_recycler_view);
        recentItemsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        recentItemsAdapter = new RecentItemsAdapter(new ArrayList<>());
        recentItemsRecyclerView.setAdapter(recentItemsAdapter);
        recentItemsAdapter.notifyDataSetChanged(); // 데이터 갱신 알림
    }

    private void updateRecentItems(List<ProductListResponse> items) {
        recentItemsAdapter = new RecentItemsAdapter(items);
        recentItemsRecyclerView.setAdapter(recentItemsAdapter);
    }

    private void setClickListeners() {
        searchIcon.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        detailIcon.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, DetailPage_buyer.class);
            startActivity(intent);
        });

        homeLayout.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, HomeActivity.class)));
        interestLayout.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, InterestActivity.class)));
        chatLayout.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, ChatHome.class)));
        communityLayout.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, CommunityActivity.class)));
        mypageLayout.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MyPage.class)));
    }

    private void setupRecyclerView() {
        favoritesContainer.setLayoutManager(new LinearLayoutManager(this));
        favoritesAdapter = new FavoritesAdapter(new ArrayList<>());
        favoritesContainer.setAdapter(favoritesAdapter);
    }

    private void updateFavoriteBoards() {
        SharedPreferences prefs = getSharedPreferences("favorites", MODE_PRIVATE);
        Set<String> favoriteSet = prefs.getStringSet("favorite_communities", new HashSet<>());

        List<String> favoriteList = new ArrayList<>(favoriteSet);
        if (favoriteList.size() > MAX_FAVORITES) {
            favoriteList = favoriteList.subList(0, MAX_FAVORITES);
        }
        favoritesAdapter.updateFavorites(favoriteList);
    }

    private void fetchDataFromApi() {
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getRecentProducts().enqueue(new Callback<ProductListResponseWrapper>() {
            @Override
            public void onResponse(Call<ProductListResponseWrapper> call, Response<ProductListResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductListResponse> items = response.body().getItems();

                    if (items == null) {
                        Log.d("HomeActivity", "Items is null");
                        items = new ArrayList<>(); // 빈 리스트로 초기화
                    }

                    updateRecentItems(items); // 데이터 업데이트 메서드 호출

                    Log.d("HomeActivity", "Full Response: " + response.body());
                    Log.d("HomeActivity", "Fetched items count: " + items.size()); // 로그 추가

                    // 최근 10개만 표시하도록 제한
                    if (items.size() > 10) {
                        items = items.subList(0, 10);
                    }

                    updateRecentItems(items);
                    Toast.makeText(HomeActivity.this, "데이터 로드 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("HomeActivity", "API 호출 실패: " + response.message());
                    Toast.makeText(HomeActivity.this, "API 호출 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListResponseWrapper> call, Throwable t) {
                Log.e("HomeActivity", "API 호출 오류: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "API 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopup(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_sales_option, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();
        popupWindow.showAtLocation(anchorView, 0, location[0], location[1] - popupHeight - 80);

        Button btnGeneralSales = popupView.findViewById(R.id.btn_general_sales);
        btnGeneralSales.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GeneralSales.class);
            startActivity(intent);
            popupWindow.dismiss();
        });

        Button btnAuctionSales = popupView.findViewById(R.id.btn_auction_sales);
        btnAuctionSales.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AuctionSales.class);
            startActivity(intent);
            popupWindow.dismiss();
        });
    }
}