package com.kys2024.dietcoach.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kys2024.dietcoach.activity.MyInformationActivity
import com.kys2024.dietcoach.databinding.FragmentDietMyBinding
import kotlin.math.roundToInt

class DietMyFragment:Fragment() {

    private val binding by lazy { FragmentDietMyBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvBmi.setOnClickListener { clickBmi() }
        binding.ivEditing.setOnClickListener { clickPhoto() }







    }


    private fun clickPhoto(){ //버튼클릭시 내 앨범에서 사진선택해서 프로필 변경
        val intent = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val uri:Uri?=it.data?.data
        if (uri!=null){
            Glide.with(this).load(uri).apply(RequestOptions().transform(CircleCrop())).into(binding.ivProfile)


        }
    }





    private fun clickBmi(){  //버튼 클릭시 MyInformationActivity 로 이동
        startActivity(Intent(requireContext(),MyInformationActivity::class.java))
    }



}