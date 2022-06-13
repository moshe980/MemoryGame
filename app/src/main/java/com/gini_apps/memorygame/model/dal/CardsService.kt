package com.gini_apps.memorygame.model.dal

import com.gini_apps.memorygame.model.entity.Contents
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface CardsService {
    @GET("w8LSbsZb")
    suspend fun getNumbers(): Response<Contents>

    @GET("cZd0y0DL")
    suspend fun getLetters(): Response<Contents>

    companion object {
        private var BASE_URL = "https://pastebin.com/raw/"

        private var retrofitService: CardsService? = null
        private var gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        fun getInstance(): CardsService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(CardsService::class.java)
            }
            return retrofitService!!
        }

    }

}