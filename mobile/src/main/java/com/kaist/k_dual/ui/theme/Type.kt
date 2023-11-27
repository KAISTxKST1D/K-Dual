package com.kaist.k_dual.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

val Typography = Typography(
    headlineSmall = TextStyle(
        fontSize = 21.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Normal,
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium,
    ),
)

@Composable
fun TypographyPreview() {
    Column {
        Text("Headline Small", style = Typography.headlineSmall)
        Text("Title Large", style = Typography.titleLarge)
        Text("Title Small", style = Typography.titleSmall)
        Text("Body Large", style = Typography.bodyLarge)
        Text("Body Medium", style = Typography.bodyMedium)
        Text("Label Large", style = Typography.labelLarge)
        Text("헤드라인 스몰", style = Typography.headlineSmall)
        Text("타이틀 라지", style = Typography.titleLarge)
        Text("타이틀 스몰", style = Typography.titleSmall)
        Text("바디 라지", style = Typography.bodyLarge)
        Text("바디 미디움", style = Typography.bodyMedium)
        Text("라벨 라지", style = Typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTypography() {
    TypographyPreview()
}
