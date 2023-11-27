package com.kaist.k_dual.presentation

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.kaist.k_canvas.DeviceType
import com.kaist.k_canvas.GlucoseUnits
import com.kaist.k_canvas.PREFERENCES_FILE_KEY
import com.kaist.k_canvas.SETTINGS_KEY
import com.kaist.k_canvas.Setting
import com.kaist.k_dual.model.ConfigurationProps
import com.kaist.k_dual.model.DexcomClient
import com.kaist.k_dual.model.DexcomServer
import com.kaist.k_dual.model.GlucoseEntry
import com.kaist.k_dual.model.LatestGlucoseProps
import com.kaist.k_dual.model.NightScout
import com.kaist.k_dual.model.Trend
import com.kaist.k_dual.model.nightScoutData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.math.round

val defaultGraphNightScoutData: List<nightScoutData> =
    listOf(nightScoutData("", "", 0, "", 0, 0.0f, "", "", "", "", 0, 0, "", 0))
val defaultGraphDexcomData: List<GlucoseEntry> = listOf(GlucoseEntry(0, 0.0, Trend.FLAT, 0))

object UseBloodGlucose {
    var firstUser: String = "-"
    var secondUser: String = "-"
    var firstUserDiff: String = "0"
    var secondUserDiff: String = "0"
    var firstUserGraphNightScoutData = defaultGraphNightScoutData
    var secondUserGraphNightScoutData = defaultGraphNightScoutData
    var firstUserGraphDexcomData = defaultGraphDexcomData
    var secondUserGraphDexcomData = defaultGraphDexcomData

    fun updateBloodGlucose(context: Context) {
        val latestGlucoseProps = LatestGlucoseProps(180, 36)

        val sharedPref = context.getSharedPreferences(
            PREFERENCES_FILE_KEY,
            Context.MODE_PRIVATE
        )

        val jsonString = sharedPref.getString(SETTINGS_KEY, null)

        val settings = if (jsonString != null) {
            try {
                Gson().fromJson(jsonString, Setting::class.java)
            } catch (e: JsonSyntaxException) {
                null
            } catch (e: JsonParseException) {
                null
            }
        } else {
            null
        } ?: return

        val glucoseUnit = settings.glucoseUnits

        CoroutineScope(Dispatchers.Main).launch {
            when (settings.firstUserSetting.deviceType) {
                DeviceType.Nightscout -> {
                    val dataArray = getNightScoutData(
                        url = settings.firstUserSetting.nightscoutUrl,
                        glucoseUnit = glucoseUnit
                    )
                    if (dataArray.isNotEmpty()) {
                        firstUserGraphNightScoutData = dataArray[0] as List<nightScoutData>
                        firstUser = dataArray[1] as String
                        firstUserDiff = dataArray[2] as String
                    } else {
                        firstUserGraphNightScoutData = defaultGraphNightScoutData
                        firstUser = "-"
                        firstUserDiff = "0"
                    }
                }

                DeviceType.Dexcom -> {
                    val dataArray = getDexcomData(
                        dexcomId = settings.firstUserSetting.dexcomId,
                        dexcomPassword = settings.firstUserSetting.dexcomPassword,
                        latestGlucoseProps = latestGlucoseProps,
                        glucoseUnit = glucoseUnit
                    )
                    if (dataArray.isNotEmpty()) {
                        firstUserGraphDexcomData = dataArray[0] as List<GlucoseEntry>
                        firstUser = dataArray[1] as String
                        firstUserDiff = dataArray[2] as String
                    } else {
                        firstUserGraphDexcomData = defaultGraphDexcomData
                        firstUser = "-"
                        firstUserDiff = "0"
                    }
                }
            }
            if (settings.enableDualMode) {
                when (settings.secondUserSetting.deviceType) {
                    DeviceType.Nightscout -> {
                        val dataArray = getNightScoutData(
                            url = settings.secondUserSetting.nightscoutUrl,
                            glucoseUnit = glucoseUnit
                        )
                        if (dataArray.isNotEmpty()) {
                            secondUserGraphNightScoutData = dataArray[0] as List<nightScoutData>
                            secondUser = dataArray[1] as String
                            secondUserDiff = dataArray[2] as String
                        } else {
                            secondUserGraphNightScoutData = defaultGraphNightScoutData
                            secondUser = "-"
                            secondUserDiff = "0"
                        }
                    }

                    DeviceType.Dexcom -> {
                        val dataArray = getDexcomData(
                            dexcomId = settings.secondUserSetting.dexcomId,
                            dexcomPassword = settings.secondUserSetting.dexcomPassword,
                            latestGlucoseProps,
                            glucoseUnit = glucoseUnit
                        )
                        if (dataArray.isNotEmpty()) {
                            secondUserGraphDexcomData = dataArray[0] as List<GlucoseEntry>
                            secondUser = dataArray[1] as String
                            secondUserDiff = dataArray[2] as String
                        } else {
                            secondUserGraphDexcomData = defaultGraphDexcomData
                            secondUser = "-"
                            secondUserDiff = "0"
                        }
                    }
                }
            }
        }
    }

