package com.example.garai.timerapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.garai.timerapplication.TimerActivity.CountDown
import android.R.attr.countDown
import android.app.Activity
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import org.json.JSONObject


class TimerActivity : Activity() {

    private var startButton: Button? = null
    private var stopButton: Button? = null
    private var timerText: TextView? = null
    private var radioGroup: RadioGroup? = null
    private var time: Long? = null
    private var countdown: CountDown? = null

    private var audioAttributes: AudioAttributes? = null
    private var soundPool: SoundPool? = null
    private var soundOne: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        var str = intent.getStringExtra("CODE")

        //ウィジェット取得
        timerText = findViewById(R.id.text_timer) as TextView?
        startButton = findViewById(R.id.button_start) as Button?
        radioGroup = findViewById(R.id.radioGroup1) as RadioGroup?

        timerText!!.setText("0:00.000")
        audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

        soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build()

        // one.wav をロードしておく
        soundOne = soundPool!!.load(this, R.raw.zxing_beep, 1)

        //イベント
        startButton!!.setOnClickListener {

            //値受け渡し
            var radioGroupId = radioGroup!!.checkedRadioButtonId
            val radioButton = findViewById(radioGroupId) as RadioButton?
            val btnText = radioButton!!.text

            val btnId = when (btnText) {

                "柔らかめ" -> 1
                "普通" -> 0
                "固め" -> 2

                else -> null

            }


            Log.d("HARDNESS", btnId.toString())


            //API通信
            val api = object : AsyncJsonLoader() {
                override fun onPostExecute(_result: JSONObject) {
                    Log.d("CALLBACK", _result.toString())

                    val check = _result.get("status")

                    if (check.equals("INSERT FAILED")) {
                        Toast.makeText(applicationContext, "バーコードが不正です", Toast.LENGTH_SHORT).show()
                        return
                    }

                    time = _result.getLong("wait_time")

                    time = time!!.times(1000)

                    countdown = CountDown(time!!, 10)
                    //countdown = CountDown(100, 10)
                    countdown!!.start()

                }
            }

            //仮値
//            str = "4902110000000"
           api.execute("http://27.120.120.174/NoodleApp/Index.php?jan_code=" + str + "&katasa=" + btnId)


        }


    }


    internal inner class CountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            // 完了
            timerText!!.setText("0:00.000")
            soundPool!!.play(soundOne, 1.0f, 1.0f, 0, 10, 1F)
            Toast.makeText(applicationContext, "完成！", Toast.LENGTH_LONG).show()
        }

        // インターバルで呼ばれる
        override fun onTick(millisUntilFinished: Long) {
            // 残り時間を分、秒、ミリ秒に分割
            val mm = millisUntilFinished / 1000 / 60
            val ss = millisUntilFinished / 1000 % 60
            val ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60

            timerText!!.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms))
        }
    }
}





