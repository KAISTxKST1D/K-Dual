package com.kaist.k_dual.presentation

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
import com.kaist.k_dual.model.ConfigurationProps
import com.kaist.k_dual.model.DexcomClient
import com.kaist.k_dual.model.DexcomServer
import com.kaist.k_dual.model.GlucoseEntry
import com.kaist.k_dual.model.LatestGlucoseProps
import com.kaist.k_dual.model.NightScout
import com.kaist.k_dual.presentation.UseSetting.settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.math.round

object UseBloodGlucose {
    var firstUser: String = "-"
    var secondUser: String = "-"
    var firstUserDiff: String = "0"
    var secondUserDiff: String = "0"
    fun updateBloodGlucose(context: Context) {
        val latestGlucoseProps: LatestGlucoseProps = LatestGlucoseProps(180, 36)

        val sharedPref = context.getSharedPreferences(
            PREFERENCES_FILE_KEY,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val jsonString = sharedPref.getString(SETTINGS_KEY, null)
        settings = if (jsonString != null) {
            try {
                gson.fromJson(jsonString, Setting::class.java)
            } catch (e: JsonSyntaxException) {
                null
            } catch (e: JsonParseException) {
                null
            }
        } else {
            null
        }

        if (settings == null) return

        val glucoseUnit = settings!!.glucoseUnits
        if (settings!!.enableDualMode) {
            val firstUserDevice: DeviceType = settings!!.firstUserSetting.deviceType
            val secondUserDevice: DeviceType = settings!!.secondUserSetting.deviceType

            when (firstUserDevice) {
                DeviceType.Nightscout -> {
                    var firstUserNightScoutUrl = settings!!.firstUserSetting.nightscoutUrl
                    firstUserNightScoutUrl = firstUserNightScoutUrl.plus("/")
                    if (firstUserNightScoutUrl != "/") {
                        Thread {
                            val recentThreeHourGlucoseData =
                                NightScout().getBGDataByUrl(firstUserNightScoutUrl)
                            val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                            val secondGlucoseData = recentThreeHourGlucoseData[1].sgv
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> {
                                    firstUser = recentGlucoseData.toString()
                                    firstUserDiff=(recentGlucoseData-secondGlucoseData).toString()
                                }
                                GlucoseUnits.mmol_L -> {
                                    val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                                    val rounded = round(recentGlucoseDataMmol * 100) / 100
                                    val secondGlucoseDataMmol = mgdlToMmol(secondGlucoseData)
                                    val secondRounded = round(secondGlucoseDataMmol * 100) / 100
                                    firstUser = rounded.toString()
                                    firstUserDiff = (round((rounded - secondRounded)*10)).toString()
                                }
                            }
                        }.start()
                    }
                }

                DeviceType.Dexcom -> {
                    val firstUserDexcomId = settings!!.firstUserSetting.dexcomId
                    val firstUserDexcomPassword = settings!!.firstUserSetting.dexcomPassword
                    val firstUserConfigurationProps: ConfigurationProps = ConfigurationProps(
                        firstUserDexcomId,
                        firstUserDexcomPassword,
                        DexcomServer.EU
                    )
                    val firstUserDexcomClient: DexcomClient =
                        DexcomClient(firstUserConfigurationProps)

                    if (firstUserDexcomId != "" && firstUserDexcomPassword != "") {
                        CoroutineScope(Dispatchers.IO).launch {
                            var glucoseData =
                                firstUserDexcomClient.getEstimatedGlucoseValues(latestGlucoseProps)
                            val currentGlucoseData: GlucoseEntry = glucoseData[0]
                            val secondGlucoseData : GlucoseEntry = glucoseData[1]
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> {
                                    firstUser = currentGlucoseData.mgdl.toString()
                                    firstUserDiff = (currentGlucoseData.mgdl-secondGlucoseData.mgdl).toString()
                                }
                                GlucoseUnits.mmol_L -> {
                                    val rounded = round(currentGlucoseData.mmol * 100) / 100
                                    val secondRounded = round(secondGlucoseData.mmol * 100) / 100
                                    firstUser = rounded.toString()
                                    firstUserDiff = (round((rounded - secondRounded)*10)).toString()
                                }

                            }
                        }
                    }
                }
            }
            when (secondUserDevice) {
                DeviceType.Nightscout -> {
                    var secondUserNightScoutUrl = settings!!.secondUserSetting.nightscoutUrl
                    secondUserNightScoutUrl = secondUserNightScoutUrl.plus("/")
                    if (secondUserNightScoutUrl != "/") {
                        Thread {
                            val recentThreeHourGlucoseData =
                                NightScout().getBGDataByUrl(secondUserNightScoutUrl)
                            val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                            val secondGlucoseData = recentThreeHourGlucoseData[1].sgv
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> {
                                    secondUser = recentGlucoseData.toString()
                                    secondUserDiff=(recentGlucoseData-secondGlucoseData).toString()
                                }
                                GlucoseUnits.mmol_L -> {
                                    val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                                    val rounded = round(recentGlucoseDataMmol * 100) / 100
                                    val secondGlucoseDataMmol = mgdlToMmol(secondGlucoseData)
                                    val secondRounded = round(secondGlucoseDataMmol * 100) / 100
                                    secondUser = rounded.toString()
                                    secondUserDiff = (round((rounded - secondRounded)*10)).toString()
                                }
                            }
                        }.start()
                    }
                }

                DeviceType.Dexcom -> {
                    val secondUserDexcomId = settings!!.secondUserSetting.dexcomId
                    val secondUserDexcomPassword = settings!!.secondUserSetting.dexcomPassword
                    val secondUserConfigurationProps: ConfigurationProps = ConfigurationProps(
                        secondUserDexcomId,
                        secondUserDexcomPassword,
                        DexcomServer.EU
                    )
                    val secondUserDexcomClient: DexcomClient =
                        DexcomClient(secondUserConfigurationProps)
                    if (secondUserDexcomId != "" && secondUserDexcomPassword != "") {
                        CoroutineScope(Dispatchers.IO).launch {
                            var glucoseData = secondUserDexcomClient.getEstimatedGlucoseValues(
                                latestGlucoseProps
                            )
                            val currentGlucoseData: GlucoseEntry = glucoseData[0]
                            val secondGlucoseData : GlucoseEntry = glucoseData[1]
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> {
                                    secondUser = currentGlucoseData.mgdl.toString()
                                    secondUserDiff = (currentGlucoseData.mgdl-secondGlucoseData.mgdl).toString()
                                }

                                GlucoseUnits.mmol_L -> {
                                    val rounded = round(currentGlucoseData.mmol * 100) / 100
                                    val secondRounded = round(secondGlucoseData.mmol * 100) / 100
                                    secondUser = rounded.toString()
                                    secondUserDiff = (round((rounded - secondRounded)*10)).toString()
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val firstUserDevice: DeviceType = settings!!.firstUserSetting.deviceType

            when (firstUserDevice) {
                DeviceType.Nightscout -> {
                    var firstUserNightScoutUrl = settings!!.firstUserSetting.nightscoutUrl
                    firstUserNightScoutUrl = firstUserNightScoutUrl.plus("/")
                    if (firstUserNightScoutUrl != "/") {
                        Thread {
                            val recentThreeHourGlucoseData =
                                NightScout().getBGDataByUrl(firstUserNightScoutUrl)
                            val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                            val secondGlucoseData = recentThreeHourGlucoseData[1].sgv
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> {
                                    firstUser = recentGlucoseData.toString()
                                    firstUserDiff = (recentGlucoseData-secondGlucoseData).toString()
                                }
                                GlucoseUnits.mmol_L -> {
                                    val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                                    val secondGlucoseDataMmol = mgdlToMmol(secondGlucoseData)
                                    val rounded = round(recentGlucoseDataMmol * 100) / 100
                                    val secondRounded = round(secondGlucoseDataMmol * 100) / 100
                                    firstUser = rounded.toString()
                                    firstUserDiff = (round((rounded - secondRounded)*10)).toString()
                                }
                            }
                        }.start()
                    }
                }

                DeviceType.Dexcom -> {
                    val firstUserDexcomId = settings!!.firstUserSetting.dexcomId
                    val firstUserDexcomPassword = settings!!.firstUserSetting.dexcomPassword
                    val firstUserConfigurationProps: ConfigurationProps = ConfigurationProps(
                        firstUserDexcomId,
                        firstUserDexcomPassword,
                        DexcomServer.EU
                    )
                    val firstUserDexcomClient: DexcomClient =
                        DexcomClient(firstUserConfigurationProps)
                    Log.d("updateBloodGlucose", "$firstUserDexcomId, $firstUserDexcomPassword")
                    if (firstUserDexcomId != "" && firstUserDexcomPassword != "") {
                        CoroutineScope(Dispatchers.IO).launch {
                            var glucoseData =
                                firstUserDexcomClient.getEstimatedGlucoseValues(latestGlucoseProps)
                            val currentGlucoseData: GlucoseEntry = glucoseData[0]
                            val secondGlucoseData : GlucoseEntry = glucoseData[1]
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> {
                                    firstUser = currentGlucoseData.mgdl.toString()
                                    firstUserDiff = (currentGlucoseData.mgdl - secondGlucoseData.mgdl).toString()
                                }
                                GlucoseUnits.mmol_L -> {
                                    val rounded = round(currentGlucoseData.mmol * 100) / 100
                                    val secondRounded = round(secondGlucoseData.mmol * 100) / 100
                                    firstUser = rounded.toString()
                                    firstUserDiff = (round((rounded - secondRounded)*10)).toString()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun mgdlToMmol(mgdl: Int): Double {
        return (mgdl / 18.0).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}
