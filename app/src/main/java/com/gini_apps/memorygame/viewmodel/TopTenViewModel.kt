package com.gini_apps.memorygame.viewmodel

import androidx.lifecycle.ViewModel
import com.gini_apps.memorygame.model.dal.UserDao
import com.gini_apps.memorygame.model.UserSpRepository
import com.gini_apps.memorygame.model.entity.User

class TopTenViewModel : ViewModel() {
    private val userDao: UserDao

    init {
        userDao = UserSpRepository()

    }

    fun saveUser(user: User){
        userDao.saveUser(user)
    }
    fun getUsers()=userDao.getUsers()

    fun clearUsers(){
        userDao.clear()
    }
}