    private suspend fun getNightScoutData(url: String, glucoseUnit: GlucoseUnits): List<Any> {
        val dataArray = mutableListOf<Any>()
        val formattedUrl = if (url.isNotEmpty() && url.last() == '/') url else "$url/"
        if (formattedUrl != "/") {
            val getDataJob = CoroutineScope(Dispatchers.IO).launch {
                val recentThreeHourGlucoseData =
                    NightScout().getBGDataByUrl(formattedUrl).getOrNull()
                        ?: return@launch
                if (recentThreeHourGlucoseData.size < 2) return@launch
                dataArray.add(recentThreeHourGlucoseData)
                val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                val secondGlucoseData = recentThreeHourGlucoseData[1].sgv
                when (glucoseUnit) {
                    GlucoseUnits.mg_dL -> {
                        dataArray.add(recentGlucoseData.toString())
                        dataArray.add((recentGlucoseData - secondGlucoseData).toString())
                    }

                    GlucoseUnits.mmol_L -> {
                        val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                        val rounded = round(recentGlucoseDataMmol * 10) / 10
                        val secondGlucoseDataMmol = mgdlToMmol(secondGlucoseData)
                        val secondRounded = round(secondGlucoseDataMmol * 10) / 10
                        dataArray.add(rounded.toString())
                        dataArray.add((round((rounded - secondRounded) * 10) / 10).toString())
                    }
                }
            }
            getDataJob.join()
        }
        return dataArray
    }

    private suspend fun getDexcomData(
        dexcomId: String,
        dexcomPassword: String,
        latestGlucoseProps: LatestGlucoseProps,
        glucoseUnit: GlucoseUnits
    ): List<Any> {
        val dataArray = mutableListOf<Any>()

        val firstUserConfigurationProps = ConfigurationProps(
            dexcomId,
            dexcomPassword,
            DexcomServer.EU
        )
        val firstUserDexcomClient =
            DexcomClient(firstUserConfigurationProps)
        if (dexcomId != "" && dexcomPassword != "") {
            val getDataJob = CoroutineScope(Dispatchers.IO).launch {
                val glucoseData =
                    firstUserDexcomClient.getEstimatedGlucoseValues(latestGlucoseProps)
                        ?: return@launch
                if (glucoseData.size < 2) return@launch

                dataArray.add(glucoseData)
                val currentGlucoseData: GlucoseEntry = glucoseData[0]
                val secondGlucoseData: GlucoseEntry = glucoseData[1]
                when (glucoseUnit) {
                    GlucoseUnits.mg_dL -> {
                        dataArray.add(currentGlucoseData.mgdl.toString())
                        dataArray.add((currentGlucoseData.mgdl - secondGlucoseData.mgdl).toString())
                    }

                    GlucoseUnits.mmol_L -> {
                        val rounded = round(currentGlucoseData.mmol * 10) / 10
                        val secondRounded = round(secondGlucoseData.mmol * 10) / 10
                        dataArray.add(rounded.toString())
                        dataArray.add((round((rounded - secondRounded) * 10) / 10).toString())
                    }
                }
            }
            getDataJob.join()
        }
        return dataArray
    }


    private fun mgdlToMmol(mgdl: Int): Double {
        return (mgdl / 18.0).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}
