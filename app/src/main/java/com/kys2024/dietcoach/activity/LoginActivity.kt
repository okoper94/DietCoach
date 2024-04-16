package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var userId : String? = ""
    var userNick : String? = ""

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        NaverIdLoginSDK.initialize(this, "xC8gXWUBO0R6KR8vexNc", "rTWsN8I52B", "다이어트코치")



        binding.tvDool.setOnClickListener { clickDool()}


        binding.layoutEmailLogin.setOnClickListener{ clickEmailLogin()}
        binding.layoutGoogleLogin.setOnClickListener{ clickGoogleLogin()}
        binding.layoutKakaoLogin.setOnClickListener{ clickKakaoLogin()}
        binding.buttonOAuthLoginImg.setOnClickListener{ clickNaverLogin() }
        binding.tvLogin.setOnClickListener { clickSignup() }


    }
    private fun clickSignup(){
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
        if( it.resultCode == RESULT_OK ){
            //로그인 결과를 가져온 인텐트 소환
            val intent:Intent? = it.data
            //인텐트로 부터 구글 계정 정보를 가져오는 작업자 객체를 소환
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            //작업자로부터 계정 받기
            val account: GoogleSignInAccount = task.result
            val uid:String = account.id.toString()
            val nickname:String= account.displayName ?: ""

            Toast.makeText(this, "$uid\n$nickname", Toast.LENGTH_SHORT).show()
            userId= uid
            userNick = nickname
            G.userAccount= UserAccount(uid = userId!!, nickname = userNick!!)
            serverToLoginUpload()

            //main 화면으로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "로그인 취소", Toast.LENGTH_SHORT).show()
        }
        

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
                        val uid:String = user.id.toString()
                        val nickname:String = user.kakaoAccount?.profile?.nickname ?: ""
                        Toast.makeText(this, "$uid\n$nickname", Toast.LENGTH_SHORT).show()
                        userId= uid
                        userNick = nickname
                        G.userAccount= UserAccount(uid = userId!!, nickname = userNick!!)
                        serverToLoginUpload()

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

    private fun clickNaverLogin() {
        //네이버 토큰을 받을 변수를 설정합니다.
        var naverToken: String? = ""


        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                userId = response.profile?.id
                userNick = response.profile?.nickname
                Log.d("naverlogin", "id: ${userId} \ntoken: ${naverToken}")


                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
                G.userAccount = UserAccount(uid = userId!!, nickname = userNick!!)
                Log.d("naverlogin2", "id: ${G.userAccount?.uid} \ntoken: ${naverToken}")

                serverToLoginUpload()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))


            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    this@LoginActivity, "errorCode: ${errorCode}\n" +
                            "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }


        }
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)

            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    this@LoginActivity, "errorCode: ${errorCode}\n" +
                            "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }//네이버 로그인

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
        retrofitService.postdataToServer(data).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val s = response.body()
                Toast.makeText(this@LoginActivity, "성공$s", Toast.LENGTH_SHORT).show()
                finish()// 업로드가 완료되면 액티비티 종료
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "실패:${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }//서버업로드














}