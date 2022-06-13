package com.gini_apps.memorygame.model.entity

import com.google.gson.annotations.SerializedName

data class Content<T>(
    @SerializedName(value = "number", alternate = ["Letter"]) val content: T
)