package com.example.garai.timerapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(delayFunc, 2000)


    }

    val delayFunc = Runnable {



        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
}
