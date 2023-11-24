package com.kaist.k_dual.model

import com.kaist.k_canvas.KColor

enum class DeviceType(val label: String) {
    Nightscout("Nightscout"),
    Dexcom("Dexcom")
}

enum class GlucoseUnits(val label: String) {
    mg_dL("mg/dL"),
    mmol_L("mmol/L")
}

data class UserSetting(
    val name: String,
    val color: KColor,
    val vibrationEnabled: Boolean,
    val colorBlinkEnabled: Boolean,
    val lowValue: Int,
    val highValue: Int,
    val deviceType: DeviceType,
    val nightscoutUrl: String = "",
    val dexcomId: String = "",
    val dexcomPassword: String = ""
)

data class Setting(
    val enableDualMode: Boolean,
    val firstUserSetting: UserSetting,
    val secondUserSetting: UserSetting,
    val glucoseUnits: GlucoseUnits
)

val DefaultSetting = Setting(
    enableDualMode = true,
    firstUserSetting = UserSetting(
        name = "",
        color = KColor.YELLOW,
        colorBlinkEnabled = true,
        vibrationEnabled = true,
        highValue = 180,
        lowValue = 70,
        deviceType = DeviceType.Nightscout
    ),
    secondUserSetting = UserSetting(
        name = "",
        color = KColor.BLUE,
        colorBlinkEnabled = true,
        vibrationEnabled = true,
        highValue = 180,
        lowValue = 70,
        deviceType = DeviceType.Nightscout
    ),
    glucoseUnits = GlucoseUnits.mg_dL,
)
