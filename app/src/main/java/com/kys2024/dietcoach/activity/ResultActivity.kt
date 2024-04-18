package com.kys2024.dietcoach.activity

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kys2024.dietcoach.adapter.FoodDataAdapter
import com.kys2024.dietcoach.data.FoodData
import com.kys2024.dietcoach.data.FoodName
import com.kys2024.dietcoach.data.FoodResponse
import com.kys2024.dietcoach.databinding.ActivityResultBinding
import com.kys2024.dietcoach.ml.Modelfood
import com.kys2024.dietcoach.network.FoodApiService
import com.psg2024.ex68retrofitmarketapp.RetrofitHelper2
import org.tensorflow.lite.support.image.TensorImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

class ResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultBinding.inflate( layoutInflater ) }

    private lateinit var foodDataList: List<FoodData>
    private lateinit var foodDataAdapter: FoodDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imageAnalysis()
    }

    val foodNameMap: MutableMap<String, FoodName> = mutableMapOf()

    private fun loadFoodName() {
        val inputStream = assets.open( "foodListUTF8.csv" )
        val inputStreamReader = InputStreamReader( inputStream )
        val reader = BufferedReader( inputStreamReader )

        reader.readLine()

        val builder = StringBuilder()
        while( true ) {
            val line: String = reader.readLine() ?: break
            builder.append(line + "\n")

            val data: List<String> = line.split(",")
            foodNameMap[data[0]] = FoodName(data[1])
        }
        binding.resultTv.text = "${foodNameMap}\n\n"
        binding.resultTv.append( builder.toString() )
    }

    private fun imageAnalysis() {

        loadFoodName()

        val bm = (binding.resultIv.drawable as BitmapDrawable).bitmap
        val modelFood: Modelfood = Modelfood.newInstance(this)
        val image: TensorImage = TensorImage.fromBitmap(bm)
        val outputs: Modelfood.Outputs = modelFood.process(image)
        val category = outputs.probabilityAsCategoryList.maxByOrNull { it.score }

        if( category != null ) {
            val food : FoodName? = foodNameMap[category.label]
            if( food != null ) {
                binding.resultTv.text = food.name
                fetchFoodData(food.name)
            } else {
                binding.resultTv.text = "음식을 인식할 수 없습니다."
            }
        }

        // 모델 닫기
        modelFood.close()
    }

        private fun fetchFoodData(query: String) {
        val retrofit = RetrofitHelper2.getRetrofitInstance("https://api.odcloud.kr/api/")
        val foodApiService = retrofit.create(FoodApiService::class.java)
        val call = foodApiService.getFoods()

        call.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foodResponse = response.body()
                    if (foodResponse != null) {
                        foodDataList = foodResponse.data
                        val filteredList = foodDataList.filter { it.foodName.contains(query, ignoreCase = true) }
                        if (filteredList.isNotEmpty()) {
                            binding.resultTv.text =
                                "음식명 : ${filteredList.first().foodName} ${filteredList.first().calories} 칼로리 " +
                                        "\n 탄수화물 : ${filteredList.first().carbsGram} \n " +
                                        "단백질 : ${filteredList.first().proteinGram} \n 지방 : ${filteredList.first().fatGram}"
                            Toast.makeText(this@ResultActivity, "됬나", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.resultTv.text = "검색 결과가 없습니다."
                        }
                    }
                } else {
                    Toast.makeText(this@ResultActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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