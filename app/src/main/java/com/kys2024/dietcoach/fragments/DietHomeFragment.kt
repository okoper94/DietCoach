package com.kys2024.dietcoach.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.FragmentDietHomeBinding

class DietHomeFragment:Fragment() {


    private val binding by lazy { FragmentDietHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 넣고 싶은 데이터 설정
        val dataList: List<PieEntry> = listOf(
            PieEntry(45f, "탄수화물"),
            PieEntry(40f, "단백질"),
            PieEntry(15f, "지방")
        )

        val dataSet = PieDataSet(dataList, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.pastel_rainbow1),
            ContextCompat.getColor(requireContext(), R.color.pastel_rainbow2),
            ContextCompat.getColor(requireContext(), R.color.pastel_rainbow3)
        ) // 각 섹션의 색상 설정

        // pieChart 안에 들어갈 텍스트 크기
        dataSet.valueTextSize = 16f

        // pieChart 안에 들어간 value 값 표기 지우기
        dataSet.setDrawValues(true)

        // 데이터 설정 값 삽입
        val piedata = PieData(dataSet)

        binding.pieChart.apply {
            data = piedata
            description.isEnabled = false // 차트 설명 비활성화
            legend.isEnabled = false // 하단 설명 비활성화
            isRotationEnabled = true // 차트 회전 활성화
            setEntryLabelColor(Color.BLACK) // label 색상
            animateY(1400, Easing.EaseInOutQuad) // 1.4초 동안 애니메이션 설정

            binding.relativeLayoutMorning.setOnClickListener { clickMorning() }
            binding.relativeLayoutLunch.setOnClickListener { clickLunch() }
            binding.relativeLayoutDinner.setOnClickListener { clickDinner() }
        }
    }
    private fun clickMorning(){ //아침메뉴 선택시 다이얼로그로 카메라 촬영 앨범 선택 사항
        val items = arrayOf<CharSequence> ("카메라로 촬영","앨범에서 선택")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("사진선택")
        builder.setItems(items) { dialog, which ->
            when (which) {
                0 -> takePicture()
                1 -> chooseFromGallery()
            }

        }

        builder.show()
    }
    private fun takePicture(){  //카메라앱
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }
    private fun chooseFromGallery(){  //사진앨범
        val intent = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)

    }
    val resultLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode==Activity.RESULT_OK){
            val intentData = result.data
            intentData?.let { data->
                val imageUri =data.data
                imageUri?.let { uri ->
                    Glide.with(requireContext()).load(uri)
                }
            }
        }
    }

    private fun clickLunch(){
        val items = arrayOf<CharSequence> ("카메라로 촬영","앨범에서 선택")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("사진선택")
        builder.setItems(items) { dialog, which ->
            when (which) {
                0 -> takePicture()
                1 -> chooseFromGallery()
            }

        }

        builder.show()


    }

    private fun clickDinner(){
        val items = arrayOf<CharSequence> ("카메라로 촬영","앨범에서 선택")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("사진선택")
        builder.setItems(items) { dialog, which ->
            when (which) {
                0 -> takePicture()
                1 -> chooseFromGallery()
            }

        }

        builder.show()


    }

    }