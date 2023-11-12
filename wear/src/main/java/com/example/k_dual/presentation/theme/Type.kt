package com.example.k_dual.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Typography
import com.example.k_dual.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val robotoFamily = FontFamily(
    Font(googleFont = GoogleFont("Roboto"), fontProvider = provider)
)

// Set of Material typography styles to start with
val Typography = Typography(
    title1 = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight(500),
        fontSize = 14.sp
    ),
    title2 = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight(500),
        fontSize = 12.sp,
        color = Color(0xFFBDC1C6)
    )

)