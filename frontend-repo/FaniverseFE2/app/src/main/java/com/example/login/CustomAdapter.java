package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Community> communityList;
    private Set<String> favoriteCommunities;
    private static final int MAX_FAVORITES = 3; // 최대 노란 별 개수

    public CustomAdapter(Context context, List<Community> communityList, Set<String> favoriteCommunities) {
        this.context = context;
        this.communityList = communityList;
        this.favoriteCommunities = new HashSet<>(favoriteCommunities);
    }

    @Override
    public int getCount() {
        return communityList.size();
    }

    @Override
    public Object getItem(int position) {
        return communityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_community, parent, false);
        }

        ImageView communityImage = convertView.findViewById(R.id.community_image);
        TextView communityName = convertView.findViewById(R.id.community_name);
        ImageView bookmarkIcon = convertView.findViewById(R.id.bookmark_icon);

        Community community = communityList.get(position);

        communityImage.setImageResource(community.getImageResourceId());
        communityName.setText(community.getName());

        // 초기 상태 설정
        if (favoriteCommunities.contains(community.getName())) {
            bookmarkIcon.setImageResource(R.drawable.ic_star_filled);
        } else {
            bookmarkIcon.setImageResource(R.drawable.ic_star);
        }

        // bookmarkIcon 클릭 이벤트 처리
        bookmarkIcon.setOnClickListener(v -> {
            boolean isFavorite = favoriteCommunities.contains(community.getName());
            if (isFavorite) {
                bookmarkIcon.setImageResource(R.drawable.ic_star);
                favoriteCommunities.remove(community.getName());
                ((CommunityActivity) context).removeFavoriteFromPrefs(community.getName());
            } else {
                if (favoriteCommunities.size() < MAX_FAVORITES) {
                    bookmarkIcon.setImageResource(R.drawable.ic_star_filled);
                    favoriteCommunities.add(community.getName());
                    ((CommunityActivity) context).saveFavoriteToPrefs(community.getName());
                } else {
                    showMaxFavoritesAlert();
                }
            }
        });

        // GridView 항목 전체 클릭 이벤트 처리
        convertView.setOnClickListener(v -> {
            // 항목 클릭 시 BoardActivity로 이동
            Intent intent = new Intent(context, BoardActivity.class);
            intent.putExtra("communityName", community.getName());
            intent.putExtra("communityId", community.getId());
            context.startActivity(intent);
        });

        return convertView;
    }


    // 즐겨찾기 상태 업데이트
    public void updateFavorites(Set<String> favorites) {
        this.favoriteCommunities = favorites;
        notifyDataSetChanged();
    }

    private void showMaxFavoritesAlert() {
        if (favoriteCommunities.size() >= MAX_FAVORITES) { // 4개 이상일 때만 알림 표시
            new AlertDialog.Builder(context)
                    .setTitle("알림")
                    .setMessage("커뮤니티 즐겨찾기는 3개까지만 가능합니다.")
                    .setPositiveButton("확인", null)
                    .show();
        }
    }

    public void updateList(List<Community> newList) {
        communityList = newList;
        notifyDataSetChanged();
    }
}
