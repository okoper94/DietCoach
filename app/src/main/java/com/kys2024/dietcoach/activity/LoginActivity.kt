package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.tvDool.setOnClickListener { clickDool() }
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

    }

    private fun clickNaverLogin(){

    }


}