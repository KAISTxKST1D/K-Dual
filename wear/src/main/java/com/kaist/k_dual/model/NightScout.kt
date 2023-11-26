package com.kaist.k_dual.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception

data class nightScoutData(
    val id: String,
    val device: String,
    val date: Long,
    val dateString: String,
    val sgv: Int,
    val delta: Float,
    val direction: String,
    val type: String,
    val filtered: String,
    val unfiltered: String,
    val rssi: Int,
    val noise: Int,
    val systime: String,
    val utcOffset: Int
)

interface ApiService {
    @GET("api/v1/entries/sgv.json")
    fun getBGData(@Query("count") count: Int): retrofit2.Call<List<nightScoutData>>
}

class NightScout {
    suspend fun getBGDataByUrl(urlStr: String): Result<List<nightScoutData>> =
        withContext(Dispatchers.IO) {
            try {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(urlStr)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                var body: List<nightScoutData> =
                    listOf(nightScoutData("", "", 0, "", 0, 0.0f, "", "", "", "", 0, 0, "", 0))
                var done = false
                val apiService = retrofit.create(ApiService::class.java)
                apiService.getBGData(36).enqueue(object : Callback<List<nightScoutData>> {
                    override fun onResponse(
                        call: Call<List<nightScoutData>>,
                        response: Response<List<nightScoutData>>
                    ) {
                        body = response.body() ?: throw Exception("Response body is null")
                        done = true
                    }

                    override fun onFailure(call: Call<List<nightScoutData>>, t: Throwable) {
                        done = true
                        Log.e("getBGDataByUrl", t.message ?: "")
                    }

                })
                while (!done) {
                    delay(100)
                }
                Result.success(body)
            } catch (e: Exception) {
                Log.e("NightScout", "Exception: ${e.message}")
                Result.failure(e)
            }
        }
}