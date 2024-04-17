//package com.kys2024.dietcoach.activity
//
//import android.content.Intent
//import android.database.Cursor
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.loader.content.CursorLoader
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.CircleCrop
//import com.bumptech.glide.request.RequestOptions
//import com.kys2024.dietcoach.G
//import com.kys2024.dietcoach.data.UserAccount
//import com.psg2024.ex68retrofitmarketapp.RetrofitService
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import java.io.File
//import java.io.FileOutputStream
//import java.io.InputStream
//import java.io.OutputStream
//
//class BoardDBupload {
//    // 이미지를 설정하고 글쓰기를 다 했을때
//    // binding.boardIv
//
//    private fun clickPhoto(){ //버튼클릭시 내 앨범에서 사진선택해서 프로필 변경
//        val intent = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(
//            Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
//        resultLauncher.launch(intent)
//    }
//    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        val uri: Uri?=it.data?.data
//        if (uri!=null){
//            Glide.with(this).load(uri).apply(RequestOptions().into(binding.boardIv)
//
//            imgPath = getRealPathFromUri(uri)
//            clickUpload()
//        }
//    }
//    var imgPath: String?= null
//
//    private fun getRealPathFromUri(uri: Uri): String? {
//
//        // Uri[미디어저장소의 DB주소]로 부터 파일의 이름을 얻어오기 - DB SELECT 쿼리작업을 해주는 기능을 가진 객체를 이용
//        val cursorLoader : CursorLoader = CursorLoader(requireContext(), uri, null, null, null, null)
//        val cursor: Cursor? =cursorLoader.loadInBackground()
//        val fileName:String? = cursor?.run {
//            moveToFirst()
//            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
//        }
//
//        // 복사본이 저장될 파일의 경로와 파일명. 확장자
//        val file: File = File(requireContext().externalCacheDir,fileName)
//
//        //이제 진짜 복사작업 수행
//        val inputStream: InputStream =requireContext().contentResolver.openInputStream(uri) ?: return null
//        val outputStream: OutputStream = FileOutputStream(file)
//
//        //파일복사
//        while (true){
//            val buf: ByteArray = ByteArray(1024) // 빈 바이트 배열[길이1kb]
//            val len:Int =inputStream.read(buf) //스트림을 통해 읽어들인 바이트들을 buf배열에 넣어줌-- 읽어들인 바이트 수를 리턴해줌
//            if(len <=0) break
//            outputStream.write(buf, 0, len)
//        }
//        //반복문이 끝났으면 복사가 완료된 것임.
//        inputStream.close()
//        outputStream.close()
//        return file.absolutePath
//    }
//
//    private fun  clickUpload(){
//
//        imgPath ?: return
//
//        //1. retrofit 객체 생성
//        val builder: Retrofit.Builder = Retrofit.Builder()
//        builder.baseUrl("http://toachwithfire3.dothome.co.kr")
//        builder.addConverterFactory(ScalarsConverterFactory.create())
//        val retrofit: Retrofit = builder.build()
//
//        //2. 레트로핏 서비스 명세서 인터페이스 설계하기
//        val retrofitService = retrofit.create(RetrofitService::class.java)
//
//        val dataPart: MutableMap<String, String> = mutableMapOf()
//        dataPart["userid"] = G.userAccount!!.uid.toString()
//        dataPart["profile"] = G.userAccount!!.uri.toString()
//        dataPart["msg"] = binding.msg.editext!!.text.toString()//글쓴 텍스트들을 msg로
//
//
//        //3. 전송할 파일을 특별한 박스 (MultipartBody.Part객체)로 포장하기
//        val file: File = File(imgPath)
//        val requestBody: RequestBody =
//            file.asRequestBody("image/*".toMediaTypeOrNull()) //일종의 진공팩..[파일을 포장]
//        val filepart: MultipartBody.Part =
//            MultipartBody.Part.createFormData("img1",file.name, requestBody)//식별자, 파일명, 요청Body [택배 스티로폼 상자]
//
//
//        retrofitService.uploadboard(dataPart, filepart).enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                val s = response.body()
//                Toast.makeText(requireContext(), "$s 성공", Toast.LENGTH_SHORT).show()
//
//
//
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Toast.makeText(requireContext(), "실패:${t.message}", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//
//    }
//}