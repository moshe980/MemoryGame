package com.gini_apps.memorygame.model

import androidx.lifecycle.MutableLiveData
import com.gini_apps.memorygame.model.entity.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardApiRepository(var level: Int) {
    private var cardsMap = mutableMapOf<String, Card<String>>()


    fun getCards(listener: RetrofitListener): MutableLiveData<Map<String, Card<String>>> {

        val data = MutableLiveData<Map<String, Card<String>>>()
        data.value = cardsMap
        return data
    }
}