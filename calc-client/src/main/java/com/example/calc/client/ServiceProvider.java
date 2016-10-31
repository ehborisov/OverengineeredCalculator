package com.example.calc.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

enum ServiceProvider {

    INSTANCE;

    private final Retrofit retrofit;
    private final OkHttpClient okHttpClient;

    ServiceProvider() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Config.INSTANCE.connectionTimeout(), TimeUnit.SECONDS)
                .readTimeout(Config.INSTANCE.readTimeout(), TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Config.INSTANCE.baseUrl())
                .build();
    }

    CalculatorService buildService() {
        return retrofit.create(CalculatorService.class);
    }

    Retrofit getRetrofit() {
        return retrofit;
    }
}
