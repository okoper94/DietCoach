package com.kys2024.dietcoach.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kys2024.dietcoach.databinding.ActivityCarenderViewBinding
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class CalendarViewActivity : AppCompatActivity() {

    val binding by lazy { ActivityCarenderViewBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(binding.root)




    }
}