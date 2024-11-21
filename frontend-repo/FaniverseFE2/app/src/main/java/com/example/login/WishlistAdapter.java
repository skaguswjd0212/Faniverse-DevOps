package com.example.login;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.model.WishlistProductDto;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private List<WishlistProductDto> wishlistProducts;
    private Context context;
    private RemoveItemListener removeItemListener;

    public interface RemoveItemListener {
        void onRemoveWishlistItem(long wishlistId, int position);
    }

    public WishlistAdapter(List<WishlistProductDto> wishlistProducts, Context context, RemoveItemListener removeItemListener) {
        this.wishlistProducts = wishlistProducts;
        this.context = context;
        this.removeItemListener = removeItemListener;
    }

    @Override
    public WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist_product, parent, false);
        return new WishlistViewHolder(itemView);
    }

    public void removeItem(int position) {
        wishlistProducts.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(WishlistViewHolder holder, int position) {
        WishlistProductDto wishlistProduct = wishlistProducts.get(position);

        // 상품 정보를 레이아웃에 설정
        holder.wishlistProductTitle.setText(wishlistProduct.getTitle());
        holder.wishlistProductPrice.setText(String.format("%,d원", (int) wishlistProduct.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(wishlistProduct.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.wishlistProductImage);

        holder.removeButton.setOnClickListener(v -> {
            long wishlistId = wishlistProduct.getWishlistId();
            //int position = holder.getAdapterPosition();

            if (removeItemListener != null) {
                removeItemListener.onRemoveWishlistItem(wishlistId, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return wishlistProducts.size();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView wishlistProductImage;
        TextView wishlistProductTitle;
        TextView wishlistProductPrice;
        ImageView removeButton;

        public WishlistViewHolder(View itemView) {
            super(itemView);
            wishlistProductImage = itemView.findViewById(R.id.wishlist_product_image);
            wishlistProductTitle = itemView.findViewById(R.id.wishlist_product_title);
            wishlistProductPrice = itemView.findViewById(R.id.wishlist_product_price);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}