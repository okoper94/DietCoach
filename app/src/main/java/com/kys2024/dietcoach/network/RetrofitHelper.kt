package com.kys2024.dietcoach.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper {
    companion object{
        fun getRetrofitInstance(naver:String): Retrofit {
            val builder = Retrofit.Builder()
            builder.baseUrl(naver)
            builder.addConverterFactory(ScalarsConverterFactory.create())
            builder.addConverterFactory(GsonConverterFactory.create())
            return builder.build()

        }
    }
}