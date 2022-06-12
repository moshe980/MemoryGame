package com.gini_apps.memorygame.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.res.TypedArray
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.memorygame.R
import com.example.memorygame.databinding.ActivityGameBinding
import com.gini_apps.memorygame.model.entity.User
import com.gini_apps.memorygame.viewModel.GameViewModel
import com.gini_apps.memorygame.viewModel.MyViewModelFactory
import com.gini_apps.memorygame.viewModel.TopTenViewModel
import java.security.AccessController.getContext

class GameActivity : AppCompatActivity() {
    private var _binding: ActivityGameBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var gameViewModel: GameViewModel
    private lateinit var topTenViewModel: TopTenViewModel
    private lateinit var cardsImages: TypedArray
    private var cardsFlippedCounter = 0
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val level = intent.getStringExtra("level")

        dialog = Dialog(this)
        cardsImages = resources.obtainTypedArray(R.array.cards)
        val factory = MyViewModelFactory(level!!)
        gameViewModel = ViewModelProvider(this, factory)[GameViewModel::class.java]
        topTenViewModel = ViewModelProvider(this)[TopTenViewModel::class.java]
        gameViewModel.getCards()



        gameViewModel.cards.observe(this){

            println(it)
            initUI(level.toInt())

            gameViewModel.runTimer()

        }

        gameViewModel.cardIdChanged.observe(this) {
            revealCard(it)

        }

        gameViewModel.cardsChanged.observe(this) { it ->
            it.forEach {
                if (it.value.isMatched) {
                    removeCard(it.key.toInt())
                } else {
                    flipBackCard(it.key.toInt())
                }
            }
        }

        gameViewModel.gameManager.observe(this) {
            if (!it.isGameOver) {
                binding.timerTV.text = String.format("%.2f", it.timer)
            } else {
                it.stopTimer()
                blockClick()
                if (it.isWon()) {
                    openWinDialog()
                } else {
                    openLoseDialog()

                }
            }
            binding.movesTv.text = getString(R.string.moves, it.moveCounter)


        }



        binding.flow.referencedIds.map<ImageView>(::findViewById)
            .forEach { btn ->
                btn.setOnClickListener {
                    cardTapped(it as ImageView)
                }
            }


    }

    private fun openWinDialog() {
        dialog.setContentView(R.layout.win_dialog)
        dialog.setCanceledOnTouchOutside(false)
        val scoreTV = dialog.findViewById<TextView>(R.id.scoreTV)
        val nameET = dialog.findViewById<EditText>(R.id.nameET)
        val submitBtn = dialog.findViewById<Button>(R.id.submitBtn)
        scoreTV.text = getString(R.string.score, gameViewModel.gameManager.value?.getScore())

        submitBtn.setOnClickListener {
            val user = User(nameET.text.toString(), scoreTV.text.toString())
            topTenViewModel.saveUser(user)
            dialog.dismiss()
            finish()
        }


        dialog.show()
    }

    private fun openLoseDialog() {
        dialog.setContentView(R.layout.lose_dialog)
        dialog.setCanceledOnTouchOutside(false)
        val backBtn = dialog.findViewById<Button>(R.id.backBtn)
        val tryAgainBtn = dialog.findViewById<Button>(R.id.tryAgainBtn)

        backBtn.setOnClickListener {
            dialog.dismiss()
            finish()

        }
        tryAgainBtn.setOnClickListener {

            gameViewModel.start()
            allowClick()
            dialog.dismiss()

        }

        dialog.show()
    }

    private fun revealCard(cardId: Int) {
        blockClick(cardId)
        val currentCard = findViewById<ImageView>(cardId)
        revealCardAnimate(currentCard)
        if (cardsFlippedCounter >= 2) {
            Handler(Looper.getMainLooper()).postDelayed({
                gameViewModel.checkMatch()
                allowClick()
            }, 1000)
            blockClick()
            cardsFlippedCounter = 0

        }

    }

    private fun blockClick() {
        binding.flow.referencedIds.map<ImageView>(::findViewById)
            .forEach { btn ->
                btn.isClickable = false
            }

    }

    private fun blockClick(cardId: Int) {
        binding.flow.referencedIds.map<ImageView>(::findViewById)
            .filter { image -> image.id == cardId }[0].isClickable = false

    }

    private fun allowClick() {
        binding.flow.referencedIds.map<ImageView>(::findViewById)
            .forEach { btn ->
                btn.isClickable = true
            }

    }

    private fun revealCardAnimate(currentCard: ImageView) {

        val anime1 = initAnimation(currentCard, 1f, 0f)
        val anime2 = initAnimation(currentCard, 0f, 1f)
        val context=this
        anime1.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {

                super.onAnimationEnd(animation)
                anime2.start()
                currentCard.setImageResource(R.drawable.back)
                val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                var paint= Paint()

                paint.color= Color.WHITE
                paint.style = Paint.Style.FILL
                canvas.drawRect(RectF(15F, 0f, 85f, 500f),paint)

                paint.color= Color.BLACK
                paint.style = Paint.Style.FILL
                paint.textSize= 30f
                val text= gameViewModel.gameManager.value!!.cardsMap[currentCard.id.toString()]?.content!!
                canvas.drawText(text, 35f, 60f,paint)
                currentCard.setImageBitmap(bitmap)



                /*currentCard.setImageResource(
                    cardsImages.getResourceId(
                        cardsImages.getIndex(
                            gameViewModel.gameManager.value!!.cardsMap[currentCard.id.toString()]?.content!!.toInt()
                        ), 0
                    )
                )*/

            }
        })
        anime1.start()
    }

    private fun flipBackCardAnimate(currentCard: ImageView) {

        val anime1 = initAnimation(currentCard, 1f, 0f)
        val anime2 = initAnimation(currentCard, 0f, 1f)

        anime1.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {

                super.onAnimationEnd(animation)
                anime2.start()

                currentCard.setImageResource(R.drawable.back)


            }
        })
        anime1.start()
    }

    private fun initAnimation(currentCard: ImageView, xStart: Float, xEnd: Float): ObjectAnimator {
        val anime = ObjectAnimator.ofFloat(currentCard, "scaleX", xStart, xEnd)
        anime.interpolator = DecelerateInterpolator()
        return anime
    }

    private fun cardTapped(imageView: ImageView) {

        gameViewModel.cardTapped(imageView.id)
        cardsFlippedCounter++
    }

    private fun flipBackCard(cardId: Int) {
        val currentCard = findViewById<ImageView>(cardId)
        flipBackCardAnimate(currentCard)

    }

    private fun removeCard(cardId: Int) {
        findViewById<ImageView>(cardId).animate().alpha(0f).duration = 1000
    }

    private fun initUI(level: Int) {
        binding.flow.setMaxElementsWrap(level / 2)
        gameViewModel.gameManager.value?.cardsMap?.forEach {
            val imageView = ImageView(this)
            imageView.apply {
                this.id = it.key.toInt()
                this.setImageResource(R.drawable.back)
            }
            binding.root.addView(imageView)
            binding.flow.referencedIds += imageView.id

            with(imageView) {
                this.layoutParams.width = 0
                this.layoutParams.height = 0
                this.setOnClickListener { cardTapped(imageView) }
            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}