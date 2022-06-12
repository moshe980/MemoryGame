package com.gini_apps.memorygame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memorygame.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding
        get() = _binding!!
    private val extraLevel = "level"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playBtn.setOnClickListener {
            when (binding.levels.checkedRadioButtonId) {
                binding.easyBtn.id -> playGame("4")
                binding.mediumBtn.id -> playGame("6")
                binding.hardBtn.id -> playGame("10")
            }
        }
        binding.topTenBtn.setOnClickListener {
            val intent = Intent(this, TopTenActivity::class.java)
            startActivity(intent)
        }

    }

    private fun playGame(level: String) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(extraLevel, level)
        }
        startActivity(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}