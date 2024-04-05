package com.kys2024.dietcoach.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitApiService  {
    //네아로 회원정보 프로필 api 요청
    @GET("/v1/nid/me")
    fun getNidUserInfo(@Header("Authorization") authorization:String): Call<String>
}