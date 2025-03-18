package com.example.bookmycaterer.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Retro {

    private const val BASE_URL = "https://fnqfq9sv-8000.inc1.devtunnels.ms/api/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        retrofit.create(ApiService::class.java)
    }
}
