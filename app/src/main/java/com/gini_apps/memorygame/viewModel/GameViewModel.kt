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
import com.gini_apps.memorygame.model.entity.NumbersJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(val myParm: String) : ViewModel() {
    private var _gameManager: MutableLiveData<GameManager>
    var cardIdChanged = MutableLiveData<Int>()
    var cards = MutableLiveData<Map<String, Card<String>>>()
    var cardsChanged = MutableLiveData<MutableMap<String, Card<String>>>()
    val gameManager get() = _gameManager
    //private val cardDao: CardDao

    init {

        //cardDao = CardsLocalRepository(myParm.toInt())
        val cardsMap = mutableMapOf<String, Card<String>>()
        val data = MutableLiveData<GameManager>()
        viewModelScope.launch {
            val numbersJson: NumbersJson? = CardsService.getInstance().getNumbers().body()
            if (numbersJson != null) {
                var numbers = numbersJson.numbers
                for (i in 0 until myParm.toInt() * 2) {
                    val id = View.generateViewId().toString()
                    cardsMap[id] = Card(id, "${numbers[i].number}")

                }
                data.value = GameManager(cardsMap!!, listener = {
                    runTimer()
                })




            }
        }
        _gameManager = data

    }

    fun getCards() {
        val cardsMap = mutableMapOf<String, Card<String>>()
        val data = MutableLiveData<Map<String, Card<String>>>()
        viewModelScope.launch {
            val numbersJson: NumbersJson? = CardsService.getInstance().getNumbers().body()
            if (numbersJson != null) {
                var numbers = numbersJson.numbers
                for (i in 0 until myParm.toInt() * 2) {
                    val id = View.generateViewId().toString()
                    cardsMap[id] = Card(id, "${numbers[i].number}")

                }
                GameManager(cardsMap, listener = {
                    runTimer()
                })
                data.value = cardsMap

                cards.postValue(data.value)


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