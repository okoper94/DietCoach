package com.kys2024.dietcoach.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.data.UserAccount
import com.kys2024.dietcoach.databinding.ActivityLoginMemberShipBinding
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part

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
        val nickname = binding.inputLayoutNickname.editText!!.text.toString()



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

        G.userAccount = UserAccount(email, password, nickname)
        serverToLoginUpload()

    }

    fun serverToLoginUpload() {

        // 회원가입한 데이터들 받아와서 서버에 보내기

        // 레트로핏 작업 5단계..
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        // 먼저 String 데이터들은 Map collection 으로 묶어서 전송: @PartMap
        val data: HashMap<String, String> = hashMapOf()
        data["userid"] = G.userAccount!!.uid.toString()
        data["nickname"] = G.userAccount!!.nickname.toString()
        data["password"] = G.userAccount!!.password.toString()
        data["date"] = System.currentTimeMillis().toString()


        //네트워크 작업 시작
        retrofitService.postdataToServer(data ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val s = response.body()
                if (s!!.contains("성공")){
                Toast.makeText(this@LoginMemberShipActivity, "\"축하합니다 \n회원가입이 완료되었습니다\"$s", Toast.LENGTH_SHORT).show()
                finish()}// 업로드가 완료되면 액티비티 종료
                else{
                    Toast.makeText(this@LoginMemberShipActivity, "이미 가입된 이메일 입니다", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@LoginMemberShipActivity, "실패:${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }//서버업로드

}