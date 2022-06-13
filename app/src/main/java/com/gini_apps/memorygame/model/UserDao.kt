package com.gini_apps.memorygame.model

import com.gini_apps.memorygame.model.entity.User

interface UserDao {
    fun saveUser(user: User)
    fun getUsers(): MutableSet<String>
    fun clear()
}