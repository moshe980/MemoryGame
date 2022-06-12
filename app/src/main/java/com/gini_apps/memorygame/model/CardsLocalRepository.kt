package com.gini_apps.memorygame.model

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gini_apps.memorygame.model.entity.Card

class CardsLocalRepository(var level: Int) :CardDao {

    private var cardsMap = mutableMapOf<String, Card<String>>()


    private fun setCards() {
        for (i in 0 until level) {
            repeat(2) {
                val id = View.generateViewId().toString()
                cardsMap[id] = Card(id, "$i")
            }

        }
        val keys = cardsMap.keys.toMutableList().shuffled()
        val values = cardsMap.values.toMutableList().shuffled()

        keys.forEachIndexed { index, key ->

            cardsMap[key] = values[index]


        }

    }

    override  fun getCards(): MutableLiveData<Map<String, Card<String>>> {
        setCards()
        val data = MutableLiveData<Map<String, Card<String>>>()
        data.value = cardsMap
        return data
    }


}