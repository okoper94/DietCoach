package com.kys2024.dietcoach.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        val nutritionProgressView = binding.nutritionProgressView
        nutritionProgressView.setNutritionValues( carb = 50f, protein = 30f, fat = 20f )
    }
}