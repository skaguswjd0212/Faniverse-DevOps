package com.example.login;

import com.example.login.Comment;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.api.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentListActivity extends AppCompatActivity {

    private ListView commentListView;
    private CommentAdapter commentAdapter;
    private ApiService apiService;
    private int postPosition;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        commentListView = findViewById(R.id.comment_list_view);

        Intent intent = getIntent();
        postPosition = intent.getIntExtra("postPosition", -1);

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-api-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        if (postPosition != -1) {
            post = BoardActivity.postList.get(postPosition);
            loadComments(post.getId());
        }
    }

    private void loadComments(int postId) {
        apiService.getComments(postId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body();
                    commentAdapter = new CommentAdapter(CommentListActivity.this, comments);
                    commentListView.setAdapter(commentAdapter);
                } else {
                    Toast.makeText(CommentListActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(CommentListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

