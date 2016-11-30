package com.example.garai.timerapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.AppLaunchChecker
import android.util.Log
import android.view.Window

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(delayFunc, 2000)

    }

    val delayFunc = Runnable {
        val intent = Intent(this, TermsActivity::class.java)
        startActivity(intent)
    }
}
