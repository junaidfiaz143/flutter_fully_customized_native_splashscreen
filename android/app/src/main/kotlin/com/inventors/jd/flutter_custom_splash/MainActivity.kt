package com.inventors.jd.flutter_custom_splash

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.SplashScreen

class MainActivity : FlutterActivity() {

    override fun provideSplashScreen(): SplashScreen {
        return MySplashScreen()
    }
}
