package com.kys2024.dietcoach.data

data class LoadBoardData (
   var DB: MutableList<BoardData>
    )

data class BoardData(var userid:String,
                     var profileimg:String,
                     var no:Int,
                     var image:String,
                     var msg:String)