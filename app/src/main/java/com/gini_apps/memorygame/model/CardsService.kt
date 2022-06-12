package com.gini_apps.memorygame.model

import com.gini_apps.memorygame.model.entity.NumbersJson
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CardsService {
    @GET("/raw/w8LSbsZb")
    suspend fun getNumbers(): Response<NumbersJson>

    companion object {
        private var BASE_URL = "https://pastebin.com/"

        var retrofitService: CardsService? = null
        fun getInstance() : CardsService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(CardsService::class.java)
            }
            return retrofitService!!
        }

    }

}