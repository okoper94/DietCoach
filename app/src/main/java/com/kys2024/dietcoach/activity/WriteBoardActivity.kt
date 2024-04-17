package com.kys2024.dietcoach.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.Response
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.BoardItem
import com.kys2024.dietcoach.databinding.ActivityWriteBoardBinding
import com.kys2024.dietcoach.fragments.DietMyFragment
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

class WriteBoardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWriteBoardBinding.inflate( layoutInflater ) }
    private lateinit var selectImageLayout : LinearLayout
    private lateinit var imageView : ImageView
    private var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        selectImageLayout = binding.writeSelectImageLayout
        imageView = binding.writeIv

        selectImageLayout.setOnClickListener {
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
            val message = binding.writeMsgEditText.text.toString()
            val boardItem = BoardItem( imageUri!!, message )
            // 여기에서 서버에 저장하면 됨!! 서버에 boardItem 보내기!!
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
        val imageUri: Uri? = result.data?.data
        if ( imageUri != null ) {
            selectImageLayout.visibility = View.GONE
            imageView.setImageURI( imageUri )
            this.imageUri = imageUri
        } else {
            Toast.makeText(this, "이미지를 선택하지 않았습니다", Toast.LENGTH_SHORT).show()
        }
    }
}