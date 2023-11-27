package com.kaist.k_dual.presentation

import android.content.Context
import android.util.Log
import com.kaist.k_canvas.DeviceType
import com.kaist.k_canvas.GlucoseUnits
import com.kaist.k_canvas.PREFERENCES_FILE_KEY
import com.kaist.k_dual.model.ConfigurationProps
import com.kaist.k_dual.model.DexcomClient
import com.kaist.k_dual.model.DexcomServer
import com.kaist.k_dual.model.GlucoseEntry
import com.kaist.k_dual.model.LatestGlucoseProps
import com.kaist.k_dual.model.NightScout
import com.kaist.k_dual.model.Trend
import com.kaist.k_dual.model.nightScoutData
import com.kaist.k_dual.presentation.UseSetting.settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.math.round

object UseBloodGlucose {
    var firstUser: String = "-"
    var secondUser: String = "-"
    var firstUserDiff: String = "0"
    var secondUserDiff: String = "0"
    var firstUserGraphNightScoutData: List<nightScoutData> =
        listOf(nightScoutData("", "", 0, "", 0, 0.0f, "", "", "", "", 0, 0, "", 0))
    var secondUserGraphNightScoutData: List<nightScoutData> =
        listOf(nightScoutData("", "", 0, "", 0, 0.0f, "", "", "", "", 0, 0, "", 0))
    var firstUserGraphDexcomData: List<GlucoseEntry> = listOf(GlucoseEntry(0, 0.0, Trend.FLAT, 0))
    var secondUserGraphDexcomData: List<GlucoseEntry> = listOf(GlucoseEntry(0, 0.0, Trend.FLAT, 0))

