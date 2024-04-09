package com.kys2024.dietcoach

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화작업
        KakaoSdk.init(this, "9785b7a8e0249b0db09ed0d32b5a1680")
                                //네이티브앱키
    }
}