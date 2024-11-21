package com.example.login;

import android.os.Parcel;
import android.os.Parcelable;

public class Product /*implements Parcelable*/ {
    private String title;
    private String content;
    private String category;
    private double price;
    private String status;
    private String imageUrl;

    public Product(String title, String content, String category, double price, String status, String imageUrl) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    // 검색 결과 상품 목록 조회에 필요한 필드만 받는 생성자 (오버로딩)
    public Product(String title, String category, double price, String imageUrl) {
        this.title = title;
        this.content = ""; // 기본값
        this.category = category;
        this.price = price;
        this.status = "";  // 기본값
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() { return content; }

    public String getCategory() { return category;}

    public double getPrice() {
        return price;
    }

    public String getStatus() { return status; }

    public String getImageUrl() { return imageUrl; }

}
