package com.kys2024.dietcoach.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityMyPageBinding

class MyPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }

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
        binding.ivPencilNickname.setOnClickListener { clickNickname() }
        binding.tvSave.setOnClickListener { clickSave() }

    }

    private fun clickSave(){

    }

    private fun clickNickname(){

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


        }

    }

}