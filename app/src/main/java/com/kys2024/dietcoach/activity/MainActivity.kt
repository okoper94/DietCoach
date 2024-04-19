package com.kys2024.dietcoach.activity


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
import com.kys2024.dietcoach.data.BoardData
import com.kys2024.dietcoach.data.FoodData
import com.kys2024.dietcoach.data.FoodTime
import com.kys2024.dietcoach.data.LoadUserData
import com.kys2024.dietcoach.data.TodayDate
import com.kys2024.dietcoach.databinding.ActivityMainBinding
import com.kys2024.dietcoach.fragments.DietBoardFragment
import com.kys2024.dietcoach.fragments.DietCalendarFragment
import com.kys2024.dietcoach.fragments.DietHomeFragment
import com.kys2024.dietcoach.fragments.ManboFragment
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper
import com.psg2024.ex68retrofitmarketapp.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView


    private lateinit var foodDataList: List<FoodData>
    private lateinit var foodDataAdapter: FoodDataAdapter
    var loadBoardData: List<BoardData>? = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val formattedDate = dateFormat.format(today.time)
        G.todaydate = TodayDate(formattedDate)


        //툴바 타이틀제거
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setTitle("")
        binding.toolbar.setSubtitle("")

        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container_fragment, DietHomeFragment())
            .commit()

        binding.bottomnavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bnv_home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, DietHomeFragment()).commit()

                R.id.menu_bnv_board -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, DietBoardFragment()).commit()

                R.id.menu_bnv_calendar -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, DietCalendarFragment()).commit()

                R.id.menu_bnv_manbo -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, ManboFragment()).commit()
            }
            true
        }


        drawerLayout = binding.drawerLayout
        toolbar = binding.toolbar
        navigationView = binding.navigationView




        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
                var drawerNickname = findViewById<TextView>(R.id.drawer_name)
                drawerNickname.text=""
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.my_page -> {
                    val intent = Intent(this, MyPageActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.bmi_go -> {
                    val intent = Intent(this, MyInformationActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.drawer_logout -> {
                    AlertDialog.Builder(this)
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인") { p0, p1 ->
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
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
        loadDB2()
        loadDB()


    }

    private fun loadDB() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)


        val data: HashMap<String, String> = hashMapOf()
        data["userid"] = G.userAccount?.uid.toString()
        retrofitService.loadDataFromServer(data).enqueue(object : Callback<LoadUserData> {

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(p0: Call<LoadUserData>, p1: Response<LoadUserData>) {
                val s = p1.body()
                if (s != null)
                    G.userAccount?.uri = s.profileimg

            }

            override fun onFailure(p0: Call<LoadUserData>, p1: Throwable) {
                Toast.makeText(this@MainActivity, "${p1.message}", Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun loadDB2() {
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.loadDataFromServerboard().enqueue(object : Callback<List<BoardData>> {
            override fun onResponse(p0: Call<List<BoardData>>, p1: Response<List<BoardData>>) {
                if (p1.isSuccessful) {
                    loadBoardData = p1.body()

                }
            }

            override fun onFailure(p0: Call<List<BoardData>>, p1: Throwable) {
                Log.d("실패", "${p1.message}")
            }
        })

    }
}