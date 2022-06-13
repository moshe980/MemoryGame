package com.gini_apps.memorygame.model.dal

import androidx.lifecycle.MutableLiveData
import com.gini_apps.memorygame.model.entity.Card

interface CardDao {
   fun getCards(): MutableLiveData<MutableMap<String, Card<String>>>
}