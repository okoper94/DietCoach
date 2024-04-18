package com.kys2024.dietcoach.activity

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.MypageData
import com.kys2024.dietcoach.databinding.ActivityMyPageBinding
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.io.File
import java.io.FileOutputStream

class MyPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }
    var imgPath: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_my_page)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContentView(binding.root)
        binding.ivEditing.setOnClickListener { clickPhoto() }
        binding.tvSave.setOnClickListener { clickSave() }
        binding.toolbar.setNavigationOnClickListener { finish() }

    }

    private fun clickSave(){


    }


    private fun clickPhoto(){ //버튼클릭시 내 앨범에서 사진선택해서 프로필 변경
        val intent = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(
            Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)

    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val uri: Uri?=it.data?.data
        if (uri!=null){
            Glide.with(this).load(uri).apply(RequestOptions().transform(CircleCrop())).into(binding.ivProfile)

            imgPath = getRealPathFromUri (uri)
            clickUpload()
        }
    }



    private fun getRealPathFromUri(uri: Uri):String? {
        val cursorLoader = CursorLoader(this,uri, null,null,null,null)
        val cursor:Cursor? = cursorLoader.loadInBackground()
        val fileName:String? = cursor?.run {
            moveToFirst()
            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        }
        val file:File = File(this.externalCacheDir,fileName)
        val inputStream = this.contentResolver.openInputStream(uri) ?:return null
        val outputStream = FileOutputStream(file)

        while (true) {
            val buf:ByteArray = ByteArray(1024)
            val len:Int = inputStream.read(buf)
            if(len <= 0) break
            outputStream.write(buf,0,len)
        }
        inputStream.close()
        outputStream.close()
        return file.absolutePath
    }


     private fun clickUpload(){
         imgPath?: return
        val builder:Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl("http://toachwithfire3.dothome.co.kr")
        builder.addConverterFactory(ScalarsConverterFactory.create())
        val retrofit: Retrofit = builder.build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        val dataPart: MutableMap<String,String> = mutableMapOf()
        dataPart["nickname"] = binding.etNickname.editableText.toString()
         dataPart["userid"] = G.userAccount?.uid.toString()



         val file: File = File(imgPath)
         val requestBody:RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
         val filepart:MultipartBody.Part = MultipartBody.Part.createFormData("img1",file.name,requestBody)

         retrofitService.updateprofile(dataPart,filepart).enqueue(object : Callback<String>{
             override fun onResponse(p0: Call<String>, p1: Response<String>) {
                 val s = p1.body()
                 Toast.makeText(this@MyPageActivity, "$s 성공", Toast.LENGTH_SHORT).show()
             }

             override fun onFailure(p0: Call<String>, p1: Throwable) {
                 Toast.makeText(this@MyPageActivity, "실패: ${p1.message}", Toast.LENGTH_SHORT).show()
             }

         })
     }
}

