package com.kys2024.dietcoach.data

import android.provider.ContactsContract.CommonDataKinds.Nickname

data class BoardData(var nickname:String,
                     var profileimg:String?,
                     var no:String,
                     var image:String,
                     var msg:String)