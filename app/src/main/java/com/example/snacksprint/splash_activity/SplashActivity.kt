package com.example.snacksprint.splash_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.snacksprint.R
import com.example.snacksprint.home_activity.HomepageActivity
import com.example.snacksprint.log_in.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@OptIn(DelicateCoroutinesApi::class)
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, HomepageActivity::class.java))
        }

    }
}