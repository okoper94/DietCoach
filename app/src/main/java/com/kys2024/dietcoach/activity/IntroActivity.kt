package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.common.util.Utility
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var keyHash = Utility.getKeyHash(this)
        Log.i("keyhash", "$keyHash")

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java ))
            finish()
        },1500)

    }
}