package com.kys2024.dietcoach.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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






    }
    private fun clickBmi(){
        startActivity(Intent(requireContext(),MyInformationActivity::class.java))
    }



}