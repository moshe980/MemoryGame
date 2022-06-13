package com.gini_apps.memorygame.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.memorygame.databinding.ActivityTopTenBinding
import com.gini_apps.memorygame.model.entity.User
import com.gini_apps.memorygame.viewmodel.TopTenViewModel
import com.google.gson.Gson

class TopTenActivity : AppCompatActivity() {
    private var _binding: ActivityTopTenBinding? = null
    private val binding
        get() = _binding!!
    private var text = ""
    private lateinit var topTenViewModel: TopTenViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTopTenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topTenViewModel = ViewModelProvider(this).get(TopTenViewModel::class.java)
        val set = topTenViewModel.getUsers()
        set.map { json -> Gson().fromJson(json, User::class.java) }
            .sortedWith(compareByDescending { it.score }).forEachIndexed(::display)
        binding.nameTV.text = text

    }

    private fun display(i: Int, user: User) {
        text += "${i + 1}. ${user.name} | score: ${user.score}\n\n"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}