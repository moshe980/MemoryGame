package com.gini_apps.memorygame.model

import androidx.lifecycle.MutableLiveData
import com.gini_apps.memorygame.model.entity.Card

interface CardDao {
   fun getCards(): MutableLiveData<Map<String, Card<String>>>
}