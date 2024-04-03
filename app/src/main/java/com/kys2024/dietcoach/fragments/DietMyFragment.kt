package com.kys2024.dietcoach.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kys2024.dietcoach.databinding.FragmentDietMyBinding

class DietMyFragment:Fragment() {

    private val binding by lazy { FragmentDietMyBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bmiBtn.setOnClickListener { clickBtn() }



    }


    private fun clickBtn(){


        var heightText=binding.inputKey.editText!!.text.toString()
        var weightText=binding.inputWeight.editText!!.text.toString()

        if (weightText.isNotEmpty()&&heightText.isNotEmpty()){
            val weight=weightText.toDouble()
            val height=heightText.toDouble()

            val bmi = calculateBMI(weight,height)
            val obesityGrade = getObesityGrade(bmi)

            binding.tvBmi.text="BMI: $bmi\n 비만도 등급:$obesityGrade"


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