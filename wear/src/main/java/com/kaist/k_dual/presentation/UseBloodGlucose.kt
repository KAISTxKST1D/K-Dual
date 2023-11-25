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
    fun updateBloodGlucose(context: Context) {
        Log.d("updateBloodGlucose", "inside function")
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
                    Log.d("updateBloodGlucose", "first user is nightscout")
                    var firstUserNightScoutUrl = settings!!.firstUserSetting.nightscoutUrl
                    firstUserNightScoutUrl = firstUserNightScoutUrl.plus("/")
                    Log.d("updateBloodGlucose", "$firstUserNightScoutUrl")
                    if (firstUserNightScoutUrl != "/") {
                        Thread {
                            val recentThreeHourGlucoseData =
                                NightScout().getBGDataByUrl(firstUserNightScoutUrl)
                            val recentGlucoseData = recentThreeHourGlucoseData[0].sgv
                            Log.d("nightscout", recentGlucoseData.toString())
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> firstUser = recentGlucoseData.toString()
                                GlucoseUnits.mmol_L -> {
                                    val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                                    val rounded = round(recentGlucoseDataMmol * 100) / 100
                                    firstUser = rounded.toString()
                                }
                            }
                        }.start()
                    }
                }

                DeviceType.Dexcom -> {
                    Log.d("updateBloodGlucose", "first user is dexcom")
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
                            val currentGlucoseData: GlucoseEntry = glucoseData.get(0)
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> firstUser = currentGlucoseData.mgdl.toString()
                                GlucoseUnits.mmol_L -> {
                                    val rounded = round(currentGlucoseData.mmol * 100) / 100
                                    firstUser = rounded.toString()
                                }

                            }
                        }
                    }
                }
            }
            when (secondUserDevice) {
                DeviceType.Nightscout -> {
                    Log.d("updateBloodGlucose", "second user is nightscout")
                    var secondUserNightScoutUrl = settings!!.secondUserSetting.nightscoutUrl
                    secondUserNightScoutUrl = secondUserNightScoutUrl.plus("/")
                    if (secondUserNightScoutUrl != "/") {
                        Thread {
                            val recentThreeHourGlucoseData =
                                NightScout().getBGDataByUrl(secondUserNightScoutUrl)
                            val recentGlucoseData = recentThreeHourGlucoseData.get(0).sgv
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> secondUser = recentGlucoseData.toString()
                                GlucoseUnits.mmol_L -> {
                                    val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                                    val rounded = round(recentGlucoseDataMmol * 100) / 100
                                    secondUser = rounded.toString()
                                }
                            }
                        }.start()
                    }
                }

                DeviceType.Dexcom -> {
                    Log.d("updateBloodGlucose", "second user is dexcom")
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
                            val currentGlucoseData: GlucoseEntry = glucoseData.get(0)
                            Log.d("updateBloodGlucose", "dexcom data is $currentGlucoseData")
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> secondUser =
                                    currentGlucoseData.mgdl.toString()

                                GlucoseUnits.mmol_L -> {
                                    val rounded = round(currentGlucoseData.mmol * 100) / 100
                                    secondUser = rounded.toString()
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
                    Log.d("updateBloodGlucose", "alone user is nightscout")
                    var firstUserNightScoutUrl = settings!!.firstUserSetting.nightscoutUrl
                    firstUserNightScoutUrl = firstUserNightScoutUrl.plus("/")
                    if (firstUserNightScoutUrl != "/") {
                        Thread {
                            val recentThreeHourGlucoseData =
                                NightScout().getBGDataByUrl(firstUserNightScoutUrl)
                            val recentGlucoseData = recentThreeHourGlucoseData.get(0).sgv
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> firstUser = recentGlucoseData.toString()
                                GlucoseUnits.mmol_L -> {
                                    val recentGlucoseDataMmol = mgdlToMmol(recentGlucoseData)
                                    val rounded = round(recentGlucoseDataMmol * 100) / 100
                                    firstUser = rounded.toString()
                                }
                            }
                        }.start()
                    }
                }

                DeviceType.Dexcom -> {
                    Log.d("updateBloodGlucose", "alone is dexcom")
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
                            val currentGlucoseData: GlucoseEntry = glucoseData.get(0)
                            Log.d("updateBloodGlucose", "dexcom data is $currentGlucoseData")
                            when (glucoseUnit) {
                                GlucoseUnits.mg_dL -> firstUser = currentGlucoseData.mgdl.toString()
                                GlucoseUnits.mmol_L -> {
                                    val rounded = round(currentGlucoseData.mmol * 100) / 100
                                    firstUser = rounded.toString()
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
