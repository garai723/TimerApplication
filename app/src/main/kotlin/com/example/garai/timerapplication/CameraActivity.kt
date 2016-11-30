package com.example.garai.timerapplication

import java.io.IOException
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.hardware.Camera.PreviewCallback
import android.hardware.Camera.Size
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Toast

import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ReaderException
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer

class CameraActivity : Activity(), SurfaceHolder.Callback, AutoFocusCallback, PreviewCallback {

    private val TAG = "SampleQrActivity"
    private var mCamera: Camera? = null
    private var mSurfaceView: SurfaceView? = null
    private var mPreviewSize: Point? = null
    private var mPreviewWidthRatio: Float = 0.toFloat()
    private var mPreviewHeightRatio: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initCamera()
    }

    override fun onResume() {
        super.onResume()
        mCamera = Camera.open()
        (mCamera as Camera?)!!.setDisplayOrientation(90)

        setPreviewSize()
    }

    override fun onPause() {
        super.onPause()
        if (mCamera != null) {
            mCamera!!.release()
            mCamera = null
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            if (mCamera != null) {
                mCamera!!.setPreviewDisplay(holder)
            }
        } catch (exception: IOException) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception)
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (mCamera != null) {
            val parameters = mCamera!!.parameters
            parameters.setPreviewSize(mPreviewSize!!.x, mPreviewSize!!.y)
            mCamera!!.parameters = parameters
            mCamera!!.startPreview()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (mCamera != null) {
            mCamera!!.stopPreview()
        }
    }

    private fun initCamera() {
        mSurfaceView = findViewById(R.id.preview) as SurfaceView
        val holder = mSurfaceView!!.holder
        holder.addCallback(this)
    }

    private fun setPreviewSize() {
        val parameters = mCamera!!.parameters
        val rawPreviewSizes = parameters.supportedPreviewSizes
        val supportPreviewSizes = ArrayList(rawPreviewSizes)
        Collections.sort(supportPreviewSizes, Comparator<Size> { lSize, rSize ->
            val lPixels = lSize.width * lSize.height
            val rPixels = rSize.width * rSize.height
            if (rPixels < lPixels) {
                return@Comparator -1
            }
            if (rPixels > lPixels) {
                return@Comparator 1
            }
            0
        })
        val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val screenWidth = display.width
        val screenHeight = display.height
        val screenAspectRatio = screenWidth.toFloat() / screenHeight.toFloat()
        var bestSize: Point? = null
        var diff = java.lang.Float.POSITIVE_INFINITY
        for (supportPreviewSize in supportPreviewSizes) {
            val supportWidth = supportPreviewSize.width
            val supportHeight = supportPreviewSize.height
            val pixels = supportWidth * supportHeight
            if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
                continue
            }
            val isPortrait = supportWidth < supportHeight
            val previewWidth = if (isPortrait) supportHeight else supportWidth
            val previewHeight = if (isPortrait) supportWidth else supportHeight
            if (previewWidth == screenWidth && previewHeight == screenHeight) {
                mPreviewSize = Point(supportWidth, supportHeight)
                mPreviewWidthRatio = 1f
                mPreviewHeightRatio = 1f
                return
            }
            val aspectRatio = previewWidth.toFloat() / previewHeight.toFloat()
            val newDiff = Math.abs(aspectRatio - screenAspectRatio)
            if (newDiff < diff) {
                bestSize = Point(supportWidth, supportHeight)
                diff = newDiff
            }
        }
        if (bestSize == null) {
            val defaultSize = parameters.previewSize
            bestSize = Point(defaultSize.width, defaultSize.height)
        }
        mPreviewSize = bestSize
        mPreviewWidthRatio = mPreviewSize!!.x.toFloat() / screenWidth.toFloat()
        mPreviewHeightRatio = mPreviewSize!!.y.toFloat() / screenHeight.toFloat()
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
        var rawResult: Result? = null
        val left = (findViewById(R.id.target).left * mPreviewWidthRatio).toInt()
        val top = (findViewById(R.id.target).top * mPreviewHeightRatio).toInt()
        val width = (findViewById(R.id.target).width * mPreviewWidthRatio).toInt()
        val height = (findViewById(R.id.target).height * mPreviewHeightRatio).toInt()
        val source = PlanarYUVLuminanceSource(data, mPreviewSize!!.x,
                mPreviewSize!!.y, left, top, width, height, false)
        if (source != null) {
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            val multiFormatReader = MultiFormatReader()
            try {
                rawResult = multiFormatReader.decode(bitmap)
                Toast.makeText(applicationContext, rawResult!!.text, Toast.LENGTH_LONG)
                        .show()

                val intent = Intent(this, TimerActivity::class.java)
                intent.putExtra( "CODE",rawResult!!.text)
                startActivity(intent)

            } catch (re: ReaderException) {
                Toast.makeText(applicationContext, "読み取りに失敗しました",
                        Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onAutoFocus(success: Boolean, camera: Camera) {
        if (success) {
            mCamera!!.setOneShotPreviewCallback(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mCamera != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                mCamera!!.autoFocus(this)
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {

        private val MIN_PREVIEW_PIXELS = 470 * 320
        private val MAX_PREVIEW_PIXELS = 1280 * 720
    }
}