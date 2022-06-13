package com.gini_apps.memorygame.model.entity

import com.google.gson.annotations.SerializedName


data class Contents(
    @SerializedName(value = "numbers", alternate = ["Letters"]) val contents: List<Content<*>>
)