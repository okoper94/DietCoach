package com.kys2024.dietcoach


import android.app.Application
import com.kys2024.dietcoach.data.FoodTime
import com.kys2024.dietcoach.data.TodayDate
import com.kys2024.dietcoach.data.UserAccount


class G : Application() {
    companion object {
        var userAccount: UserAccount? = null

        var foodtime: FoodTime? =null
        var todaydate: TodayDate? =null
    }

    fun setUserAccount(account: UserAccount) {
        userAccount = account
    }

    fun getUserAccount(): UserAccount? {
        return userAccount
    }

}