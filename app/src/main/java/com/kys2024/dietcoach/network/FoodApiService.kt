package com.kys2024.dietcoach.network

import com.kys2024.dietcoach.data.FoodResponse
import retrofit2.Call
import retrofit2.http.GET

interface FoodApiService {

    @GET( "15050912/v1/uddi:0a633058-9843-40fe-93d0-b568f23b715e_201909261047?page=1&perPage=1000&serviceKey=aRSbUmNWgVrkijlIVfNoAIpBxmI%2BqJp9eZEaFJGNHrS5UzMNgV27JiM%2FKW8nSP58oxcpTKHIdjawY1GScz%2BegQ%3D%3D" )
    fun getFoods() : Call<FoodResponse>
}