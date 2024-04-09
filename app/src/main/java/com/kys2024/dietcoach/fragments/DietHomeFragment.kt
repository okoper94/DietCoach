package com.kys2024.dietcoach.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
        }
    }

    }