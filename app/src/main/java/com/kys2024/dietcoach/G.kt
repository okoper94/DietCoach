package com.kys2024.dietcoach


import android.app.Application
import com.kys2024.dietcoach.data.UserAccount


class G : Application() {
    companion object {
        var userAccount: UserAccount? = null
    }

    fun setUserAccount(account: UserAccount) {
        userAccount = account
    }

    fun getUserAccount(): UserAccount? {
        return userAccount
    }
}