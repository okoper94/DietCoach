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
import com.kys2024.dietcoach.data.UserAccount
import com.kys2024.dietcoach.databinding.ActivityLoginBinding
import com.kys2024.dietcoach.databinding.ActivityLoginMemberShipBinding
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginMemberShipActivity : AppCompatActivity() {

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

        G.userAccount = UserAccount(email, password)
        serverToLoginUpload()

    }

    fun serverToLoginUpload() {

        // 회원가입한 데이터들 받아와서 서버에 보내기

        // 레트로핏 작업 5단계..
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        // 먼저 String 데이터들은 Map collection 으로 묶어서 전송: @PartMap
        val dataPart: MutableMap<String, String> = mutableMapOf()
        dataPart["userid"] = G.userAccount!!.uid.toString()
        dataPart["nickname"] = G.userAccount!!.nickname.toString()
        dataPart["password"] = G.userAccount!!.password.toString()
        dataPart["date"] = System.currentTimeMillis().toString()


        //네트워크 작업 시작
        retrofitService.postdataToServer(dataPart, null).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val s = response.body()
                Toast.makeText(this@LoginMemberShipActivity, "\"축하합니다 \n회원가입이 완료되었습니다\"$s", Toast.LENGTH_SHORT).show()
                finish()// 업로드가 완료되면 액티비티 종료
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@LoginMemberShipActivity, "실패:${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }//서버업로드

}