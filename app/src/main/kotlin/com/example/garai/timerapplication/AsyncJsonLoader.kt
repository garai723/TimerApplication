package com.example.garai.timerapplication

import android.os.AsyncTask
import android.util.Log

import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONException
import org.json.JSONObject

import java.io.ByteArrayOutputStream
import java.io.IOException

class AsyncJsonLoader : AsyncTask<String, Int, JSONObject>() {

    override fun onPostExecute(_result: JSONObject) {
        super.onPostExecute(_result)

        //APIリクエスト確認用
        Log.d("API", _result.toString())
    }



    override fun doInBackground(vararg _uri: String): JSONObject? {
        val httpClient = DefaultHttpClient()
        val httpGet = HttpGet(_uri[0])
        try {
            val httpResponse = httpClient.execute(httpGet)
            if (httpResponse.statusLine.statusCode == HttpStatus.SC_OK) {
                val outputStream = ByteArrayOutputStream()
                httpResponse.entity.writeTo(outputStream)
                outputStream.close()
                return JSONObject(outputStream.toString())
            } else {
                httpResponse.entity.content.close()
                throw IOException()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }
}