package com.example.login;

import com.example.login.api.ApiService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {

    private TextView postTextView;
    private EditText commentEditText;
    private Button submitButton;
    private int postPosition;
    private Post post;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postTextView = findViewById(R.id.post_text);
        commentEditText = findViewById(R.id.comment_edit_text);
        submitButton = findViewById(R.id.submit_button);

        Intent intent = getIntent();
        postPosition = intent.getIntExtra("postPosition", -1);

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-api-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        if (postPosition != -1) {
            post = BoardActivity.postList.get(postPosition); // BoardActivity의 postList에서 게시물 가져오기
            postTextView.setText(post.getPostText());
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                if (!commentText.isEmpty()) {
                    Comment comment = new Comment(commentText);
                    submitComment(comment);
                }
            }
        });

        postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 댓글 목록을 표시하는 액티비티로 이동
                Intent intent = new Intent(CommentActivity.this, CommentListActivity.class);
                intent.putExtra("postPosition", postPosition);
                startActivity(intent);
            }
        });
    }

    private void submitComment(Comment comment) {
        apiService.addComment(post.getId(), comment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CommentActivity.this, "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 댓글 작성 후 액티비티 종료
                } else {
                    Toast.makeText(CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
