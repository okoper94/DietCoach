package com.kys2024.dietcoach.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.activity.ResultActivity
import com.kys2024.dietcoach.databinding.FragmentDietHomeBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DietHomeFragment : Fragment() {

    private var _binding: FragmentDietHomeBinding? = null
    private val binding get() = _binding!!

    private var currentPhotoPath: String? = null
    private var currentMealType: String = "breakfast" // 현재 선택된 식사 유형 저장

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDietHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPieChart()
        loadNutritionData()

        // 각 식사 버튼에 클릭 리스너 설정
        binding.relativeLayoutMorning.setOnClickListener { clickMeal("breakfast") }
        binding.relativeLayoutLunch.setOnClickListener { clickMeal("lunch") }
        binding.relativeLayoutDinner.setOnClickListener { clickMeal("dinner") }
    }

    override fun onResume() {
        super.onResume()
        loadNutritionData() // 화면이 다시 보일 때마다 영양 정보 다시 읽어오기
    }

    // 파이 차트 초기 설정
    private fun setupPieChart() {
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
        )

        dataSet.valueTextSize = 16f
        dataSet.setDrawValues(true)

        val piedata = PieData(dataSet)

        binding.pieChart.apply {
            data = piedata
            description.isEnabled = false
            legend.isEnabled = false
            isRotationEnabled = true
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
        }
    }

    // 영양 정보 읽어오고 UI 업데이트 하고
    private fun loadNutritionData() {
        val sharedPref = requireActivity().getSharedPreferences("UserNutrition", Context.MODE_PRIVATE)
        val recommendedCarbs = sharedPref.getInt("RECOMMENDED_CARBS", 0)
        val recommendedProtein = sharedPref.getInt("RECOMMENDED_PROTEIN", 0)
        val recommendedFat = sharedPref.getInt("RECOMMENDED_FAT", 0)

        binding.tvHomeCarbs.text = "${recommendedCarbs}g"
        binding.tvHomeProtein.text = "${recommendedProtein}g"
        binding.tvHomeFat.text = "${recommendedFat}g"

        // 실제 섭취량 읽어오고
        val nutritionPref = requireActivity().getSharedPreferences("NutritionData", Context.MODE_PRIVATE)
        val totalCarbs = nutritionPref.getFloat("breakfast_carbs", 0f) +
                nutritionPref.getFloat("lunch_carbs", 0f) +
                nutritionPref.getFloat("dinner_carbs", 0f)
        val totalProtein = nutritionPref.getFloat("breakfast_protein", 0f) +
                nutritionPref.getFloat("lunch_protein", 0f) +
                nutritionPref.getFloat("dinner_protein", 0f)
        val totalFat = nutritionPref.getFloat("breakfast_fat", 0f) +
                nutritionPref.getFloat("lunch_fat", 0f) +
                nutritionPref.getFloat("dinner_fat", 0f)

        // 파이 차트 업데이트
        updatePieChart(totalCarbs, totalProtein, totalFat)

        // 프로그레스바 업데이트
        updateProgressBars(totalCarbs, totalProtein, totalFat, recommendedCarbs, recommendedProtein, recommendedFat)

        // 칼로리 정보 업데이트
        updateCaloriesInfo(nutritionPref)
    }

    private fun updatePieChart(carbs: Float, protein: Float, fat: Float) {
        val total = carbs + protein + fat
        if (total > 0) {
            val dataList: List<PieEntry> = listOf(
                PieEntry(carbs, "탄수화물"),
                PieEntry(protein, "단백질"),
                PieEntry(fat, "지방")
            )

            val dataSet = PieDataSet(dataList, "")
            dataSet.colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.pastel_rainbow1),
                ContextCompat.getColor(requireContext(), R.color.pastel_rainbow2),
                ContextCompat.getColor(requireContext(), R.color.pastel_rainbow3)
            )
            dataSet.valueTextSize = 16f
            dataSet.setDrawValues(true)

            val pieData = PieData(dataSet)

            binding.pieChart.apply {
                data = pieData
                description.isEnabled = false
                legend.isEnabled = false
                isRotationEnabled = true
                setEntryLabelColor(Color.BLACK)
                animateY(1400, Easing.EaseInOutQuad)
                invalidate() // 차트를 다시 그립니다.
            }
        } else {
            binding.pieChart.clear()
            binding.pieChart.invalidate()
        }
    }

    // 프로그레스바 업데이트
    private fun updateProgressBars(carbs: Float, protein: Float, fat: Float,
                                   recommendedCarbs: Int, recommendedProtein: Int, recommendedFat: Int) {
        binding.progressHorizontalCarbs.progress = (carbs / recommendedCarbs * 100).toInt()
        binding.progressHorizontalProtein.progress = (protein / recommendedProtein * 100).toInt()
        binding.progressHorizontalFat.progress = (fat / recommendedFat * 100).toInt()
    }

    // 칼로리 정보 업데이트
    private fun updateCaloriesInfo(nutritionPref: SharedPreferences) {
        binding.bkcal.text = "${nutritionPref.getInt("breakfast_calories", 0)} kcal"
        binding.lunchKcal.text = "${nutritionPref.getInt("lunch_calories", 0)} kcal"
        binding.dinnerKcal.text = "${nutritionPref.getInt("dinner_calories", 0)} kcal"
    }

    // 식사 선택 시 호출되는 함수
    private fun clickMeal(mealType: String) {
        currentMealType = mealType // 현재 선택된 식사 유형 저장( 아침, 점심, 저녁 )
        val items = arrayOf<CharSequence>("카메라로 촬영", "앨범에서 선택")
        AlertDialog.Builder(requireContext())
            .setTitle("사진선택")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> takePicture()
                    1 -> chooseFromGallery()
                }
            }
            .show()
    }

    // 카메라로 사진 촬영
    private fun takePicture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            dispatchTakePictureIntent()
        }
    }

    // 카메라 인텐트 실행
    private fun dispatchTakePictureIntent() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                it
            )
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // 갤러리에서 사진 선택
    private fun chooseFromGallery() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }
        }
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    // 이미지 파일 생성
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    // ResultActivity 열기
    private fun openResultActivity(imageUri: Uri?, imagePath: String?) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            if (imageUri != null) {
                putExtra("imageUri", imageUri.toString())
            } else if (imagePath != null) {
                putExtra("imagePath", imagePath)
            }
            putExtra("MEAL_TYPE", currentMealType)
        }
        startActivity(intent)
    }

    // 액티비티 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    openResultActivity(null, currentPhotoPath)
                }
                REQUEST_PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        openResultActivity(uri, null)
                    }
                }
            }
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    dispatchTakePictureIntent()
                } else {
                    Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val REQUEST_PICK_IMAGE = 3
    }
}