package com.example.proyectofinal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;

    // Instancia única de Retrofit
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Base URL de tu backend Spring Boot
                    .addConverterFactory(GsonConverterFactory.create()) // Conversor JSON <-> Java
                    .build();
        }
        return retrofit;
    }

    // Devuelve una instancia del servicio con los endpoints definidos
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