    fun updateBloodGlucose(context: Context) {
        val latestGlucoseProps = LatestGlucoseProps(180, 36)

        val sharedPref = context.getSharedPreferences(
            PREFERENCES_FILE_KEY,
            Context.MODE_PRIVATE
        )

        val settings = UseSetting.settings ?: return

        val glucoseUnit = settings.glucoseUnits
        Log.d("type", "first: ${settings.firstUserSetting.deviceType}, second: ${settings.secondUserSetting.deviceType}")
        when (settings.firstUserSetting.deviceType) {
            DeviceType.Nightscout -> {
                getNightScoutData(isFirst = true, glucoseUnit = glucoseUnit)
            }

            DeviceType.Dexcom -> {
                getDexcomData(
                    isFirst = true,
                    latestGlucoseProps = latestGlucoseProps,
                    glucoseUnit = glucoseUnit
                )
            }
        }
        if (settings.enableDualMode) {
            when (settings.secondUserSetting.deviceType) {
                DeviceType.Nightscout -> {
                    getNightScoutData(isFirst = false, glucoseUnit = glucoseUnit)
                }

                DeviceType.Dexcom -> {
                    getDexcomData(isFirst = false, latestGlucoseProps, glucoseUnit = glucoseUnit)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getNightScoutData(isFirst: Boolean, glucoseUnit: GlucoseUnits) {
        if (isFirst) {
            var firstUserNightScoutUrl = settings!!.firstUserSetting.nightscoutUrl + "/"
            if (firstUserNightScoutUrl != "/") {
                GlobalScope.launch {
                    val recentThreeHourGlucoseData =
                        NightScout().getBGDataByUrl(firstUserNightScoutUrl).getOrNull()
                            ?: return@launch
                    if (recentThreeHourGlucoseData.size < 2) return@launch
                    firstUserGraphNightScoutData = recentThreeHourGlucoseData
                    val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                    val secondGlucoseData = recentThreeHourGlucoseData[1].sgv
                    when (glucoseUnit) {
                        GlucoseUnits.mg_dL -> {
                            firstUser = recentGlucoseData.toString()
                            firstUserDiff =
                                (recentGlucoseData - secondGlucoseData).toString()
                        }

                        GlucoseUnits.mmol_L -> {
                            val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                            val rounded = round(recentGlucoseDataMmol * 10) / 10
                            val secondGlucoseDataMmol = mgdlToMmol(secondGlucoseData)
                            val secondRounded = round(secondGlucoseDataMmol * 10) / 10
                            firstUser = rounded.toString()
                            firstUserDiff =
                                (round((rounded - secondRounded) * 10) / 10).toString()
                        }
                    }
                }
            }
        } else {
            var secondUserNightScoutUrl = settings!!.secondUserSetting.nightscoutUrl + "/"
            if (secondUserNightScoutUrl != "/") {
                CoroutineScope(Dispatchers.IO).launch {
                    val recentThreeHourGlucoseData =
                        NightScout().getBGDataByUrl(secondUserNightScoutUrl).getOrNull()
                            ?: return@launch
                    secondUserGraphNightScoutData = recentThreeHourGlucoseData
                    val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                    val secondGlucoseData = recentThreeHourGlucoseData[1].sgv
                    when (glucoseUnit) {
                        GlucoseUnits.mg_dL -> {
                            secondUser = recentGlucoseData.toString()
                            secondUserDiff =
                                (recentGlucoseData - secondGlucoseData).toString()
                        }

                        GlucoseUnits.mmol_L -> {
                            val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                            val rounded = round(recentGlucoseDataMmol * 10) / 10
                            val secondGlucoseDataMmol = mgdlToMmol(secondGlucoseData)
                            val secondRounded = round(secondGlucoseDataMmol * 10) / 10
                            secondUser = rounded.toString()
                            secondUserDiff =
                                (round((rounded - secondRounded) * 10) / 10).toString()
                        }
                    }
                }
            }
        }
    }

    private fun getDexcomData(
        isFirst: Boolean,
        latestGlucoseProps: LatestGlucoseProps,
        glucoseUnit: GlucoseUnits
    ) {
        val settings = UseSetting.settings ?: return

        if (isFirst) {
            val firstUserDexcomId = settings.firstUserSetting.dexcomId
            val firstUserDexcomPassword = settings.firstUserSetting.dexcomPassword
            val firstUserConfigurationProps = ConfigurationProps(
                firstUserDexcomId,
                firstUserDexcomPassword,
                DexcomServer.EU
            )
            val firstUserDexcomClient =
                DexcomClient(firstUserConfigurationProps)
            if (firstUserDexcomId != "" && firstUserDexcomPassword != "") {
                CoroutineScope(Dispatchers.IO).launch {
                    val glucoseData =
                        firstUserDexcomClient.getEstimatedGlucoseValues(latestGlucoseProps)
                            ?: return@launch
                    firstUserGraphDexcomData = glucoseData
                    try {
                        val currentGlucoseData: GlucoseEntry = glucoseData[0]
                        val secondGlucoseData: GlucoseEntry = glucoseData[1]
                        when (glucoseUnit) {
                            GlucoseUnits.mg_dL -> {
                                firstUser = currentGlucoseData.mgdl.toString()
                                firstUserDiff =
                                    (currentGlucoseData.mgdl - secondGlucoseData.mgdl).toString()
                            }

                            GlucoseUnits.mmol_L -> {
                                val rounded = round(currentGlucoseData.mmol * 10) / 10
                                val secondRounded = round(secondGlucoseData.mmol * 10) / 10
                                firstUser = rounded.toString()
                                firstUserDiff =
                                    (round((rounded - secondRounded) * 10) / 10).toString()
                            }
                        }
                    } catch (e: Exception) {
                        secondUser = "-"
                        secondUserDiff = "0"
                    }
                }
            }
        } else {
            val secondUserDexcomId = settings.secondUserSetting.dexcomId
            val secondUserDexcomPassword = settings.secondUserSetting.dexcomPassword
            val secondUserConfigurationProps = ConfigurationProps(
                secondUserDexcomId,
                secondUserDexcomPassword,
                DexcomServer.EU
            )
            val secondUserDexcomClient: DexcomClient =
                DexcomClient(secondUserConfigurationProps)
            if (secondUserDexcomId != "" && secondUserDexcomPassword != "") {
                CoroutineScope(Dispatchers.IO).launch {
                    val glucoseData = secondUserDexcomClient.getEstimatedGlucoseValues(
                        latestGlucoseProps
                    ) ?: return@launch
                    secondUserGraphDexcomData = glucoseData
                    try {
                        val currentGlucoseData: GlucoseEntry = glucoseData[0]
                        val secondGlucoseData: GlucoseEntry = glucoseData[1]

                        when (glucoseUnit) {
                            GlucoseUnits.mg_dL -> {
                                secondUser = currentGlucoseData.mgdl.toString()
                                secondUserDiff =
                                    (currentGlucoseData.mgdl - secondGlucoseData.mgdl).toString()
                            }

                            GlucoseUnits.mmol_L -> {
                                val rounded = round(currentGlucoseData.mmol * 10) / 10
                                val secondRounded = round(secondGlucoseData.mmol * 10) / 10
                                secondUser = rounded.toString()
                                secondUserDiff =
                                    (round((rounded - secondRounded) * 10) / 10).toString()
                            }
                        }
                    } catch (e: Exception) {
                        secondUser = "-"
                        secondUserDiff = "0"
                    }
                }
            }
        }
    }


    private fun mgdlToMmol(mgdl: Int): Double {
        return (mgdl / 18.0).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}
