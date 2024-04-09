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

//   NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback{
//            override fun onError(errorCode: Int, message: String) {
//                Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(httpStatus: Int, message: String) {
//                Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onSuccess() {
//                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
//
//                //사용자 정보를 받아오기 -- REST API로 받아야함
//                //로그인에 성공하면 REST API로 요청할 수 있는 토큰을 발급받음
//                val accessToken:String? = NaverIdLoginSDK.getAccessToken()
//
//                //Retrofit 작업을 통해 사용자 정보 가져오기
//                val retrofit = RetrofitHelper.getRetrofitInstance("https://openapi.naver.com")
//                val retrofitApiService=retrofit.create(RetrofitApiService::class.java)
//                val call= retrofitApiService.getNidUserInfo("Bearer ${accessToken}")
//                call.enqueue(object : Callback<String> {
//                    override  fun onResponse(call: Call<String>, response: Response<String>){
//                        val s = response.body()
//                        AlertDialog.Builder(this@LoginActivity).setMessage(s).create().show()
//
//                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                        finish()
//                    }
//
//                    override  fun onFailure(call: Call<String>, t: Throwable){
//                        Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//