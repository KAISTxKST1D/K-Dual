package com.kaist.k_dual.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Font(R.font.notosanscjkkr_vf, FontWeight.Normal),
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

@Composable
fun TypographyPreview() {
    Column {
        Text("Headline Small", style = EnglishTypography.headlineSmall)
        Text("Title Large", style = EnglishTypography.titleLarge)
        Text("Title Small", style = EnglishTypography.titleSmall)
        Text("Body Large", style = EnglishTypography.bodyLarge)
        Text("Body Medium", style = EnglishTypography.bodyMedium)
        Text("Label Large", style = EnglishTypography.labelLarge)

        Spacer(Modifier.height(16.dp))

        Text("머리글 작게", style = KoreanTypography.headlineSmall)
        Text("제목 크게", style = KoreanTypography.titleLarge)
        Text("제목 작게", style = KoreanTypography.titleSmall)
        Text("본문 크게", style = KoreanTypography.bodyLarge)
        Text("본문 중간", style = KoreanTypography.bodyMedium)
        Text("레이블 크게", style = KoreanTypography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTypography() {
    TypographyPreview()
}
