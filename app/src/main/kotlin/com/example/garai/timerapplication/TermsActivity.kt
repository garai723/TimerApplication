package com.example.garai.timerapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.AppLaunchChecker
import android.util.Log
import android.widget.Toast
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.preference.PreferenceManager



class TermsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val  PREF_KEY="first login check"
        if (defaultSharedPreferences.getBoolean(PREF_KEY, true)) {
            defaultSharedPreferences.edit().putBoolean(PREF_KEY, false).apply()
            Log.d("AppLaunchChecker","1回目")
        } else {
            Log.d("AppLaunchChecker","2回目")
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }


        setContentView(R.layout.activity_terms)

        val button = findViewById(R.id.button4)
        button.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}
