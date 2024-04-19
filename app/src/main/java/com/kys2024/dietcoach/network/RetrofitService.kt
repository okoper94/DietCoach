package com.psg2024.ex68retrofitmarketapp

import com.kys2024.dietcoach.data.BoardData
import com.kys2024.dietcoach.data.FoodBoardData
import com.kys2024.dietcoach.data.LoadUserData
import com.kys2024.dietcoach.data.MypageData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface RetrofitService {

    // 1. POST방식으로 데이터를 보내기..

    @POST("/DietCoach/usertable.php")
    fun postdataToServer(@Body data: HashMap<String, String>): Call<String>

    @POST("/DietCoach/fooddata.php")
    fun postdataTofooddata(@Body data: HashMap<String, String>): Call<String>
    @POST("/DietCoach/emailLogin.php")
    fun postLogindataToServer(@Body data: HashMap<String, String>): Call<String>

    @Multipart
    @POST("/DietCoach/imgupload.php")
    fun uploadImage(@PartMap dataPart:Map<String, String>,
                    @Part filePart: MultipartBody.Part?): Call<String>
    @Multipart
    @POST("/DietCoach/boardupload.php")
    fun updateprofile(@PartMap dataPart:Map<String, String>,
                    @Part filePart: MultipartBody.Part?): Call<String>
    @Multipart
    @POST("/DietCoach/board.php")
    fun uploadboard(@PartMap dataPart:Map<String, String>,
                    @Part filePart: MultipartBody.Part?): Call<String>

    @POST("/DietCoach/loadDB.php")
    fun loadDataFromServer(@Body data: HashMap<String, String>) :Call<LoadUserData>

    @POST("/DietCoach/loadfoodDB.php")
    fun loadFoodDataFromServer(@Body data: HashMap<String, String>) :Call<List<FoodBoardData>>

    @POST("/DietCoach/boardDBload.php")
    fun loadDataFromServerboard() :Call<List<BoardData>>



}