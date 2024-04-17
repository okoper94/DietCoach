package com.kys2024.dietcoach.activity


import android.content.Context
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kys2024.dietcoach.G
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.adapter.FoodDataAdapter
import com.kys2024.dietcoach.data.FoodData
import com.kys2024.dietcoach.data.FoodResponse
import com.kys2024.dietcoach.data.LoadBoardData
import com.kys2024.dietcoach.data.LoadUserData
import com.kys2024.dietcoach.data.UserAccount
import com.kys2024.dietcoach.databinding.ActivityMainBinding
import com.kys2024.dietcoach.fragments.DietBoardFragment
import com.kys2024.dietcoach.fragments.DietCalendarFragment
import com.kys2024.dietcoach.fragments.DietHomeFragment
import com.kys2024.dietcoach.fragments.DietMyFragment
import com.kys2024.dietcoach.fragments.ManboFragment
import com.kys2024.dietcoach.network.FoodApiService
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper2
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var foodDataList: List<FoodData>
    private lateinit var foodDataAdapter: FoodDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //툴바 타이틀제거
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setTitle("")
        binding.toolbar.setSubtitle("")


        setContentView(binding.root)
        Log.d("id보기", "id: ${G.userAccount?.uid}")
        val sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userid",G.userAccount?.uid.toString())
        editor.apply()

        loadDataFromServerboard()




        supportFragmentManager.beginTransaction().add(R.id.container_fragment,DietHomeFragment()).commit()

        binding.bottomnavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_bnv_home ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment,DietHomeFragment()).commit()
                R.id.menu_bnv_board ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment, DietBoardFragment()).commit()
                R.id.menu_bnv_calendar ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment,DietCalendarFragment()).commit()
                R.id.menu_bnv_manbo ->supportFragmentManager.beginTransaction().replace(R.id.container_fragment,ManboFragment()).commit()
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
                R.id.my_page ->{
                    val intent = Intent(this,MyPageActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.bmi_go -> {
                    val intent = Intent( this, MyInformationActivity::class.java )
                    startActivity( intent )
                    drawerLayout.closeDrawer( GravityCompat.START )
                    true
                }
                R.id.drawer_logout -> {
                    AlertDialog.Builder(this)
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인") { p0, p1 ->
                            val intent = Intent( this, LoginActivity::class.java )
                            startActivity( intent )
                            G.userAccount!!.uid = ""
                        }
                        .setNegativeButton("취소", null)
                        .show()
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

    override fun onResume() {
        super.onResume()
        loadDB()
    }

    private fun loadDB() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)


        val data :HashMap<String,String> = hashMapOf()
        data["userid"] = G.userAccount!!.uid.toString()
        retrofitService.loadDataFromServer(data).enqueue(object : Callback<LoadUserData>{

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(p0: Call<LoadUserData>, p1: Response<LoadUserData>) {
                val s = p1.body()
                if(s!=null)
                G.userAccount = UserAccount(uri = s.profileimg, nickname = s.nickname)
            }

            override fun onFailure(p0: Call<LoadUserData>, p1: Throwable) {
                Toast.makeText(this@MainActivity, "${p1.message}", Toast.LENGTH_SHORT).show()
            }

        })


    }
    private fun loadDB2() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.loadDataFromServerboard().enqueue(object : Callback<LoadBoardData>{
            override fun onResponse(p0: Call<LoadBoardData>, p1: Response<LoadBoardData>) {

            }

            override fun onFailure(p0: Call<LoadBoardData>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })


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