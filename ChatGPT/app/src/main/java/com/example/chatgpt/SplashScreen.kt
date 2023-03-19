package com.example.chatgpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.coroutines.Runnable

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        var handler = Handler()
        var runnable =  Runnable{ kotlin.run {
          var intent = Intent(this,OnBoarding::class.java)
            startActivity(intent)
          finish()
        }
        }

        handler.postDelayed(runnable,3000)

    }
}