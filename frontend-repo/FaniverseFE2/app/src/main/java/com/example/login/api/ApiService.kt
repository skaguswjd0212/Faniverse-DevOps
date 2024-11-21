package com.example.login.api

import com.example.login.Comment
import com.example.login.model.BoardCreateRequest
import com.example.login.model.BoardResponse
import com.example.login.model.BoardUpdateForm
import com.example.login.model.ChatRoomResponse
import com.example.login.model.CommentCreateRequest
import com.example.login.model.CommentResponse
import com.example.login.model.CommentUpdateForm
import com.example.login.model.KeywordDto
import com.example.login.model.KeywordProductDto
import com.example.login.model.LoginRequestDto
import com.example.login.model.LoginResponseDto
import com.example.login.model.LogoutResponseDto
import com.example.login.model.MessageRequest
import com.example.login.model.MessageResponse
import com.example.login.model.Post
import com.example.login.model.PostCreateRequest
import com.example.login.model.PostResponse
import com.example.login.model.PostUpdateForm
import com.example.login.model.ProductDetailsResponse
import com.example.login.model.ProductListResponse
import com.example.login.model.ProductListResponseWrapper
import com.example.login.model.RegisterRequestDto
import com.example.login.model.RegisterResponseDto
import com.example.login.model.WishlistProductDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    // 로그인
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequestDto): Call<LoginResponseDto>

    // 로그아웃
    @POST("auth/logout")
    fun logout(): Call<LogoutResponseDto>

    // 사용자 조회
    @GET("users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<RegisterResponseDto>

    // 회원가입
    @POST("users/register")
    fun registerUser(@Body registerRequest: RegisterRequestDto): Call<RegisterResponseDto>


    // ---위시리스트(관심상품)---
    // 사용자의 wishlist 조회
    @GET("wishlist/user")
    fun getWishlistProducts(): Call<List<WishlistProductDto>>

    // wishlist 항목 추가
    @POST("wishlist/add")
    fun addWishlistItem(@Query("productId") productId: Long): Call<String>

    // wishlist 항목 삭제
    @DELETE("wishlist/remove/{wishlistId}")
    fun removeWishlistItem(@Path("wishlistId") wishlistId: Long): Call<String>


    // ---키워드---
    // 키워드 추가
    @POST("keywords/add")
    fun addKeyword(@Body keywordDto: KeywordDto): Call<KeywordDto>

    // 키워드 수정
    @PUT("keywords/{id}")
    fun updateKeyword(@Path("id") id: Long, @Body keywordDto: KeywordDto): Call<KeywordDto>

    // 키워드 삭제
    @DELETE("keywords/remove")
    fun deleteKeyword(@Query("keywordId") keywordId: Long): Call<String>

    // 사용자의 키워드 목록 조회
    @GET("keywords/user")
    fun getUserKeywords(): Call<List<KeywordDto>>
    //fun getUserKeywords(@Query("userId") userId: Long): Call<List<KeywordDto>>

    // 키워드 포함한 상품 목록 조회
    @GET("keywords/")
    fun getProductsByKeyword(@Query("keyword") keyword: String): Call<List<KeywordProductDto>>


    //ProductController
    // 전체 상품 리스트 출력
    @GET("/products/list")
    fun getProducts(): Call<List<ProductListResponse>>

    @Multipart
    @POST("/products/generalproducts/register")
    fun registerGeneralProduct(
        @Part("title") title: RequestBody,
        @Part("category") category: RequestBody,
        @Part("content") content: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<String>

    // 경매 상품 등록
    @Multipart
    @POST("/products/auctionproducts/register")
    fun registerAuctionProduct(
        @Part("title") title: RequestBody,
        @Part("category") category: RequestBody,
        @Part("content") content: RequestBody,
        @Part("startingPrice") startingPrice: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("endDate") endDate: RequestBody
    ): Call<String>

    // 상품 상세 정보 조회
    @GET("/products/{productId}")
    fun getProductDetail(
        @Path("productId") productId: Long
    ): Call<ProductDetailsResponse>

    // 판매 상품 상태 변경
    @PUT("/products/{productId}/status")
    fun updateProductStatus(
        @Path("productId") productId: Long,
        @Query("status") status: String
    ): Call<Void>

    // 상품 삭제
    @DELETE("/products/{productId}")
    fun deleteProduct(
        @Path("productId") productId: Long
    ): Call<Void>

    // 홈 화면 - 최근 등록된 상품 조회 및 카테고리별 상품 조회
    @GET("/products/home")
    fun getHomePage(): Call<Map<String, List<ProductListResponse>>>


    // ProductCategoryController
    // 최상위 카테고리 조회 (음악, 스포츠, 애니, 게임)
    @GET("/categories/root")
    fun getRootCategories(): Call<List<String>>

    // 특정 상위 카테고리의 하위 카테고리 조회
    @GET("/categories/subcategories/{parentTitle}")
    fun getSubCategories(
        @Path("parentTitle") parentTitle: String
    ): Call<List<String>>


    //GeneralProductController
    // 상품 수정
    @Multipart
    @PUT("/products/update/{productId}")
    fun updateProduct(
        @Path("productId") productId: Long,
        @Part("title") title: RequestBody,
        @Part("category") category: RequestBody,
        @Part("content") content: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Call<String>

    //AuctionProductController
    // 경매 입찰 등록
    @POST("/products/{productId}/bids")
    fun placeBid(
        @Path("productId") productId: Long,
        @Body bidRequest: Map<String, Any>
    ): Call<String>

    // 경매 입찰 취소
    @DELETE("/products/bids/{bidId}")
    fun cancelBid(
        @Path("bidId") bidId: Long
    ): Call<String>

    // 경매 금액 지불 여부 확인 (판매자 수동)
    @PUT("/products/{productId}/confirm-payment")
    fun confirmPayment(
        @Path("productId") productId: Long
    ): Call<String>


    //SearchController
    // 검색 결과 조회
    @GET("/search/results")
    fun searchProducts(
        @Query("searchWord") searchWord: String,
        @Query("sortBy") sortBy: String = "latest"
    ): Call<List<ProductDetailsResponse>>

    // 최근 검색어 조회
    @GET("/search/recent")
    fun getRecentSearches(): Call<List<String>>

    // 최근 검색어 추가
    @POST("/search/recent")
    @FormUrlEncoded
    fun addRecentSearch(
        @Field("searchWord") searchWord: String
    ): Call<Void>

    // 상품 필터링 및 정렬
    @GET("/search/results/filter")
    fun filterProducts(
        @Query("keyword") keyword: String,
        @Query("sortBy") sortBy: String = "latest"
    ): Call<List<ProductDetailsResponse>>



// ==========================
    // 채팅 API
    // ==========================

    // 메시지 전송
    @POST("chat/send/{roomId}")
    fun sendMessage(
        @Path("roomId") roomId: Long,
        @Body messageRequest: MessageRequest
    ): Call<Void>

    // 채팅방 메시지 리스트 가져오기
    @GET("chat/list/{roomId}")
    fun getMessages(
        @Path("roomId") roomId: Long,
        @Query("lastMessageId") lastMessageId: Long
    ): Call<List<MessageResponse>>

    // 채팅방 리스트 가져오기
    @GET("chatroom/list")
    fun getChatRooms(): Call<List<ChatRoomResponse>>

    // 채팅방 삭제
    @DELETE("chatroom/{roomId}")
    fun deleteChatRoom(
        @Path("roomId") roomId: Long
    ): Call<Void>

    // 채팅방 차단
    @POST("chatroom/block/{roomId}")
    fun blockChatRoom(
        @Path("roomId") roomId: Long
    ): Call<Void>

    // 채팅방 생성
    @POST("chatroom/create/{sellerId}/{productId}")
    fun createChatRoom(
        @Path("sellerId") sellerId: Long,
        @Path("productId") productId: Long
    ): Call<Void>

    // ==========================
    // 커뮤니티 API
    // ==========================

    // 게시판 생성
    @POST("board/create")
    fun createBoard(
        @Body boardCreateRequest: BoardCreateRequest
    ): Call<Void>

    // 게시판 검색
    @GET("board/search")
    fun searchBoardsByName(
        @Query("name") name: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<List<BoardResponse>>

    // 게시판 리스트 가져오기
    @GET("board/list")
    fun getBoardList(
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<List<BoardResponse>>

    // 게시판 수정
    @PATCH("board/update/{boardId}")
    fun updateBoard(
        @Path("boardId") boardId: Long,
        @Body boardUpdateForm: BoardUpdateForm
    ): Call<Void>

    // 게시판 삭제
    @DELETE("board/delete/{boardId}")
    fun deleteBoard(
        @Path("boardId") boardId: Long
    ): Call<Void>

    // 게시판 좋아요
    @POST("board/likes/{boardId}")
    fun likeBoard(
        @Path("boardId") boardId: Long
    ): Call<Void>

    // 댓글 생성
    @POST("comment/create/{postId}")
    fun createComment(
        @Path("postId") postId: Long,
        @Body commentCreateRequest: CommentCreateRequest
    ): Call<Void>

    // 댓글 리스트 가져오기
    @GET("comment/list/{postId}")
    fun getCommentList(
        @Path("postId") postId: Long,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<List<CommentResponse>>

    // 댓글 수정
    @PATCH("comment/update/{commentId}")
    fun updateComment(
        @Path("commentId") commentId: Long,
        @Body commentUpdateForm: CommentUpdateForm
    ): Call<Void>

    // 댓글 삭제
    @DELETE("comment/delete/{commentId}")
    fun deleteComment(
        @Path("commentId") commentId: Long
    ): Call<Void>

    // 게시물 생성
    @POST("post/create")
    fun createPost(
        @Body postCreateRequest: PostCreateRequest
    ): Call<Void>

    // 게시물 검색
    @GET("post/search")
    fun searchPostsByTitle(
        @Query("boardId") boardId: Long,
        @Query("title") title: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<List<PostResponse>>

    // 특정 게시물 가져오기
    @GET("post/{postId}")
    fun getPost(
        @Path("postId") postId: Long
    ): Call<PostResponse>

    // 게시물 수정
    @PATCH("post/update/{postId}")
    fun updatePost(
        @Path("postId") postId: Long,
        @Body postUpdateForm: PostUpdateForm
    ): Call<Void>

    // 게시물 삭제
    @DELETE("post/delete/{postId}")
    fun deletePost(
        @Path("postId") postId: Long
    ): Call<Void>

    // 게시물 좋아요
    @POST("post/likes/{postId}")
    fun likePost(
        @Path("postId") postId: Long
    ): Call<Void>

    //홈 최근 등록된 상품 가져오기
    @GET("/products/home")
    fun getRecentProducts(): Call<ProductListResponseWrapper>

    // 기존에 있던 코드
    @GET("getPosts")
    fun getPosts(@Query("community_id") communityId: Int): Call<List<Post>>

    // 댓글 가져오기
    @GET("posts/{postId}/comments")
    fun getComments(@Path("postId") postId: Int): Call<List<Comment>>

    // 댓글 추가하기
    @POST("posts/{postId}/comments")
    fun addComment(@Path("postId") postId: Int, @Body comment: Comment): Call<Void>
}

