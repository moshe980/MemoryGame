package com.gini_apps.memorygame.model.entity

data class Card<T : Comparable<T>>(
    val id: String,
    val content: T,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false

)