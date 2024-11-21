package com.example.login;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private int id;  // 게시물의 고유 ID
    private String userName;
    private String postText;
    private int likeCount;
    private int commentCount;
    private long timestamp;
    private int imageResourceId;
    private boolean isLiked;
    private List<String> comments;

    public Post(int id, String userName, String postText, int likeCount, int commentCount, long timestamp, int imageResourceId) {
        this.id = id;  // ID 초기화
        this.userName = userName;
        this.postText = postText;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.timestamp = timestamp;
        this.imageResourceId = imageResourceId;
        this.isLiked = false;
        this.comments = new ArrayList<>();
    }

    // id 필드의 getter 메서드
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostText() {
        return postText;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void incrementLikeCount() {
        likeCount++;
    }

    public void decrementLikeCount() {
        likeCount--;
    }

    public void incrementCommentCount() {
        commentCount++;
    }

    public void addComment(String comment) {
        comments.add(comment);
        incrementCommentCount();
    }

    public List<String> getComments() {
        return comments;
    }

    public String getPostTime() {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - timestamp;
        long minutes = diff / (1000 * 60);
        return minutes + " min ago";
    }
}




