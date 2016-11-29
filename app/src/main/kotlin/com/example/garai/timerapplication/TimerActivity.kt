package com.example.garai.timerapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        val str = intent.getStringExtra("CODE")
        Toast.makeText(this, "RECEIVE" + str, Toast.LENGTH_LONG).show()

        //TODO katasaを入力→APIにリクエスト
        val api = AsyncJsonLoader()
        api.execute("http://27.120.120.174/NoodleApp/Index.php?jan_code="+str+"&katasa=2")

        }

}

