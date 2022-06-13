package com.gini_apps.memorygame.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gini_apps.memorygame.model.CardDao
import com.gini_apps.memorygame.model.CardsLocalRepository
import com.gini_apps.memorygame.model.CardsService
import com.gini_apps.memorygame.model.entity.Card
import com.gini_apps.memorygame.model.GameManager
import com.gini_apps.memorygame.model.entity.Contents
import kotlinx.coroutines.launch

class GameViewModel(private val myParm: String) : ViewModel() {
    private var _gameManager: MutableLiveData<GameManager>
    var cardIdChanged = MutableLiveData<Int>()
    var cardsChanged = MutableLiveData<MutableMap<String, Card<String>>>()
    val gameManager get() = _gameManager
    private val cardDao: CardDao

    init {
        val gameData = MutableLiveData<GameManager>()

        cardDao = CardsLocalRepository(myParm.toInt())
        gameData.value = GameManager(cardDao.getCards().value!!, listener = {
            runTimer()
        })

        _gameManager = gameData


    }

    fun getCardsFromApi() {
        val cardsMap = mutableMapOf<String, Card<String>>()

        viewModelScope.launch {
            val numbersJson: Contents? = CardsService.getInstance().getLetters().body()
            numbersJson?.let {
                val contents = it.contents
                for (i in 0 until myParm.toInt() * 2) {
                    val id = View.generateViewId().toString()
                    cardsMap["${i+1}"] = Card(id, "${contents[i].content}")

                }
                val keys = cardsMap.keys.toMutableList().shuffled()
                val values = cardsMap.values.toMutableList().shuffled()

                keys.forEachIndexed { index, key ->
                    cardsMap[key] = values[index]
                }

                _gameManager.value?.setCards(cardsMap)
            }
        }


    }

    fun cardTapped(cardId: Int) {
        gameManager.value?.cardTapped(cardId)
        cardIdChanged.apply {
            this.value = cardId
            this.postValue(cardIdChanged.value)

        }
    }

    fun checkMatch() {
        val firstCardIndex = gameManager.value?.firstCardFlippedIndex
        val secondCardIndex = gameManager.value?.secondCardFlippedIndex

        val firstCard = gameManager.value?.cardsMap?.get(firstCardIndex)
        val secondCard = gameManager.value?.cardsMap?.get(secondCardIndex)

        cardsChanged.apply {
            this.value = mutableMapOf()
        }.run {
            this.value?.put(firstCardIndex.toString(), firstCard!!)
            this.value?.put(secondCardIndex.toString(), secondCard!!)

        }
        gameManager.value?.checkMatch()
        cardsChanged.postValue(cardsChanged.value)

    }

    fun runTimer() {
        gameManager.postValue(gameManager.value)

    }

    fun start() {
        gameManager.value?.start()
        gameManager.postValue(gameManager.value)

    }


}