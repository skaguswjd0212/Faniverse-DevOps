package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.api.ApiService;
import com.example.login.model.ProductDetailsResponse;
import com.example.login.model.ProductListResponse;
import com.example.login.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AfterSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private EditText searchEditText;
    private ImageView backButton, filterIcon, searchButton;
    private String query;
    private LinearLayout homeLayout;
    private LinearLayout interestLayout;
    private LinearLayout chatLayout;
    private TextView noResultsTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersearch);

        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        backButton = findViewById(R.id.back_button);
        filterIcon = findViewById(R.id.filter_icon);
        searchButton = findViewById(R.id.search_icon);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        noResultsTextView = findViewById(R.id.no_results_text_view);
        noResultsTextView.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);


       // Intent로부터 검색어와 정렬 옵션 가져오기
       String searchWord = getIntent().getStringExtra("searchWord");
       String sortBy = getIntent().getStringExtra("sortBy");

       Log.d("AfterSearchActivity", "검색어: " + searchWord);
       Log.d("AfterSearchActivity", "필터: " + sortBy);

       if (searchWord != null) {
           searchEditText.setText(searchWord); // 검색창에 검색어 유지
           searchEditText.setSelection(searchEditText.getText().length()); // 커서를 텍스트 끝으로 이동
       }

       if (sortBy != null) {
           // 필터가 적용된 경우 필터링된 결과 가져오기
           filterProducts(searchWord, sortBy);
       } else {
           // 필터가 없는 경우 기본 검색 결과 가져오기
           searchProducts(searchWord);
       }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterSearchActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWord = searchEditText.getText().toString();

                if (!TextUtils.isEmpty(searchWord)) {
                    Intent intent = new Intent(AfterSearchActivity.this, SearchFilterActivity.class);
                    intent.putExtra("searchWord", searchWord); // 검색어를 intent에 추가
                    startActivity(intent);
                    //filterProducts(searchWord, sortBy);
                } else {
                    Toast.makeText(AfterSearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWord = searchEditText.getText().toString();
                if (!TextUtils.isEmpty(searchWord)) {
                    searchProducts(searchWord);
                } else {
                    Toast.makeText(AfterSearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String searchWord = searchEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(searchWord)) {
                        searchProducts(searchWord);
                    } else {
                        Toast.makeText(AfterSearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });


        // 홈 레이아웃 클릭 이벤트 설정
        homeLayout = findViewById(R.id.home_layout);
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterSearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // 관심 레이아웃 클릭 이벤트 설정
        interestLayout = findViewById(R.id.interest_layout);
        interestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterSearchActivity.this, InterestActivity.class);
                startActivity(intent);
            }
        });

        // 채팅 레이아웃 클릭 이벤트 설정
        chatLayout = findViewById(R.id.chat_layout);
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterSearchActivity.this,  ChatHome.class);
                startActivity(intent);
            }
        });
    }


    // 검색 결과 조회
    private void searchProducts(String searchWord) {
        Call<List<ProductDetailsResponse>> call = RetrofitClient.getApiService().searchProducts(searchWord, "latest");
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<ProductDetailsResponse>>() {
            @Override
            public void onResponse(Call<List<ProductDetailsResponse>> call, Response<List<ProductDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    if (response.body().isEmpty()) {
                        noResultsTextView.setVisibility(View.VISIBLE);
                        noResultsTextView.setText("'" + searchWord + "' 포함한 검색 결과가 없습니다.");
                    } else {
                        noResultsTextView.setVisibility(View.GONE);
                        for (ProductDetailsResponse productDetailsResponse : response.body()) {
                            // Product 객체로 변환 후 추가
                            productList.add(new Product(
                                    productDetailsResponse.getTitle(),
                                    productDetailsResponse.getCategory(),
                                    productDetailsResponse.getPrice(),
                                    productDetailsResponse.getImageUrl()
                            ));
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("AfterSearchActivity", "검색 결과 불러오기 실패. 응답 코드: " + response.code());
                    Toast.makeText(AfterSearchActivity.this, "검색 결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDetailsResponse>> call, Throwable t) {
                Log.e("AfterSearchActivity", "오류 발생: " + t.getMessage(), t);
                Toast.makeText(AfterSearchActivity.this, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    // 정렬된 검색 결과 조회
    private void filterProducts(String searchWord, String sortBy) {
        Call<List<ProductDetailsResponse>> call = RetrofitClient.getApiService().filterProducts(searchWord, sortBy);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<ProductDetailsResponse>>() {
            @Override
            public void onResponse(Call<List<ProductDetailsResponse>> call, Response<List<ProductDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    if (response.body().isEmpty()) {
                        noResultsTextView.setVisibility(View.VISIBLE);
                        noResultsTextView.setText("'" + searchWord + "' 포함한 검색 결과가 없습니다.");
                    } else {
                        noResultsTextView.setVisibility(View.GONE);
                        for (ProductDetailsResponse productDetailsResponse : response.body()) {
                            // Product 객체로 변환 후 추가
                            productList.add(new Product(
                                    productDetailsResponse.getTitle(),
                                    productDetailsResponse.getCategory(),
                                    productDetailsResponse.getPrice(),
                                    productDetailsResponse.getImageUrl()
                            ));
                        }

                    }
                    productAdapter.notifyDataSetChanged();

                } else {
                    try {
                        Log.e("AfterSearchActivity", String.format("정렬된 검색 결과 불러오기 실패. 응답 코드: %d, 오류 내용: %s",
                                response.code(), response.errorBody().string()));
                        Toast.makeText(AfterSearchActivity.this, "검색 결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Log.e("AfterSearchActivity", "오류 내용을 가져오는 중 예외 발생", e);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<ProductDetailsResponse>> call, Throwable t) {
                // 네트워크 오류 또는 예외 발생 시 처리
                Log.e("AfterSearchActivity", "오류 발생: " + t.getMessage(), t);
                Toast.makeText(AfterSearchActivity.this, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
