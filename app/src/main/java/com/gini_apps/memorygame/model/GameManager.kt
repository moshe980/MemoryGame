package com.gini_apps.memorygame.model

import com.gini_apps.memorygame.model.entity.Card
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class GameManager(val cardsMap: Map<String, Card<String>>, val listener: OnTimerChangedListener) {
    var moveCounter = 2*cardsMap.size
    var firstCardFlippedIndex: String? = null
    var secondCardFlippedIndex: String? = null
    var timer: Double = 0.0
    var isGameOver = false
    private var matchCounter = 0
    private lateinit var executor: ScheduledExecutorService


    init {
        start()
    }

    fun start() {
        executor= Executors.newSingleThreadScheduledExecutor()
        matchCounter = 0
        isGameOver = false
        timer = 0.0
        moveCounter = 2
        firstCardFlippedIndex = null
        secondCardFlippedIndex = null
        runTimer()

    }

    fun cardTapped(cardId: Int) {
        firstCardFlippedIndex?.let { initSecondCard(cardId) } ?: run { initFirstCard(cardId) }
    }

    private fun initFirstCard(cardId: Int) {
        firstCardFlippedIndex = cardId.toString()
        cardsMap[firstCardFlippedIndex]?.isFaceUp = true
    }

    private fun initSecondCard(cardId: Int) {
        secondCardFlippedIndex = cardId.toString()
        cardsMap[secondCardFlippedIndex]?.isFaceUp = true
    }

    fun checkMatch() {
        if (isMatch()) {
            setMatch()
            if (isWon()) {
                isGameOver = true
            }

        } else {
            downMovesCounter()
        }

        clearFirstAndSecondCardIndexes()
    }

    private fun downMovesCounter() {
        moveCounter--
        checkGameOver()
    }

    private fun checkGameOver() {
        if (moveCounter == 0) {
            isGameOver = true
        }
    }

    private fun clearFirstAndSecondCardIndexes() {
        firstCardFlippedIndex = null
        secondCardFlippedIndex = null
    }

    private fun setMatch() {
        cardsMap[firstCardFlippedIndex]?.isMatched = true
        cardsMap[secondCardFlippedIndex]?.isMatched = true
        matchCounter++
    }

    private fun isMatch() = (cardsMap[firstCardFlippedIndex.toString()]?.content
            == cardsMap[secondCardFlippedIndex.toString()]?.content)

    private fun runTimer() {
        if (!isGameOver) {
            executor.scheduleAtFixedRate({
                timer += 0.001
                listener.onTimerChanged(timer)
            }, 0, 1, TimeUnit.MILLISECONDS)
        }
    }

    fun stopTimer() {
        executor.shutdown()
    }

    fun isWon(): Boolean = (cardsMap.size / 2 == matchCounter)

    fun interface OnTimerChangedListener {
        fun onTimerChanged(timerValue: Double)
    }

    fun getScore(): Int =
        ((1000 / timer) * 0.3 + (cardsMap.size * 1000 / 2) * 0.2 + (moveCounter * 1000) * 0.5).toInt()


}