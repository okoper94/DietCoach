package com.kys2024.dietcoach.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

import com.kys2024.dietcoach.G

import com.google.android.gms.common.api.Response

import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.BoardItem
import com.kys2024.dietcoach.data.UserAccount
import com.kys2024.dietcoach.databinding.ActivityWriteBoardBinding
import com.kys2024.dietcoach.fragments.DietMyFragment

import com.psg2024.ex68retrofitmarketapp.RetrofitService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class WriteBoardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWriteBoardBinding.inflate( layoutInflater ) }
    private var imageUri : Uri? = null
    var imgPath: String?= null
    var userId :String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        userId = G.userAccount?.uid
        Log.d("id보기", "id: ${userId}")

         binding.writeSelectImageLayout.setOnClickListener {
            openImageSelector()
        }

        binding.writeOkBtn.setOnClickListener {
            if ( imageUri == null ) {
                showImageNotSelectedDialog()
            } else {
                saveData()
            }
        }

        binding.writeIvBack.setOnClickListener {
            showCancelConfirmationDialog()
        }
    }

    private fun saveData() {
        if ( imageUri != null ) {
            imgPath = getRealPathFromUri(imageUri!!)
            clickUpload()

        } else {
            Toast.makeText( this, "이미지가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun saveData() {
//        val message = binding.writeMsgEditText.text.toString()
//        val boardItem = BoardItem(imageUri!!, message)
//
//        // Retrofit 초기화
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://example.com/") // 서버의 기본 URL을 입력
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//
//        // 이미지 파일 생성하고 전송하고
//        val file = File(imageUri!!.path!!)
//        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
//        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//        // 메시지 전송하고
//        val messagePart = RequestBody.create(MediaType.parse("text/plain"), message)
//
//        // 서버에 이미지와 메시지 전송
//        apiService.uploadImage(imagePart, messagePart).enqueue(object : Callback<ResponseBody> {
//            fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@WriteBoardActivity, "데이터 전송 성공", Toast.LENGTH_SHORT).show()
//                    finish() // 액티비티 종료
//                } else {
//                    Toast.makeText(this@WriteBoardActivity, "데이터 전송 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@WriteBoardActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


    private fun openImageSelector() {
        val intent = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            Intent( MediaStore.ACTION_PICK_IMAGES )
        } else {
            Intent( Intent.ACTION_OPEN_DOCUMENT ).setType( "image/*" )
        }
        resultLauncher.launch( intent )
    }

    private fun showImageNotSelectedDialog() {
        AlertDialog.Builder(this)
            .setMessage("이미지를 선택하세요")
            .setPositiveButton("확인") { p0, p1 -> p0.dismiss() }
            .show()
    }

    private fun showCancelConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("작성을 취소하고 이전 페이지로 돌아가시겠습니까?")
            .setPositiveButton("확인") { p0, p1 -> finish() }
            .setNegativeButton("취소", null)
            .show()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode== RESULT_OK) {
            imageUri = result.data?.data
            if (imageUri != null) {

                Glide.with(this).load(imageUri).apply(RequestOptions()).into(binding.writeIv)
                binding.writeSelectImageLayout.visibility = View.GONE
            } else {
                Toast.makeText(this, "이미지를 선택하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getRealPathFromUri(uri: Uri): String? {

        // Uri[미디어저장소의 DB주소]로 부터 파일의 이름을 얻어오기 - DB SELECT 쿼리작업을 해주는 기능을 가진 객체를 이용
        val cursorLoader : CursorLoader = CursorLoader(this, uri, null, null, null, null)
        val cursor: Cursor? =cursorLoader.loadInBackground()
        val fileName:String? = cursor?.run {
            moveToFirst()
            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        }

        // 복사본이 저장될 파일의 경로와 파일명. 확장자
        val file: File = File(this.externalCacheDir,fileName)

        //이제 진짜 복사작업 수행
        val inputStream: InputStream =this.contentResolver.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)

        //파일복사
        while (true){
            val buf: ByteArray = ByteArray(1024) // 빈 바이트 배열[길이1kb]
            val len:Int =inputStream.read(buf) //스트림을 통해 읽어들인 바이트들을 buf배열에 넣어줌-- 읽어들인 바이트 수를 리턴해줌
            if(len <=0) break
            outputStream.write(buf, 0, len)
        }
        //반복문이 끝났으면 복사가 완료된 것임.
        inputStream.close()
        outputStream.close()
        return file.absolutePath
    }

    private fun  clickUpload(){

        imgPath ?: return

        //1. retrofit 객체 생성
        val builder: Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl("http://toachwithfire3.dothome.co.kr")
        builder.addConverterFactory(ScalarsConverterFactory.create())
        val retrofit: Retrofit = builder.build()

        //2. 레트로핏 서비스 명세서 인터페이스 설계하기
        val retrofitService = retrofit.create(RetrofitService::class.java)


        val dataPart: MutableMap<String, String> = mutableMapOf()
        dataPart["userid"] = userId.toString()
        dataPart["msg"] = binding.writeMsgEditText.text.toString()//글쓴 텍스트들을 msg로


        //3. 전송할 파일을 특별한 박스 (MultipartBody.Part객체)로 포장하기
        val file: File = File(imgPath)
        val requestBody: RequestBody =
            file.asRequestBody("image/*".toMediaTypeOrNull()) //일종의 진공팩..[파일을 포장]
        val filepart: MultipartBody.Part =
            MultipartBody.Part.createFormData("img1",file.name, requestBody)//식별자, 파일명, 요청Body [택배 스티로폼 상자]


        retrofitService.uploadboard(dataPart, filepart).enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                Toast.makeText(this@WriteBoardActivity, "업로드 성공", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onFailure(p0: Call<String>, p1: Throwable) {
                Toast.makeText(this@WriteBoardActivity, "${p1.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }
}