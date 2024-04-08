package com.kys2024.dietcoach.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityMainBinding
import com.kys2024.dietcoach.fragments.DietBoardFragment
import com.kys2024.dietcoach.fragments.DietCalendarFragment
import com.kys2024.dietcoach.fragments.DietHomeFragment
import com.kys2024.dietcoach.fragments.DietMyFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container_fragment,DietHomeFragment()).commit()

        binding.bottomnavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_bnv_home ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment,DietHomeFragment()).commit()
                R.id.menu_bnv_board ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment, DietBoardFragment()).commit()
                R.id.menu_bnv_calendar ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment,DietCalendarFragment()).commit()
                R.id.menu_bnv_my ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment,DietMyFragment()).commit()
            }
            true
        }




    }
}