package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommunityActivity extends AppCompatActivity {

    private LinearLayout homeLayout;
    private LinearLayout interestLayout;
    private LinearLayout chatLayout;
    private LinearLayout communityLayout;
    private LinearLayout mypageLayout;

    private CustomAdapter adapter;
    private List<Community> communityList;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "FavoriteCommunities";
    private static final int MAX_FAVORITES = 3; // 최대 즐겨찾기 개수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        setContentView(R.layout.activity_community);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        GridView gridView = findViewById(R.id.grid_view);
        EditText searchEditText = findViewById(R.id.search_edit_text);

        // 커뮤니티 리스트 초기화
        communityList = new ArrayList<>();
        communityList.add(new Community(1, "뉴진스", "아이돌 그룹", R.drawable.newjeans_icon));
        communityList.add(new Community(2, "에스파", "아이돌 그룹", R.drawable.aespa_icon));
        communityList.add(new Community(3, "방탄소년단", "아이돌 그룹", R.drawable.bts_icon));
        communityList.add(new Community(4, "샤이니", "아이돌 그룹", R.drawable.shinee_icon));
        // 추가 커뮤니티 데이터 추가

        adapter = new CustomAdapter(this, communityList, getFavoritesFromPrefs());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("CommunityActivity", "Item clicked: position=" + position);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCommunityList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 홈 레이아웃 클릭 이벤트 설정
        homeLayout = findViewById(R.id.home_layout);
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // 관심 레이아웃 클릭 이벤트 설정
        interestLayout = findViewById(R.id.interest_layout);
        interestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, InterestActivity.class);
                startActivity(intent);
            }
        });

        // 채팅 레이아웃 클릭 이벤트 설정
        chatLayout = findViewById(R.id.chat_layout);
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this,  ChatHome.class);
                startActivity(intent);
            }
        });

        // 커뮤니티 레이아웃 클릭 이벤트 설정
        communityLayout = findViewById(R.id.community_layout);
        communityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this,  CommunityActivity.class);
                startActivity(intent);
            }
        });

        // 마이페이지 레이아웃 클릭 이벤트 설정
        mypageLayout = findViewById(R.id.mypage_layout);
        mypageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this,  MyPage.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 돌아올 때 즐겨찾기 상태를 불러와서 UI를 업데이트
        Set<String> favoriteCommunities = getFavoritesFromPrefs();
        adapter.updateFavorites(favoriteCommunities);
    }

    private void filterCommunityList(String searchQuery) {
        String lowerCaseSearchQuery = searchQuery.toLowerCase();
        List<Community> filteredList = new ArrayList<>();
        for (Community community : communityList) {
            if (community.getName().toLowerCase().contains(lowerCaseSearchQuery)) {
                filteredList.add(community);
            }
        }

        if (filteredList.isEmpty()) {
            // 검색 결과가 없을 때 사용자에게 알려주기 위해 가상의 항목 추가
            filteredList.add(new Community(5, "No results found", "", R.drawable.ic_search));
        }

        adapter.updateList(filteredList);
    }

    private Set<String> getFavoritesFromPrefs() {
        return sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>());
    }

    public void saveFavoriteToPrefs(String communityName) {
        Set<String> favorites = getFavoritesFromPrefs();
        if (favorites.size() < MAX_FAVORITES) {
            favorites.add(communityName);
            sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply();
            notifyHomeActivity();
        } else {
            showMaxFavoritesAlert(); // 최대 즐겨찾기 개수를 초과할 때 알림
        }
    }

    public void removeFavoriteFromPrefs(String communityName) {
        Set<String> favorites = getFavoritesFromPrefs();
        favorites.remove(communityName);
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply();
        notifyHomeActivity();
    }

    private void showMaxFavoritesAlert() {
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("커뮤니티 즐겨찾기는 3개까지만 가능합니다.")
                .setPositiveButton("확인", null)
                .show();
    }

    private void notifyHomeActivity() {
        Intent intent = new Intent("com.example.login.UPDATE_FAVORITES");
        sendBroadcast(intent);
    }
}