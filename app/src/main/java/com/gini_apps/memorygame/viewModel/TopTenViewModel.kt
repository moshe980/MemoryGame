package com.gini_apps.memorygame.viewModel

import androidx.lifecycle.ViewModel
import com.gini_apps.memorygame.model.UserDao
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