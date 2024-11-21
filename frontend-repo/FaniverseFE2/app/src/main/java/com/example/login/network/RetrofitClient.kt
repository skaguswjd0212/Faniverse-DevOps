package com.example.login.network

import com.example.login.api.ApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.CookieJar
import okhttp3.Cookie
import okhttp3.HttpUrl
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.CookieStore
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://faniversebe.onrender.com/" // 로컬에서 실행할 때 서버 주소: http://10.0.2.2:8080/

    private val cookieJar = object : CookieJar {
        private val cookies = mutableListOf<Cookie>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this.cookies.addAll(cookies)
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookies.filter { it.matches(url) }
        }
    }



    private val instance: Retrofit
        get() {
            if (retrofit == null) {

                val client = OkHttpClient.Builder()
                    //.connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                    //.readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                    //.writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                    .cookieJar(cookieJar)
                    .build()

                val gson = GsonBuilder().setLenient().create();

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }

    @JvmStatic
    fun getApiService(): ApiService {
        return instance.create(ApiService::class.java)
    }
}
