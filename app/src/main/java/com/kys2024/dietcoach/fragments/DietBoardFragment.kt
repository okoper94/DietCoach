package com.kys2024.dietcoach.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.kys2024.dietcoach.activity.MainActivity
import com.kys2024.dietcoach.activity.WriteBoardActivity
import com.kys2024.dietcoach.adapter.BoardItemAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.kys2024.dietcoach.activity.WriteBoardActivity
import com.kys2024.dietcoach.adapter.BoardItemAdapter
import com.kys2024.dietcoach.data.BoardItem
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

        val ma:MainActivity = activity as MainActivity
        ma.loadBoardData ?: return
        binding.recyclerViewBoard.adapter = BoardItemAdapter(requireContext(), ma.loadBoardData!!)


        binding.btn.setOnClickListener {
            startActivity( Intent( requireActivity(), WriteBoardActivity::class.java ) )



        }





    }
    override fun onResume() {
        super.onResume()
        binding.recyclerViewBoard.adapter?.notifyDataSetChanged()



    }
}