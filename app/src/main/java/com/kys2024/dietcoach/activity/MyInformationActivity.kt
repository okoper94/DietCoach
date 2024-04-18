package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityMyInformationBinding
import com.kys2024.dietcoach.databinding.FragmentDietMyBinding

class MyInformationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMyInformationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_my_information)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContentView(binding.root)
        binding.bmiBtn.setOnClickListener { clickBtn() }
        binding.toolbar.setNavigationOnClickListener { finish() }
//        binding.saveBtn.setOnClickListener { clickSave() }


    }
//    private fun clickSave(){
//        finish()
//    }

    private fun clickBtn(){


        var ageText=binding.inputAge.editText!!.text.toString().toIntOrNull()
        var heightText=binding.inputKey.editText!!.text.toString().toDoubleOrNull()
        var weightText=binding.inputWeight.editText!!.text.toString().toDoubleOrNull()

        if (weightText!=null && heightText!=null && ageText!=null){
            val isMale = binding.maleRadioButton.isChecked
            if (isMale){
                val isMale1=66.47+(13.75*weightText.toInt()) + (5*heightText.toInt()) - (6.76*ageText.toInt()).toInt()
                binding.tvBmr.text="기초대사량: ${isMale1.toInt()} Kcal"
            }else{
                val famle1=655.1+(9.56*weightText.toInt()) + (1.85*heightText.toInt()) - (4.68*ageText.toInt()).toInt()
                binding.tvBmr.text="기초대사량: ${famle1.toInt()} Kcal"
            }
        }

        if (weightText!=null && heightText!=null){
            val weight=weightText.toDouble()
            val height=heightText.toDouble()

            val bmi = calculateBMI(weight,height)
            val obesityGrade = getObesityGrade(bmi)

            binding.tvBmi.text="BMI: ${bmi.toInt()}\n비만도 등급:$obesityGrade"
        }
    }


    private fun calculateBMI(weight:Double,height:Double):Double{
        val heightInMeters = height/100
        return weight/(heightInMeters*heightInMeters)
    }
    private fun getObesityGrade(bmi:Double):String{
        return when{
            bmi<20 ->"저체중"
            bmi in 20.0..24.9 ->"정상"
            bmi in 25.0..29.9 ->"과체중"
            else->"비만"
        }
    }
}