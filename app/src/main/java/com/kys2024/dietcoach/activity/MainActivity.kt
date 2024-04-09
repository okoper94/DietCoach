package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.ActivityMainBinding
import com.kys2024.dietcoach.fragments.DietBoardFragment
import com.kys2024.dietcoach.fragments.DietCalendarFragment
import com.kys2024.dietcoach.fragments.DietHomeFragment
import com.kys2024.dietcoach.fragments.DietMyFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView

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

        drawerLayout = binding.drawerLayout
        toolbar = binding.toolbar
        navigationView = binding.navigationView

        setSupportActionBar( toolbar )
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen( GravityCompat.START ) ) {
                drawerLayout.closeDrawer( GravityCompat.START )
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when( menuItem.itemId ) {
                R.id.bmi_go -> {
                    val intent = Intent( this, MyInformationActivity::class.java )
                    startActivity( intent )
                    drawerLayout.closeDrawer( GravityCompat.START )
                    true
                }
                else -> false
            }
        }

        val headerView = navigationView.getHeaderView(0)

        val drawerImage = headerView.findViewById<ImageView>(R.id.drawer_image)
        val drawerName = headerView.findViewById<TextView>(R.id.drawer_name)

        drawerImage.setOnClickListener {

        }

        drawerName.setOnClickListener {
            drawerName.text = "New Name"
        }

    } // onCreate..
}