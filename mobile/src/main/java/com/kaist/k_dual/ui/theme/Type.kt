package com.kaist.k_dual.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.kaist.k_dual.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val robotoFontName = GoogleFont("Roboto")

val robotoFontFamily = FontFamily(
    Font(googleFont = robotoFontName, fontProvider = provider)
)
val notoSansFontFamily =  FontFamily(
    Font(R.font.notosanskr_regular, FontWeight.Normal),
    Font(R.font.notosanskr_medium, FontWeight.Medium),
)

val EnglishTypography = Typography(
    headlineSmall = TextStyle(
        fontSize = 21.sp,
        lineHeight = 32.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
    ),
)

val KoreanTypography = Typography(
    headlineSmall = TextStyle(
        fontSize = 21.sp,
        lineHeight = 21.sp,
        fontFamily = notoSansFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 22.sp,
        fontFamily = notoSansFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        lineHeight = 16.sp,
        fontFamily = notoSansFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 16.sp,
        fontFamily = notoSansFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontFamily = notoSansFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontFamily = notoSansFontFamily,
        fontWeight = FontWeight.Medium,
    ),
)