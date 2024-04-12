package com.kys2024.dietcoach.fragments

import android.Manifest.*
import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.kys2024.dietcoach.R
import com.kys2024.dietcoach.databinding.FragmentManboBinding

class ManboFragment : Fragment() {

    private lateinit var sensorManager: SensorManager
    private var stepCountSensor: Sensor? = null
    private lateinit var binding: FragmentManboBinding

    private val stepCountListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val stepCount = event.values[0].toInt()
                activity?.runOnUiThread {
                    binding.tvStepbox.text = "걸음 수: $stepCount"
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // 센서 정확도 변경 이벤트 처리
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManboBinding.inflate(inflater, container, false)

        // ImageView에 Glide 이미지 로딩
        Glide.with(requireContext())
            .load(R.drawable.move)
            .into(binding.ivMove)

        // SensorManager 초기화
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 만보기 센서 가져오기
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Activity Recognition 권한 체크 및 요청
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(permission.ACTIVITY_RECOGNITION), 0)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 센서 리스너 등록
        stepCountSensor?.let {
            sensorManager.registerListener(
                stepCountListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // 센서 리스너 해제
        sensorManager.unregisterListener(stepCountListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 퍼미션 허용되면 센서 리스너 다시 등록
            stepCountSensor?.let {
                sensorManager.registerListener(
                    stepCountListener,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }
    }
}




