package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private String loggedInUserName;
    private Context context;

    public PostAdapter(Context context, List<Post> postList, String loggedInUserName) {
        this.context = context;
        this.postList = postList;
        this.loggedInUserName = loggedInUserName;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.userName.setText(post.getUserName());
        holder.postTime.setText(post.getPostTime());
        holder.postText.setText(post.getPostText());
        holder.likeCount.setText(post.getLikeCount() + " likes");
        holder.commentCount.setText(post.getCommentCount() + " comments");

        // 좋아요 색상 설정
        if (post.isLiked()) {
            holder.likeCount.setTextColor(Color.parseColor("#0000FF")); // 좋아요 눌렀을 때 색상
        } else {
            holder.likeCount.setTextColor(Color.BLACK); // 기본 색상
        }

        // 이미지 설정
        if (post.getImageResourceId() != 0) {
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postImage.setImageResource(post.getImageResourceId());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        // 좋아요 클릭 이벤트 처리
        holder.likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                Post post = postList.get(adapterPosition);
                if (post.isLiked()) {
                    post.setLiked(false);
                    post.decrementLikeCount();
                    holder.likeCount.setTextColor(Color.BLACK);
                } else {
                    post.setLiked(true);
                    post.incrementLikeCount();
                    holder.likeCount.setTextColor(Color.MAGENTA);
                }
                holder.likeCount.setText(post.getLikeCount() + " likes");
            }
        });

        // 댓글 클릭 이벤트 처리
        holder.commentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postPosition", adapterPosition);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView userName, postTime, postText, likeCount, commentCount;
        ImageView userProfileImage, postImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            postTime = itemView.findViewById(R.id.post_time);
            postText = itemView.findViewById(R.id.post_text);
            likeCount = itemView.findViewById(R.id.like_count);
            commentCount = itemView.findViewById(R.id.comment_count);
            userProfileImage = itemView.findViewById(R.id.user_profile_image);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}





