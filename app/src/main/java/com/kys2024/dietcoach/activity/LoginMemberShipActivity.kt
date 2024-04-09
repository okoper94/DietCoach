package com.kys2024.dietcoach.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityLoginBinding
import com.kys2024.dietcoach.databinding.ActivityLoginMemberShipBinding

class LoginMemberShipActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private val binding by lazy { ActivityLoginMemberShipBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnLogin.setOnClickListener { signUpToAuth() }


    }

    private fun signUpToAuth() {
        val email = binding.inputLayoutEmail.editText!!.text.toString()
        val password = binding.inputLayoutPassword.editText!!.text.toString()
        val passwordConform = binding.inputLayoutPasswordCheck.editText!!.text.toString()



        if (password.isEmpty()) {
            AlertDialog.Builder(this)
                .setMessage("password를 입력해주세요")
                .setPositiveButton("확인", { _, _ -> })
                .create().show()
            return
        }

        if (password != passwordConform) {
            AlertDialog.Builder(this).setMessage("패스워드가 다릅니다. 다시 확인하여 입력해주세요.").create()
                .show()
            binding.inputLayoutPasswordCheck.editText!!.selectAll()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this)
                        .setMessage("축하합니다 \n회원가입이 완료되었습니다")
                        .setPositiveButton("확인", { _, _ -> finish() })
                        .create().show()
                } else {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }
            }





//             다 성공했으면  토큰발급, 서버연결 후  회원가입 정보,(email, Pw, 토큰값) 저장
//             저장 후 sqlite에 내 정보들 저장해놓고 로그인 화면으로 이동

    }
}