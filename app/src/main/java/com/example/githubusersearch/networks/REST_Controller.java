package com.example.githubusersearch.networks;

import com.example.githubusersearch.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class REST_Controller {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static HttpLoggingInterceptor.Level getInterceptorLevel() {
        if (BuildConfig.DEBUG)
            return HttpLoggingInterceptor.Level.BODY;
        else
            return HttpLoggingInterceptor.Level.NONE;
    }

    private static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(getInterceptorLevel());

        return httpLoggingInterceptor;
    }

    public static <S> S create(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
