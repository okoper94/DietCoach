package com.kys2024.dietcoach


import com.kys2024.dietcoach.data.BoardData
import com.kys2024.dietcoach.data.UserAccount


class G {
    companion object {
        var userAccount: UserAccount? = null

        var DB: MutableList<BoardData> = mutableListOf()

    }
}