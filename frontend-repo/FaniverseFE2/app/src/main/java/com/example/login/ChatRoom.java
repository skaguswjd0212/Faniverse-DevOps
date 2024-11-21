package com.example.login;

public class ChatRoom {
    private String id;
    private String name;
    private String sellerName;
    private int imageResId;  // Drawable 리소스 ID

    public ChatRoom(String id, String name, String sellerName, int imageResId) {
        this.id = id;
        this.name = name;
        this.sellerName = sellerName;
        this.imageResId = imageResId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSellerName() {
        return sellerName;
    }

    public int getImageResId() {
        return imageResId;
    }
}
