package com.kys2024.dietcoach.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kys2024.dietcoach.activity.WriteBoardActivity
import com.kys2024.dietcoach.databinding.FragmentDietBoardBinding

class DietBoardFragment :Fragment(){

    private val binding by lazy { FragmentDietBoardBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn.setOnClickListener {
            startActivity( Intent( requireActivity(), WriteBoardActivity::class.java ) )



        }


    }
}