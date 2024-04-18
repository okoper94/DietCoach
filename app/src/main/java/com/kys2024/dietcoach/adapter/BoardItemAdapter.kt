package com.kys2024.dietcoach.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.BoardData
import com.kys2024.dietcoach.data.BoardItem
import com.kys2024.dietcoach.databinding.RecyclerBoradListFragmentBinding

class BoardItemAdapter( val context : Context, val boardDatas : List<BoardData>) : RecyclerView.Adapter<BoardItemAdapter.VH>() {

    inner class VH(val binding : RecyclerBoradListFragmentBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater= LayoutInflater.from(context)
        val binding= RecyclerBoradListFragmentBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return boardDatas.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currentItem = boardDatas[position]
        Glide.with(context).load(currentItem.profileimg).into(holder.binding.tvProfile)
        holder.binding.tvNickname.text =currentItem.nickname
        Glide.with(context).load(currentItem.image).into(holder.binding.ivPicture)
        holder.binding.tvContent.text = currentItem.msg

    }
}