package com.example.login;

import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.login.model.ProductListResponse;
import java.util.List;

public class RecentItemsAdapter extends RecyclerView.Adapter<RecentItemsAdapter.ViewHolder> {
    private List<ProductListResponse> items;

    public RecentItemsAdapter(List<ProductListResponse> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductListResponse item = items.get(position);

        String imageUrl = item.getImageUrl();

        Log.d("RecentItemsAdapter", "Image URL: " + imageUrl);

        // Glide를 사용하여 이미지 로드
        Glide.with(holder.imageView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.error_image) // 로딩 중일 때 표시할 이미지
                .error(R.drawable.error_image)             // 로딩 실패 시 표시할 이미지
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
