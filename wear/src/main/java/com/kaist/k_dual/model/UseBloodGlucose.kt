package com.kaist.k_dual.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.kaist.k_canvas.DeviceType
import com.kaist.k_canvas.GlucoseUnits
import com.kaist.k_canvas.PREFERENCES_FILE_KEY
import com.kaist.k_canvas.SETTINGS_KEY
import com.kaist.k_canvas.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.math.round

val defaultGraphNightScoutData: List<NightScoutData> =
    listOf(NightScoutData("", "", 0, "", 0, 0.0f, "", "", "", "", 0, 0, "", 0))
val defaultGraphDexcomData: List<GlucoseEntry> = listOf(GlucoseEntry(0, 0.0, Trend.FLAT, 0))

val dummyGraphDexcomData: List<GlucoseEntry> = listOf(
    GlucoseEntry(81, 4.5, Trend.FLAT, 1701343513311L),
    GlucoseEntry(79, 4.4, Trend.SINGLEDOWN, 1701343813311L),
    GlucoseEntry(180, 10.0, Trend.FLAT, 1701344113311L),
    GlucoseEntry(125, 6.9, Trend.SINGLEUP, 1701344413311L),
    GlucoseEntry(147, 8.2, Trend.SINGLEDOWN, 1701344713311L),
    GlucoseEntry(102, 5.7, Trend.FLAT, 1701345013311L),
    GlucoseEntry(166, 9.2, Trend.SINGLEUP, 1701345313311L),
    GlucoseEntry(163, 9.1, Trend.SINGLEDOWN, 1701345613311L),
    GlucoseEntry(103, 5.7, Trend.SINGLEDOWN, 1701345913311L),
    GlucoseEntry(89, 4.9, Trend.SINGLEUP, 1701346213311L),
    GlucoseEntry(88, 4.9, Trend.FLAT, 1701346513311L),
    GlucoseEntry(75, 4.2, Trend.FLAT, 1701346813311L),
    GlucoseEntry(161, 8.9, Trend.SINGLEDOWN, 1701347113311L),
    GlucoseEntry(109, 6.1, Trend.SINGLEDOWN, 1701347413311L),
    GlucoseEntry(130, 7.2, Trend.FLAT, 1701347713311L),
    GlucoseEntry(173, 9.6, Trend.FLAT, 1701348013311L),
    GlucoseEntry(139, 7.7, Trend.SINGLEDOWN, 1701348313311L),
    GlucoseEntry(80, 4.4, Trend.FLAT, 1701348613311L),
    GlucoseEntry(157, 8.7, Trend.FLAT, 1701348913311L),
    GlucoseEntry(120, 6.7, Trend.SINGLEDOWN, 1701349213311L),
    GlucoseEntry(153, 8.5, Trend.FLAT, 1701349513311L),
    GlucoseEntry(93, 5.2, Trend.SINGLEUP, 1701349813311L),
    GlucoseEntry(129, 7.2, Trend.FLAT, 1701350113311L),
    GlucoseEntry(163, 9.1, Trend.SINGLEDOWN, 1701350413311L),
    GlucoseEntry(155, 8.6, Trend.FLAT, 1701350713311L),
    GlucoseEntry(147, 8.2, Trend.SINGLEDOWN, 1701351013311L),
    GlucoseEntry(125, 6.9, Trend.SINGLEDOWN, 1701351313311L),
    GlucoseEntry(159, 8.8, Trend.FLAT, 1701351613311L),
    GlucoseEntry(133, 7.4, Trend.FLAT, 1701351913311L),
    GlucoseEntry(129, 7.2, Trend.FLAT, 1701352213311L),
    GlucoseEntry(113, 6.3, Trend.FLAT, 1701352513311L),
    GlucoseEntry(167, 9.3, Trend.SINGLEUP, 1701352813311L),
    GlucoseEntry(148, 8.2, Trend.FLAT, 1701353113311L),
    GlucoseEntry(150, 8.3, Trend.FLAT, 1701353413311L),
    GlucoseEntry(108, 6.0, Trend.SINGLEUP, 1701353713311L),
    GlucoseEntry(176, 9.8, Trend.SINGLEUP, 1701354013311L)
)


object UseBloodGlucose {
    private var _firstUser = mutableStateOf("-")
    var firstUser: String by _firstUser

    private var _secondUser = mutableStateOf("-")
    var secondUser: String by _secondUser

    private var _firstUserDiff = mutableStateOf("0")
    var firstUserDiff: String by _firstUserDiff

    private var _secondUserDiff = mutableStateOf("0")
    var secondUserDiff: String by _secondUserDiff

    private var _firstUserGraphNightScoutData = mutableStateOf(defaultGraphNightScoutData)
    var firstUserGraphNightScoutData: List<NightScoutData> by _firstUserGraphNightScoutData

    private var _secondUserGraphNightScoutData = mutableStateOf(defaultGraphNightScoutData)
    var secondUserGraphNightScoutData: List<NightScoutData> by _secondUserGraphNightScoutData

    private var _firstUserGraphDexcomData = mutableStateOf(defaultGraphDexcomData)
    var firstUserGraphDexcomData: List<GlucoseEntry> by _firstUserGraphDexcomData

    private var _secondUserGraphDexcomData = mutableStateOf(defaultGraphDexcomData)
    var secondUserGraphDexcomData: List<GlucoseEntry> by _secondUserGraphDexcomData

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
                        firstUserGraphNightScoutData = dataArray[0] as List<NightScoutData>
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
                            secondUserGraphNightScoutData = dataArray[0] as List<NightScoutData>
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
