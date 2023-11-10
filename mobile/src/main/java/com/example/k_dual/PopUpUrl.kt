package com.example.k_dual


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import okhttp3.Call
import okhttp3.Callback
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.time.LocalDate


class PopUpUrl : AppCompatActivity() {

    private val httpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_url)
        val sharedPref = this.getSharedPreferences("URL",Context.MODE_PRIVATE)
        val popupUrlBackground = this.findViewById<ConstraintLayout>(R.id.popup_url_background)
        val popupWindowViewWithBorder = this.findViewById<CardView>(R.id.popup_window_view_with_border)
        val popupWindowButton = this.findViewById<Button>(R.id.popup_window_button)

        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popupUrlBackground.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        popupWindowViewWithBorder.alpha = 0f
        popupWindowViewWithBorder.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        val prevUrl = sharedPref.getString("URL", "Enter or Paste URL :")


        val editText = this.findViewById<EditText>(R.id.editTextUrl)
        editText.setText(prevUrl)

        popupWindowButton.setOnClickListener {
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
                //TODO : add api to carelink url
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
                //TODO : add api to libre url
                urlStr=urlStr.plus("")
                Thread {
                    getBGDataByUrl(urlStr)
                }.start()
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