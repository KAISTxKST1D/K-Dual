package com.kaist.k_dual.model

import android.util.Log
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.Locale

enum class Trend {
    DOUBLEUP, SINGLEUP, FORTYFIVEUP, FLAT, FORTYFIVEDOWN, SINGLEDOWN, DOUBLEDOWN
}

data class DexcomEntry(
    val WT: String,
    val ST: String,
    val DT: String,
    val Value: Int,
    val Trend: String
)

data class GlucoseEntry(
    val mgdl: Int,
    val mmol: Double,
    val trend: Trend,
    val timestamp: Long
)

enum class DexcomServer {
    EU, US
}

data class ConfigurationProps(
    val username: String,
    val password: String,
    val server: DexcomServer
)

data class LatestGlucoseProps(
    val minutes: Int,
    val maxCount: Int
)

interface DexcomApiService {
    @POST("General/AuthenticatePublisherAccount")
    @Headers("Content-Type: application/json")
    @JvmSuppressWildcards
    suspend fun authenticate(@Body credentials: Map<String, String>): Response<String>

    @POST("General/LoginPublisherAccountById")
    @Headers("Content-Type: application/json")
    @JvmSuppressWildcards
    suspend fun login(@Body details: Map<String, String>): Response<String>

    @POST("Publisher/ReadPublisherLatestGlucoseValues")
    @Headers("Content-Type: application/json")
    @JvmSuppressWildcards
    suspend fun getEstimatedGlucoseValues(@Body params: Map<String, Any>): Response<List<DexcomEntry>>
}

class DexcomClient(configurationProps: ConfigurationProps) {
    private val username: String = configurationProps.username
    private val password: String = configurationProps.password
    private val server: DexcomServer = configurationProps.server

    companion object {
        const val APPLICATION_ID = "d8665ade-9673-4e27-9ff6-92db4ce13d13"
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(getBaseUrl(server))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: DexcomApiService = retrofit.create(DexcomApiService::class.java)

    suspend fun getAccountId(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.authenticate(
                    mapOf(
                        "applicationId" to APPLICATION_ID,
                        "accountName" to username,
                        "password" to password
                    )
                )

                if (!response.isSuccessful) {
                    throw Exception(
                        "Dexcom server responded with status: ${response.code()}, data: ${
                            response.errorBody()?.string()
                        }"
                    )
                }

                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                Log.e("getAccountId", "Request failed with error: ${e.message}")
                null
            }
        }
    }

    suspend fun getSessionId(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val accountId = getAccountId() ?: return@withContext null

                val response = service.login(
                    mapOf(
                        "applicationId" to APPLICATION_ID,
                        "accountId" to accountId,
                        "password" to password
                    )
                )

                if (!response.isSuccessful) {
                    throw Exception(
                        "Dexcom server responded with status: ${response.code()}, data: ${
                            response.errorBody()?.string()
                        }"
                    )
                }

                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("Request failed with error: ${e.message}")
            }
        }
    }

    suspend fun getEstimatedGlucoseValues(latestGlucoseProps: LatestGlucoseProps): List<GlucoseEntry>? {
        return withContext(Dispatchers.IO) {
            try {
                val sessionId = getSessionId() ?: return@withContext null

                val response = service.getEstimatedGlucoseValues(
                    mapOf(
                        "maxCount" to latestGlucoseProps.maxCount,
                        "minutes" to latestGlucoseProps.minutes,
                        "sessionId" to sessionId
                    )
                )

                if (!response.isSuccessful) {
                    throw Exception(
                        "Dexcom server responded with status: ${response.code()}, data: ${
                            response.errorBody()?.string()
                        }"
                    )
                }

                val dexcomEntries = response.body() ?: throw Exception("Response body is null")

                dexcomEntries.map { entry ->
                    val trend = Trend.valueOf(entry.Trend.uppercase(Locale.ROOT))
                    GlucoseEntry(
                        mgdl = entry.Value,
                        mmol = mgdlToMmol(entry.Value),
                        trend = trend,
                        timestamp = extractNumber(entry.WT)
                    )
                }
            } catch (e: Exception) {
                throw Exception("Request failed with error: ${e.message}")
            }
        }
    }

    private fun getBaseUrl(server: DexcomServer): String {
        return when (server) {
            DexcomServer.US -> "https://share2.dexcom.com/ShareWebServices/Services/"
            DexcomServer.EU -> "https://shareous1.dexcom.com/ShareWebServices/Services/"
        }
    }

}

fun mgdlToMmol(mgdl: Int): Double {
    return (mgdl / 18.0).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
}

fun extractNumber(str: String): Long {
    val match = Regex("\\d+").findAll(str).map { it.value }.toList()
    return match.first().toLong()
}