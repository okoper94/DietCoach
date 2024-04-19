package com.kys2024.dietcoach.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.activity.ResultActivity
import com.kys2024.dietcoach.data.BoardData
import com.kys2024.dietcoach.data.FoodBoardData
import com.kys2024.dietcoach.data.FoodTime
import com.kys2024.dietcoach.data.LoadUserData
import com.kys2024.dietcoach.databinding.FragmentDietHomeBinding
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DietHomeFragment : Fragment() {


    private val binding by lazy { FragmentDietHomeBinding.inflate(layoutInflater) }
    private var imageUriToResult :Uri? =null
    private var todaytime:String? =G.todaydate?.todaydate
    var loadBoardData: List<FoodBoardData>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.goac.setOnClickListener { startActivity(Intent(requireActivity(), ResultActivity::class.java)) }

        // 넣고 싶은 데이터 설정
        val dataList: List<PieEntry> = listOf(
            PieEntry(45f, "탄수화물"),
            PieEntry(40f, "단백질"),
            PieEntry(15f, "지방")
        )

        if (binding.bkcal.text.toString() != "0 kcal") {
            binding.breakPlus.isInvisible = true
            binding.breakRefresh.isVisible = true
        }

        if (binding.lunchKcal.text.toString() != "0 kcal") {
            binding.lunchPlus.isInvisible = true
            binding.lunchRefresh.isVisible = true
        }

        if (binding.dinnerKcal.text.toString() != "0 kcal") {
            binding.dinnerPlus.isInvisible = true
            binding.dinnerRefresh.isVisible = true
        }


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

            binding.relativeLayoutMorning.setOnClickListener { clickMorning()
            }
            binding.relativeLayoutLunch.setOnClickListener { clickLunch() }
            binding.relativeLayoutDinner.setOnClickListener { clickDinner() }

        }
    }

    override fun onResume() {
        super.onResume()
        loadfoodkcal()
    }

    private fun loadfoodkcal() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)


        val data: HashMap<String, String> = hashMapOf()
        data["userid"] = G.userAccount?.uid.toString()
        data["time"] = G.foodtime?.time.toString()
        data["date"] = todaytime.toString()
        retrofitService.loadFoodDataFromServer(data).enqueue(object : Callback<List<FoodBoardData>>{
            override fun onResponse(
                p0: Call<List<FoodBoardData>>,
                p1: Response<List<FoodBoardData>>
            ) {
                var totalkcal:Float =0.0F
               val response = p1.body()
                response?.forEach { totalkcal += it.kcal.toFloat() }
                binding.bkcal.text = "${totalkcal.toString()}Kcal"
            }

            override fun onFailure(p0: Call<List<FoodBoardData>>, p1: Throwable) {
                Toast.makeText(requireContext(), "실패${p1.message}", Toast.LENGTH_SHORT).show()
            }
        } )
    }

    private fun clickMorning() { //아침메뉴 선택시 다이얼로그로 카메라 촬영 앨범 선택 사항
        val time ="breakfast"
        G.foodtime = FoodTime(time)
        val items = arrayOf<CharSequence>("카메라로 촬영", "앨범에서 선택")
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

    private fun takePicture() {  //카메라앱
        setPhotoUri ()
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        resultLauncher.launch(intent)



    }

    private fun chooseFromGallery() {  //사진앨범
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(
                Intent.ACTION_OPEN_DOCUMENT
            ).setType("image/*")
        resultLauncher.launch(intent)


    }

    val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUriToResult = result.data?.data

                Log.d("사진주소", "${result.data?.data}, $imageUriToResult")
                startActivity(Intent(requireActivity(), ResultActivity::class.java).putExtra("uri", imageUriToResult.toString()))
            }

        }
    private fun setPhotoUri () {
        //내장 저장공간의 외부 저장소 중에서 공용영역에 저장 - 앱을 삭제해도 파일은 남아있음
        // 공용영역의 경로부터..
        val path: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        //경로는 정해졌으니. 저장될 파일의 이름과 확장자를 정하기 - 중복되지 않도록 날짜를 이용하여 명명
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val fileName: String = "IMG_${sdf.format(Date())}.jpg"//"IMG_20240219143924.jpg"

        //경로와 파일명을 결합
        val file: File = File(path, fileName)

        //여기까지 경로가 잘 되었는지 확인
        AlertDialog.Builder(requireContext()).setMessage(file.toString()).create().show()
    }

    private fun clickLunch() {
        val time ="lunch"
        G.foodtime = FoodTime(time)
        val items = arrayOf<CharSequence>("카메라로 촬영", "앨범에서 선택")
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

    private fun clickDinner() {
        val time ="dinner"
        G.foodtime = FoodTime(time)
        val items = arrayOf<CharSequence>("카메라로 촬영", "앨범에서 선택")
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


