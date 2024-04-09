package com.psg2024.ex68retrofitmarketapp

import com.kys2024.dietcoach.data.MarketItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface RetrofitService {

    // 1. POST방식으로 데이터를 보내기..
    @Multipart
    @POST("/DietCoach/usertable.php")
    fun postdataToServer(@PartMap dataPart:Map<String, String>,
                         @Part filePart: MultipartBody.Part?): Call<String>
    //2. GET방식으로 json array 데이터를 받아와서 파싱하여 결과 받는 코드 만들어줘..
    @GET("/DietCoach/board.php")
    fun loadDataFromServer() :Call<List<MarketItem>>
}