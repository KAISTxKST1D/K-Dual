package com.example.k_dual

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import okhttp3.Call
import okhttp3.Callback
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.time.LocalDate


class PopUpUrl : AppCompatActivity() {

    private val httpClient = OkHttpClient()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_url)
        val sharedPref = this.getSharedPreferences("URL",Context.MODE_PRIVATE)
        val popup_url_background = this.findViewById<ConstraintLayout>(R.id.popup_url_background)
        val popup_window_view_with_border = this.findViewById<CardView>(R.id.popup_window_view_with_border)
        val popup_window_button = this.findViewById<Button>(R.id.popup_window_button)

        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_url_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        popup_window_view_with_border.alpha = 0f
        popup_window_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        var prevUrl = sharedPref.getString("URL", "Enter or Paste URL :")


        val editText = this.findViewById<EditText>(R.id.editTextUrl)
        editText.setText(prevUrl)

        popup_window_button.setOnClickListener {
            var urlStr = editText.getText().toString()
            //save url to sharedPreference
            with(sharedPref.edit()) {
                putString("URL", urlStr)
                apply()
            }
            //http request
            if (urlStr.contains("nightscout", ignoreCase = false)) {
                val date = LocalDate.now().toString().substring(0 until 10)
                urlStr=urlStr.plus("/api/v1/entries/sgv.json?find[dateString][\$gte]=${date}")
                Thread {
                    getBGDataByUrl(urlStr)
                }.start()
            }
            else if (urlStr.contains("carelink", ignoreCase = false)) {
                urlStr=urlStr.plus("")
                Thread {
                    getBGDataByUrl(urlStr)
                }.start()
            }
            else if (urlStr.contains("dexcom", ignoreCase = false)) {
                //TODO : add api to dexcom url
                Thread {
                    getBGDataByUrl(urlStr)
                }.start()
            }
            else if (urlStr.contains("libre", ignoreCase = false)) {
                urlStr=urlStr.plus("")
                Thread {
                    getBGDataByUrl(urlStr)
                }.start()
            }
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val alpha = 100 // between 0-255
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
                val colorAnimation =
                    ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
                colorAnimation.duration = 500 // milliseconds
                colorAnimation.addUpdateListener { animator ->
                    popup_url_background.setBackgroundColor(
                        animator.animatedValue as Int
                    )
                }
                popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
                    DecelerateInterpolator()
                ).start()

                // After animation finish, close the Activity
                colorAnimation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        finish()
                        overridePendingTransition(0, 0)
                    }
                })
                colorAnimation.start()
            }
        }
    }

    private fun getBGDataByUrl(urlStr: String) {
        val request = Request.Builder()
            .url(urlStr)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                Log.d("RESPONSE", response.body!!.string())
            }
        })
    }

}