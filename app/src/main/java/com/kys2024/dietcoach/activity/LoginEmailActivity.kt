package com.kys2024.dietcoach.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.Logindata
import com.kys2024.dietcoach.databinding.ActivityLoginBinding
import com.kys2024.dietcoach.databinding.ActivityLoginEmailBinding
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginEmailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginEmailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnLogin.setOnClickListener { emailLogin() }





    }

    private fun emailLogin(){
        val email = binding.inputLayoutEmail.editText!!.text.toString()
        val password = binding.inputLayoutPassword.editText!!.text.toString()

        if (email.isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        return}
        else if(password.isEmpty()){Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        return}
        loginVerification(email,password)



    }

    private fun loginVerification(email:String, password:String){
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        val data: HashMap<String, String> = hashMapOf()
        data["userid"] = email
        data["password"] = password

        retrofitService.postLogindataToServer(data).enqueue(object :Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
               val s = p1.body()
                if(s!=null){

                if (s.contains("성공")){
                    Toast.makeText(this@LoginEmailActivity, "$s", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginEmailActivity, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    finish()

                    }

                Toast.makeText(this@LoginEmailActivity, "$s", Toast.LENGTH_SHORT).show()
                return}
                Toast.makeText(this@LoginEmailActivity, "$s", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(p0: Call<String>, p1: Throwable) {
                Toast.makeText(this@LoginEmailActivity, "${p1.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}