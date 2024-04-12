package com.kys2024.dietcoach.data

import android.net.Uri

data class UserAccount(var uid:String="",var password:String="", var nickname:String="")

data class UserProflieImgUri(var uri: Uri?=null)
