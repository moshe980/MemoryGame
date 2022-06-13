package com.gini_apps.memorygame.model

import android.content.Context
import com.gini_apps.memorygame.MyApplication.Companion.appContext
import com.gini_apps.memorygame.model.dal.UserDao
import com.gini_apps.memorygame.model.entity.User
import com.google.gson.Gson
import java.lang.ref.WeakReference

class UserSpRepository : UserDao {
    private val _context = WeakReference(appContext)
    private val mcontext
        get() = _context.get()
    private var users = mutableSetOf<String>()
    private val tableName = "USERS"
    private var sharedPreference = mcontext?.getSharedPreferences("topTen", Context.MODE_PRIVATE)


    override fun saveUser(user: User) {
        users.addAll(getUsers())
        users.add(Gson().toJson(user))
        val editor = sharedPreference?.edit()
        editor?.putStringSet(tableName, users)

        editor?.apply()

    }

    override fun getUsers(): MutableSet<String> {
        val tmpUsers = mutableSetOf<String>()
        sharedPreference?.getStringSet(tableName, null)?.let { tmpUsers.addAll(it) }
        return tmpUsers
    }

    override fun clear() {
        val editor = sharedPreference?.edit()
        editor?.clear()
        editor?.apply()

    }

}