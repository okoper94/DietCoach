package com.kys2024.dietcoach.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.BoardItem

class BoardItemAdapter( val context : Context, val boardItems : List<BoardItem>) : RecyclerView.Adapter<BoardItemAdapter.VH>() {

    inner class VH( itemView : View ) : RecyclerView.ViewHolder( itemView ) {
        val imageView : ImageView = itemView.findViewById( R.id.write_iv )
        val editText : EditText = itemView.findViewById( R.id.write_msg_edit_text )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_borad_list_fragment, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return boardItems.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currentItem = boardItems[position]

        Glide.with(context)
            .load(currentItem.imageUri)
            .into(holder.imageView)

        holder.editText.setText( currentItem.message )
    }
}