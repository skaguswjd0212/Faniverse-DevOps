package com.example.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.api.ApiService;
import com.example.login.model.KeywordDto;
import com.example.login.model.KeywordProductDto;
import com.example.login.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bumptech.glide.Glide;


public class KeywordProductListActivity extends AppCompatActivity {
    private LinearLayout productsContainer;
    private String keyword;
    private Long keywordId;
    private ArrayList<String> existingKeywords;
    private TextView keywordTitle;
    private ImageView backButton;
    private ImageView deleteButton;
    private ImageView editButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_product_list);

        productsContainer = findViewById(R.id.products_container);
        keywordTitle = findViewById(R.id.keyword_product_list_title);
        backButton = findViewById(R.id.back_icon);
        editButton = findViewById(R.id.edit_icon);
        deleteButton = findViewById(R.id.delete_icon);
        progressBar = findViewById(R.id.loading_progress_bar);

        // Intent에서 전달된 키워드 word와 id, 키워드 단어 목록 받기
        keyword = getIntent().getStringExtra("keyword");
        keywordId = getIntent().getLongExtra("keywordId", -1);
        existingKeywords = getIntent().getStringArrayListExtra("existingKeywords");

        // 키워드 타이틀 텍스트 설정하기
        keywordTitle.setText("#" + keyword);

        if (keyword != null) {
            // 해당 키워드로 상품 목록을 불러옴
            fetchProductsByKeyword(keyword);
        }

        // 뒤로가기
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(KeywordProductListActivity.this, InterestActivity.class);
            startActivity(intent);
            finish();
        });

        // 키워드 수정
        editButton.setOnClickListener(v -> showEditKeywordDialog());

        // 키워드 삭제
        deleteButton.setOnClickListener(v -> showDeleteKeywordDialog());
    }


    private void showEditKeywordDialog() {
        // 다이얼로그 빌더 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("키워드 수정");

        // EditText 생성 및 다이얼로그에 추가
        final EditText input = new EditText(this);
        input.setText(keyword); // 기존 키워드를 EditText에 설정
        input.setSelection(input.getText().length()); // 커서를 마지막에 위치

        // EditText 폭 줄이기
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // 부모 크기와 맞춤
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int marginSize = 50;
        params.leftMargin = marginSize;
        params.rightMargin = marginSize;
        input.setLayoutParams(params);
        container.addView(input);

        // 다이얼로그에 레이아웃 추가
        builder.setView(container);

        // 확인 및 취소 버튼 설정
        builder.setPositiveButton("확인", null); // 초기에는 리스너를 null로 설정
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 생성 및 표시
        AlertDialog dialog = builder.create();
        dialog.show();

        // Positive 버튼 클릭 이벤트 설정
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newKeyword = input.getText().toString().trim();

            // 기존 키워드와 동일한 경우
            if (newKeyword.equals(keyword)) {
                dialog.dismiss();
                return;
            }

            // 빈 문자열인 경우
            if (newKeyword.isEmpty()) {
                Toast.makeText(this, "수정할 키워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 이미 존재하는 키워드인 경우
            if (existingKeywords.contains(newKeyword)) {
                Toast.makeText(this, "이미 존재하는 키워드입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            //  키워드 수정
            editKeyword(keywordId, newKeyword);
            // 수정된 키워드를 existingKeywords에 반영
            existingKeywords.remove(keyword);
            existingKeywords.add(newKeyword);
            dialog.dismiss();
        });
    }


    private void showDeleteKeywordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("키워드를 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        deleteKeyword(keywordId);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void editKeyword(Long keywordId, String newKeyword) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        // 수정할 키워드를 KeywordDto로 생성
        KeywordDto keywordDto = new KeywordDto(keywordId, newKeyword);

        ApiService apiService = RetrofitClient.getApiService();

        Call<KeywordDto> call = apiService.updateKeyword(keywordId, keywordDto);
        call.enqueue(new Callback<KeywordDto>() {
            @Override
            public void onResponse(Call<KeywordDto> call, Response<KeywordDto> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    keyword = response.body().getWord();
                    keywordTitle.setText("#" + keyword);
                    Log.d("editKeyword", "키워드 수정 성공: " + response.body());
                    Toast.makeText(KeywordProductListActivity.this, "키워드가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    fetchProductsByKeyword(keyword);
                } else {
                    Log.d("editKeyword", "키워드 수정 실패. 응답 코드: " + response.code() + "메시지: " + response.message());
                    Toast.makeText(KeywordProductListActivity.this, "키워드를 수정하지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KeywordDto> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("editKeyword", "네트워크 오류 발생: " + t.getMessage(), t);
                Toast.makeText(KeywordProductListActivity.this, "네트워크 오류 발생. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteKeyword(Long keywordId) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        ApiService apiService = RetrofitClient.getApiService();
        Call<String> call = apiService.deleteKeyword(keywordId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("deleteKeyword", "키워드 삭제 성공: " + response.body());
                    Toast.makeText(KeywordProductListActivity.this, "키워드가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(KeywordProductListActivity.this, InterestActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("deleteKeyword", "키워드 삭제 실패. 응답 코드: " + response.code() + "메시지: " + response.message());
                    Toast.makeText(KeywordProductListActivity.this, "키워드 삭제 실패, 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("deleteKeyword", "네트워크 오류 발생: " + t.getMessage(), t);
                Toast.makeText(KeywordProductListActivity.this, "네트워크 오류 발생. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductsByKeyword(String keyword) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<KeywordProductDto>> call = apiService.getProductsByKeyword(keyword);

        call.enqueue(new Callback<List<KeywordProductDto>>() {
            @Override
            public void onResponse(Call<List<KeywordProductDto>> call, Response<List<KeywordProductDto>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<KeywordProductDto> products = response.body();
                    displayProducts(products);
                    Log.d("FetchProductsByKeyword", "키워드 상품 목록 불러오기 성공: " + response.body());
                } else {
                    displayProducts(null); // 상품이 없을 때 null 처리
                    Log.d("FetchProductsByKeyword", "키워드 상품 목록 불러오기 실패. 응답 코드: " + response.code() + "메시지: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<KeywordProductDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("KeywordProductListActivity", "fetchProductsByKeyword - 네트워크 오류 발생: " + t.getMessage());
                Toast.makeText(KeywordProductListActivity.this, "키워드 상품 목록 불러오기 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProducts(List<KeywordProductDto> products) {
        if (productsContainer != null) {
            productsContainer.removeAllViews(); // 기존 뷰 제거


            /* TextView keywordTextView = new TextView(this);
            keywordTextView.setText("#" + keyword);
            keywordTextView.setTextSize(20);
            keywordTextView.setGravity(Gravity.CENTER);
            keywordTextView.setPadding(0, 100, 0, 50);
            productsContainer.addView(keywordTextView); // 텍스트 뷰 추가 */

            /*
            // 뒤로가기 버튼
            Button backButton = new Button(this);
            backButton.setText("뒤로가기");
            backButton.setOnClickListener(v -> finish()); // 버튼 클릭 시 현재 액티비티 종료
            productsContainer.addView(backButton); // 뒤로가기 버튼을 레이아웃에 추가
             */

            if (products == null || products.isEmpty()) {
                // 상품이 없을 때
                TextView noProductsTextView = new TextView(this);
                noProductsTextView.setText("상품이 없습니다.");
                noProductsTextView.setTextSize(15);
                noProductsTextView.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                layoutParams.gravity = Gravity.CENTER;
                noProductsTextView.setLayoutParams(layoutParams);

                productsContainer.addView(noProductsTextView); // 메시지를 레이아웃에 추가
            } else {
                // 상품이 있을 때
                LinearLayout rowLayout = null;
                for (KeywordProductDto product : products) {
                    // 각 상품 레이아웃 생성
                    LinearLayout productLayout = new LinearLayout(this);
                    productLayout.setOrientation(LinearLayout.HORIZONTAL);
                    productLayout.setPadding(10, 16, 10, 16);
                    productLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // 이미지 레이아웃
                    LinearLayout imageLayout = new LinearLayout(this);
                    imageLayout.setOrientation(LinearLayout.VERTICAL);
                    imageLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                    ));

                    // 이미지
                    ImageView productImage = new ImageView(this);
                    Glide.with(this)
                            .load(product.getImageUrl())
                            .into(productImage);
                    imageLayout.addView(productImage);

                    // 텍스트 레이아웃
                    LinearLayout textLayout = new LinearLayout(this);
                    textLayout.setOrientation(LinearLayout.VERTICAL);
                    textLayout.setPadding(30, 0, 16, 16);
                    textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f
                    ));

                    // title
                    TextView productTitle = new TextView(this);
                    productTitle.setText(product.getTitle());
                    productTitle.setTextSize(16);
                    productTitle.setTextColor(Color.BLACK);
                    textLayout.addView(productTitle);

                    // category
                    TextView productCategory = new TextView(this);
                    productCategory.setText(product.getCategory());
                    productCategory.setTextSize(14);
                    productCategory.setTextColor(Color.GRAY);
                    textLayout.addView(productCategory);

                    // price
                    TextView productPrice = new TextView(this);
                    productPrice.setText(String.format("%,.0f 원", product.getPrice()));
                    productPrice.setTextColor(Color.RED);
                    textLayout.addView(productPrice);

                    // 이미지와 텍스트 레이아웃을 한 행에 추가
                    productLayout.addView(imageLayout);
                    productLayout.addView(textLayout);

                    // 상품 레이아웃을 컨테이너에 추가
                    productsContainer.addView(productLayout);

                }
            }

        } else {
            Log.e("KeywordProductListActivity", "productContainer is null");
        }
    }
}

