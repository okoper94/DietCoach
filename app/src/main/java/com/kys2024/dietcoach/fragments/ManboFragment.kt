package com.kys2024.dietcoach.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.FragmentManboBinding


class ManboFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCountSensor: Sensor? = null
    private lateinit var binding: FragmentManboBinding
    private var lastCheckedDate: Int = 0

    companion object {
        private const val PERMISSION_ACTIVITY_RECOGNITION_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManboBinding.inflate(inflater, container, false)
        //Glide.with(binding.root).load(R.drawable.move).into(binding.ivMove)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // 만보 센서 체크
        if (stepCountSensor == null) {
            Toast.makeText(requireContext(), "해당 기기에는 만보 센서가 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 권한 체크
            if (!isActivityRecognitionPermissionGranted()) {
                // 권한 요청
                requestActivityRecognitionPermission()
            } else {
                // 권한이 이미 허용된 경우 센서 리스너 등록
                registerSensorListener()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isActivityRecognitionPermissionGranted(): Boolean {
        // Activity Recognition 권한 체크
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestActivityRecognitionPermission() {
        // Activity Recognition 권한 요청
        requestPermissions(
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            PERMISSION_ACTIVITY_RECOGNITION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ACTIVITY_RECOGNITION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허용 시 센서 리스너 등록
                registerSensorListener()
            } else {
                // 권한이 거부된 경우 토스트 메시지로 안내
                Toast.makeText(
                    requireContext(),
                    "만보 센서를 사용하기 위해 권한이 필요합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registerSensorListener() {
        // 센서 리스너 등록
        stepCountSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 센서 정확도 변경 이벤트 처리
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // 만보 센서 데이터 처리
        event?.let {
            if (it.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
                var stepCount = it.values[0].toInt()
                if(isDateChanged()){
                    stepCount = 0
                }



                updateStepCount(stepCount)
            }
        }
    }


    private fun getCurrentDate(): Int{
        val calendar= Calendar.getInstance()
        return calendar.get(Calendar.YEAR)*10000 +
                (calendar.get(Calendar.MONTH+1))*100 +
                calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun isDateChanged(): Boolean{
        val currentDate = getCurrentDate()
        return currentDate != lastCheckedDate
    }

    private fun updateStepCount(stepCount: Int) {
        // UI 업데이트
        binding.tvStepbox.text = "총 $stepCount / 10000 걸음 수"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 센서 리스너 해제
        sensorManager.unregisterListener(this)
    }
}