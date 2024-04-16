package com.kys2024.dietcoach.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.data.FoodData

class FoodDataAdapter( private var foodDataList : List<FoodData> ) :
    RecyclerView.Adapter<FoodDataAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFoodName : TextView = itemView.findViewById(R.id.foodName)
        val textViewCalories : TextView = itemView.findViewById(R.id.calories)
        val textViewCarbs : TextView = itemView.findViewById(R.id.carbs)
        val textViewProteins : TextView = itemView.findViewById(R.id.proteins)
        val textViewFat : TextView = itemView.findViewById(R.id.fat)
        val textViewFiber : TextView = itemView.findViewById(R.id.fiber)
        val textViewSodium : TextView = itemView.findViewById(R.id.sodium)
        val textViewCholesterol : TextView = itemView.findViewById(R.id.cholesterol)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_food, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return foodDataList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val foodData = foodDataList[position]
        holder.textViewFoodName.text = foodData.foodName
        holder.textViewCalories.text = foodData.calories
        holder.textViewCarbs.text = foodData.carbsGram
        holder.textViewProteins.text = foodData.proteinGram
        holder.textViewFat.text = foodData.fatGram
        holder.textViewFiber.text = foodData.fiberGram
        holder.textViewSodium.text = foodData.sodiumGram
        holder.textViewCholesterol.text = foodData.cholesterolGram
    }

    fun updateData(newFoodDataList: List<FoodData>) {
        foodDataList = newFoodDataList
        notifyDataSetChanged()
    }
}