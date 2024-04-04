package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.UserAccount
import com.kys2024.dietcoach.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)


        binding.tvDool.setOnClickListener { clickDool()}

        binding.layoutEmailLogin.setOnClickListener{ clickEmailLogin()}
        binding.layoutGoogleLogin.setOnClickListener{ clickGoogleLogin()}
        binding.layoutKakaoLogin.setOnClickListener{ clickKakaoLogin()}
        binding.layoutNaverLogin.setOnClickListener{ clickNaverLogin()}
        binding.tvLogin.setOnClickListener { clickLogin() }


    }
    private fun clickLogin(){
        startActivity(Intent(this,LoginMemberShipActivity::class.java))


    }
    private fun clickDool(){
        startActivity(Intent(this,MainActivity::class.java))

    }

    private fun clickEmailLogin(){
        startActivity(Intent(this,LoginEmailActivity::class.java))
    }
    private fun clickGoogleLogin(){

    }

    private fun clickKakaoLogin(){

        // 두개의 로그인 요청 콜백함수
        val callback:(OAuthToken?, Throwable?)->Unit = { token, error ->
            if(error != null) {
                Toast.makeText(this, "카카오로그인 실패", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "카카오로그인 성공", Toast.LENGTH_SHORT).show()

                //로그인이 성공하면 사용자 정보 요청
                UserApiClient.instance.me  { user, error ->
                    if(user!=null){
                        val id:String = user.id.toString()
                        val nickname:String = user.kakaoAccount?.profile?.nickname ?: ""
                        Toast.makeText(this, "$id\n$nickname", Toast.LENGTH_SHORT).show()
                        G.userAccount = UserAccount(id, nickname)

                        //로그인 되었으니..
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }

        }
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this,callback = callback)
        }

    }

    private fun clickNaverLogin(){

    }


}