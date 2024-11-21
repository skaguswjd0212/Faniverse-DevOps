package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productTitle.setText(product.getTitle());
        holder.productCategory.setText(product.getCategory());
        holder.productPrice.setText(formatPrice(product.getPrice())); // 가격 형식 지정

        // Glide를 사용하여 이미지 로드
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl()) // 이미지 URL 가져오기
                .into(holder.productImage); // ImageView에 로드
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productTitle;
        public TextView productCategory;
        public TextView productPrice;
        public ImageView productImage;

        public ProductViewHolder(View view) {
            super(view);
            productTitle = view.findViewById(R.id.product_title);
            productCategory = view.findViewById(R.id.product_category);
            productPrice = view.findViewById(R.id.product_price);
            productImage = view.findViewById(R.id.product_image);
        }
    }

    // 가격을 원화 형식으로 포맷팅하는 메서드
    private String formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 천 단위로 구분
        return decimalFormat.format(price) + " 원"; // "원" 추가
    }
}
