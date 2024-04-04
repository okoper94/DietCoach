package com.kys2024.dietcoach.data

import com.google.gson.annotations.SerializedName

data class FoodData(
    @SerializedName("음식명") val foodName : String,
    @SerializedName("1인분칼로리(kcal)") val calories : String,
    @SerializedName("탄수화물(g)") val carbsGram : String,
    @SerializedName("단백질(g)") val proteinGram : String,
    @SerializedName("지방(g)") val fatGram : String,
    @SerializedName("콜레스트롤(g)") val cholesterolGram : String,
    @SerializedName("식이섬유(g)") val fiberGram : String,
    @SerializedName("나트륨(g)") val sodiumGram : String,
    @SerializedName("등록일") val registrationDate : String
)

data class FoodResponse(
    val page : Int,
    val perPage : Int,
    val totalCount : Int,
    val currentCount : Int,
    val matchCount : Int,
    val data : List<FoodData>
)