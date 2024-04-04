package com.kys2024.dietcoach.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityLoginBinding
import com.kys2024.dietcoach.databinding.ActivityLoginEmailBinding

class LoginEmailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginEmailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_login_email)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }




    }
}