package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.data.UserAccount
import com.kys2024.dietcoach.databinding.ActivityLoginBinding
import com.kys2024.dietcoach.network.RetrofitApiService
import com.kys2024.dietcoach.network.RetrofitHelper
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

<<<<<<<<< Temporary merge branch 1
        NaverIdLoginSDK.initialize(this, "xC8gXWUBO0R6KR8vexNc", "rTWsN8I52B", "다이어트코치")


        binding.tvDool.setOnClickListener { clickDool()}

=========
        binding.tvDool.setOnClickListener { clickDool() }
>>>>>>>>> Temporary merge branch 2
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

        //로그인 옵션객체 생성 - Builder - 이메일 요청..
        val signInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        //구글 로그인을 하는 화면 액티비티를 실행하는 Intent 객체로 로그인 구현
        val intent:Intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
        resultLauncher.launch(intent)
    }
    //구글 로그인에 필요한 멤버변수
    // 구글 로그인화면 결과를 받아주는 대행사 등록
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //로그인 결과를 가져온 인텐트 소환
        val intent:Intent? = it.data
        //인텐트로 부터 구글 계정 정보를 가져오는 작업자 객체를 소환
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

        //작업자로부터 계정 받기
        val account: GoogleSignInAccount = task.result
        val id:String = account.id.toString()
        val email:String= account.email ?: ""

        Toast.makeText(this, "$id\n$email", Toast.LENGTH_SHORT).show()
        G.userAccount= UserAccount(id, email)

        //main 화면으로 이동
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
    //구글 로그인에 필요한 멤버변수

    private fun clickKakaoLogin(){

        // 두개의 로그인 요청 콜백함수
        val callback:(OAuthToken?, Throwable?)->Unit = { token, error ->
            if(error != null) {
                Toast.makeText(this, "카카오로그인 실패", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "카카오로그인 성공", Toast.LENGTH_SHORT).show()

                //로그인이 성공하면 사용자 정보 요청
                UserApiClient.instance.me  { user, e ->
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