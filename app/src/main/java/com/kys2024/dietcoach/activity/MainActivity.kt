package com.kys2024.dietcoach.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.adapter.FoodDataAdapter
import com.kys2024.dietcoach.data.FoodData
import com.kys2024.dietcoach.data.FoodResponse
import com.kys2024.dietcoach.databinding.ActivityMainBinding
import com.kys2024.dietcoach.fragments.DietBoardFragment
import com.kys2024.dietcoach.fragments.DietCalendarFragment
import com.kys2024.dietcoach.fragments.DietHomeFragment
import com.kys2024.dietcoach.fragments.DietMyFragment
import com.kys2024.dietcoach.network.FoodApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var foodDataList: List<FoodData>
    private lateinit var foodDataAdapter: FoodDataAdapter

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

        }
    } // onCreate..

//    private fun fetchFoodData(query: String) {
//        val retrofit = RetrofitHelper2.getRetrofitInstance("https://api.odcloud.kr/api/")
//        val foodApiService = retrofit.create(FoodApiService::class.java)
//        val call = foodApiService.getFoods()
//
//        call.enqueue(object : Callback<FoodResponse> {
//            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
//                if (response.isSuccessful) {
//                    val foodResponse = response.body()
//                    if (foodResponse != null) {
//                        foodDataList = foodResponse.data
//                        val filteredList = foodDataList.filter { it.foodName.contains(query, ignoreCase = true) }
//                        foodDataAdapter.updateData(filteredList)
//                    }
//                } else {
//                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}