package com.gt.alimert.emvnfclib.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gt.alimert.emvnfclib.data.model.interceptor.RequestResponseInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * @author AliMertOzdemir
 * @class NetworkManager
 * @created 28.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class NetworkManager {

    private Retrofit mRetrofit;

    public NetworkManager(String endpointUrl, int timeout) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .addConverterFactory(GsonConverterFactory.create(initGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initOkHttpClient(timeout))
                .build();
    }

    public ApiService init() {
        return mRetrofit.create(ApiService.class);
    }

    private Gson initGson() {
        return new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    private OkHttpClient initOkHttpClient(int timeout) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(initRequestResponseInterceptor());
        httpClientBuilder.addInterceptor(initHttpLoggingInterceptor());
        httpClientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        httpClientBuilder.callTimeout(timeout, TimeUnit.MILLISECONDS);
        return httpClientBuilder.build();
    }

    private RequestResponseInterceptor initRequestResponseInterceptor() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-MPOS-Request-Header", "{\"appVersion\":1011,\"deviceId\":\"498EA0D040769BB4FB9BE73CE9E32A4C654C82C17515D5DF91EF5240BF51160F\",\"requestToken\":0,\"sessionId\":\"f4e4a57c-f876-4367-af15-f13de32120c5\",\"terminalNum\":30691802,\"timestamp\":\"1587810936\",\"userOpaqueId\":\"R2RyR043bXN2cWdnYjRORnN3d3lWdz09\"}");
        return new RequestResponseInterceptor(requestHeader);
    }

    private HttpLoggingInterceptor initHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.d(TAG, message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
