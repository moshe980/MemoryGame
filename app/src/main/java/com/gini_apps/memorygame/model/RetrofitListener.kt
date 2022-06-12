package com.gini_apps.memorygame.model

import com.gini_apps.memorygame.model.entity.NumbersJson
import retrofit2.Response

fun interface RetrofitListener {
    fun success(response:Response<NumbersJson?>)

}