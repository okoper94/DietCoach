package com.kys2024.dietcoach.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        binding.btn.setOnClickListener {
            startActivity( Intent( requireActivity(), WriteBoardActivity::class.java ) )
        }

        val boardItems = loadBoardItems()
        binding.recyclerViewBoard.adapter = BoardItemAdapter( requireContext(), boardItems )

        binding.recyclerViewBoard.layoutManager = LinearLayoutManager( context )
    }

    private fun loadBoardItems() : List<BoardItem> {
        return listOf(
//            BoardItem( "이미지uri..", "메세지.." )
        )
    }
}