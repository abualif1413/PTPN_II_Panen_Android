package com.ptpn.panen.handler;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {
    public static final String BASE_URL = "https://ptpn2.kreasidigital.co/";
    //public static final String BASE_URL = "http://172.16.1.116/ptpn/";

//    https://ptpn2.asikinonlineaja.com/api/kebun

    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
