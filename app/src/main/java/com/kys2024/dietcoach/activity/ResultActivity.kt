package com.kys2024.dietcoach.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.kys2024.dietcoach.data.FoodData
import com.kys2024.dietcoach.data.FoodName
import com.kys2024.dietcoach.data.FoodResponse
import com.kys2024.dietcoach.databinding.ActivityResultBinding
import com.kys2024.dietcoach.ml.Modelfood
import com.kys2024.dietcoach.network.FoodApiService
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper2
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }
    private lateinit var foodDataList: List<FoodData>
    private val foodNameMap: MutableMap<String, FoodName> = mutableMapOf()
    private lateinit var mealType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 식사 유형 가져오기
        mealType = intent.getStringExtra("MEAL_TYPE") ?: "breakfast"

        // 툴바 설정
        setSupportActionBar(binding.toolbarBack)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("")
        binding.toolbarBack.setNavigationOnClickListener {
            onBackPressed()
        }

        // 삭제 버튼 클릭 리스너
        binding.resultBtnDelete.setOnClickListener {
            binding.resultIv.setImageResource(0)
            binding.resultTv.text = ""
        }

        // 저장 버튼 클릭 리스너 추가
        binding.resultBtnOk.setOnClickListener {
            saveNutritionData()
            finish() // ResultActivity 종료
        }

        // 음식 이름 데이터 로드
        loadFoodName()

        // 이미지 URI 처리
        val imageUri = intent.getStringExtra("imageUri")
        val imagePath = intent.getStringExtra("imagePath")
        when {
            imageUri != null -> loadAndAnalyzeImage(Uri.parse(imageUri))
            imagePath != null -> loadAndAnalyzeImage(Uri.parse("file://$imagePath"))
            else -> Toast.makeText(this, "이미지를 받아오지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    // 음식 이름 데이터 읽어오는 함수
    private fun loadFoodName() {
        try {
            val inputStream = assets.open("foodListUTF8.csv")
            InputStreamReader(inputStream).use { isr ->
                BufferedReader(isr).use { reader ->
                    reader.readLine() // Skip header
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val data = line!!.split(",")
                        foodNameMap[data[0]] = FoodName(data[1])
                    }
                }
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to load food names", Toast.LENGTH_SHORT).show()
            Log.e("ResultActivity", "Error loading food list: ", e)
        }
    }

    // 이미지 불러와서 imageAnalysis 함수에 떤지기
    private fun loadAndAnalyzeImage(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    binding.resultIv.setImageBitmap(resource)
                    imageAnalysis(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    // 이미지 분석하여 음식 식별하는 함수
    private fun imageAnalysis(bitmap: Bitmap) {
        val modelFood: Modelfood = Modelfood.newInstance(this)
        try {
            val image: TensorImage = TensorImage.fromBitmap(bitmap)
            val outputs: Modelfood.Outputs = modelFood.process(image)
            val category = outputs.probabilityAsCategoryList.maxByOrNull { it.score }
            displayResult(category)
        } finally {
            modelFood.close()
        }
    }

    // 결과 표시 함수
    private fun displayResult(category: Category?) {
        if (category != null) {
            val food = foodNameMap[category.label]
            if (food != null) {
                binding.resultTv.text = food.name
                fetchFoodData(food.name)
            } else {
                binding.resultTv.text = "음식을 인식할 수 없습니다."
            }
        }
    }

    // 음식 데이터 fetch 함수
    private fun fetchFoodData(query: String) {
        val retrofit = RetrofitHelper2.getRetrofitInstance("https://api.odcloud.kr/api/")
        val foodApiService = retrofit.create(FoodApiService::class.java)
        foodApiService.getFoods().enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        foodDataList = it.data
                        val filteredList = foodDataList.filter { it.foodName.contains(query, ignoreCase = true) }
                        if (filteredList.isNotEmpty()) {
                            updateUI(filteredList.first())
                        } else {
                            binding.resultTv.text = "검색 결과가 없습니다."
                        }
                    }
                } else {
                    Toast.makeText(this@ResultActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // UI 업데이트 함수
    private fun updateUI(food: FoodData) {
        binding.resultTv.text = "음식명: ${food.foodName}\n칼로리: ${food.calories}cal\n" +
                "탄수화물: ${food.carbsGram}g\n단백질: ${food.proteinGram}g\n지방: ${food.fatGram}g"

        val sharedPreferences = getSharedPreferences("FoodInfo", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("FoodName", food.foodName)
            putString("Calories", food.calories)
            putString("Carbs", food.carbsGram)
            putString("Protein", food.proteinGram)
            putString("Fat", food.fatGram)
            apply()
        }
    }

    // 영양 정보 저장 함수
    private fun saveNutritionData() {
        val sharedPreferences = getSharedPreferences("FoodInfo", MODE_PRIVATE)
        val calories = sharedPreferences.getString("Calories", "0")?.toIntOrNull() ?: 0
        val carbs = sharedPreferences.getString("Carbs", "0")?.toFloatOrNull() ?: 0f
        val protein = sharedPreferences.getString("Protein", "0")?.toFloatOrNull() ?: 0f
        val fat = sharedPreferences.getString("Fat", "0")?.toFloatOrNull() ?: 0f

        val nutritionPref = getSharedPreferences("NutritionData", MODE_PRIVATE)

        with(nutritionPref.edit()) {
            putInt("${mealType}_calories", nutritionPref.getInt("${mealType}_calories", 0) + calories)
            putFloat("${mealType}_carbs", nutritionPref.getFloat("${mealType}_carbs", 0f) + carbs)
            putFloat("${mealType}_protein", nutritionPref.getFloat("${mealType}_protein", 0f) + protein)
            putFloat("${mealType}_fat", nutritionPref.getFloat("${mealType}_fat", 0f) + fat)
            apply()
        }

        Toast.makeText(this, "영양 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    // 이미지가 선택되지 않았을 때 다이얼로그 표시
    private fun showImageNotSelectedDialog() {
        AlertDialog.Builder(this)
            .setMessage("사진이 없습니다")
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}


//    여러개 할꺼면 이걸로..
//        binding.btn.setOnClickListener { clickBtn() }
//    }
//
//    val foodNameMap : MutableMap<String, FoodName> = mutableMapOf()
//
//    private fun loadFoodName() {
//        val inputStream = assets.open( "foodListUTF8.csv" )
//        val inputStreamReader = InputStreamReader( inputStream )
//        val reader = BufferedReader( inputStreamReader )
//
//        reader.readLine()
//
//        val builder = StringBuilder()
//        while( true ) {
//            val line: String = reader.readLine() ?: break
//            builder.append(line + "\n")
//
//            val data: List<String> = line.split(",")
//            foodNameMap[data[0]] = FoodName(data[1])
//        }
//        binding.tv.text = "${foodNameMap}\n\n"
//        binding.tv.append( builder.toString() )
//    }
//
//    private fun clickBtn() {
//
//        // #1. ML kit Object Detection.. [ 최대 5개 인식 가능 ]
//
//        // 1. 입력이미지 준비
//        val bm = ( binding.iv.drawable as BitmapDrawable).bitmap
//        val image : InputImage = InputImage.fromBitmap( bm, 0 )
//
//        // 2. ml kit object detector instance
//        val options = ObjectDetectorOptions.Builder()
//            .setDetectorMode( ObjectDetectorOptions.SINGLE_IMAGE_MODE )
//            .enableMultipleObjects()
//            .build()
//        val objectDetector = ObjectDetection.getClient( options )
//
//        // 3. image process..
//        objectDetector.process( image ).addOnSuccessListener {
//            binding.tv.text = "인식된 음식의 개수 : ${it.size}\n\n"
//
//            // #2. 이미지 위에 사각형 영역을 그리기!!
//            val bitmap = bm.copy( bm.config, true )
//            val canvas = Canvas( bitmap )
//            val paint = Paint().apply {
//                color = Color.RED
//                style = Paint.Style.STROKE
//                strokeWidth = 4f
//
//            }
//            for( detectedObject in it ) {
//                // 1. 인식한 이미지 영역 그리기..
//                val bounds : Rect = detectedObject.boundingBox
//                canvas.drawRect( bounds, paint )
//
//                // 2. 영역만큼의 작은 Bitmap을 잘라서 그 영역에 대한 TFlite 이미지 분류 모델로 분석하기
//                val b : Bitmap = Bitmap.createBitmap( bm, bounds.left, bounds.top, bounds.width(), bounds.height() )
//                drawImageLabel( b, bounds, canvas, paint )
//
//            } // for..
//            binding.iv.setImageBitmap( bitmap )
//
//        }
//        loadFoodName()
//
//    } // clickBtn()..
//
//    private fun drawImageLabel( b : Bitmap, bounds : Rect, canvas : Canvas, paint : Paint ) {
//
//        // #3. 전달받은 조각그림(b)을 이미지 분류하기..
//
//        // 1. 모델 만들기
//        val modelFood : Modelfood = Modelfood.newInstance( this )
//
//        // 2. 입력 이미지 준비하기
//        val image : TensorImage = TensorImage.fromBitmap( b )
//
//        // 3. 이미지 처리.. 후 결과 받기
//        val outputs : Modelfood.Outputs = modelFood.process( image )
//
//        // 4. 여러 가능성 중 가장 확률이 높은 식별요소 얻어오기
//        val category = outputs.probabilityAsCategoryList.maxByOrNull { it.score }
//
//        if( category != null ) {
//
//            // 5. 식별된 라벨을 출력하기.. #5..
//            val food : FoodName? = foodNameMap[ "${category!!.label}"]
////        binding.tv.append( "${category?.label}\n" )
//            binding.tv.append( "${food!!.name}\n")
//
//            // 6. canvas에 라벨글씨 그리기..
//            paint.textSize = 24f
//            canvas.drawText( "${category?.label ?: ""}", bounds.left.toFloat(), bounds.top.toFloat() + 24f, paint )
//
//        }
//        modelFood.close()
//    }
//}