package com.kys2024.dietcoach.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kys2024.dietcoach.databinding.ActivityMyInformationBinding

class MyInformationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMyInformationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bmiBtn.setOnClickListener { clickBtn() }
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun clickBtn() {
        val ageText = binding.inputAge.editText?.text.toString().toIntOrNull()
        val heightText = binding.inputKey.editText?.text.toString().toDoubleOrNull()
        val weightText = binding.inputWeight.editText?.text.toString().toDoubleOrNull()

        if (weightText != null && heightText != null && ageText != null) {
            val isMale = binding.maleRadioButton.isChecked
            val bmr = if (isMale) {
                66.47 + (13.75 * weightText) + (5 * heightText) - (6.76 * ageText)
            } else {
                655.1 + (9.56 * weightText) + (1.85 * heightText) - (4.68 * ageText)
            }
            binding.tvBmr.text = "기초대사량: ${bmr.toInt()} Kcal"

            val bmi = calculateBMI(weightText, heightText)
            val obesityGrade = getObesityGrade(bmi)
            binding.tvBmi.text = "BMI: ${bmi.toInt()}\n비만도 등급:$obesityGrade"

            calculateAndSaveNutrition(bmr)
        }
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        val heightInMeters = height / 100
        return weight / (heightInMeters * heightInMeters)
    }

    private fun getObesityGrade(bmi: Double): String {
        return when {
            bmi < 20 -> "저체중"
            bmi in 20.0..24.9 -> "정상"
            bmi in 25.0..29.9 -> "과체중"
            else -> "비만"
        }
    }

    private fun calculateAndSaveNutrition(bmr: Double) {
        val carbsGram = (bmr * 0.5 / 4).toInt() // 탄수화물은 총 칼로리의 50%
        val proteinGram = (bmr * 0.3 / 4).toInt() // 단백질은 30%
        val fatGram = (bmr * 0.2 / 9).toInt() // 지방은 20%

        val sharedPref = getSharedPreferences("UserNutrition", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("RECOMMENDED_CARBS", carbsGram)
            putInt("RECOMMENDED_PROTEIN", proteinGram)
            putInt("RECOMMENDED_FAT", fatGram)
            apply()
        }

        // 값이 저장되었음을 알리는 토스트 메시지 표시
        Toast.makeText(this, "영양 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }
}