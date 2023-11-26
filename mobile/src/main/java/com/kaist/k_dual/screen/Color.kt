package com.kaist.k_dual.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaist.k_canvas.KColor
import com.kaist.k_dual.R
import com.kaist.k_dual.component.BackButtonTitleRow
import com.kaist.k_dual.component.Divider
import com.kaist.k_dual.component.MultipleRowWhiteBox
import com.kaist.k_dual.component.WatchFacePreview
import com.kaist.k_dual.model.ManageSetting
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun ColorScreen(navController: NavController, isFirst: Boolean, onSendMessageFailed: () -> Unit) {
    val context = LocalContext.current
    val userSetting =
        if (isFirst) ManageSetting.settings.firstUserSetting else ManageSetting.settings.secondUserSetting

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButtonTitleRow(
            navController = navController,
            title = (if (isFirst) stringResource(R.string.first_user) else stringResource(R.string.second_user)) + stringResource(
                R.string.color2
            )
        )
        WatchFacePreview(
            isFirstHighLight = isFirst,
            isSecondHighLight = !isFirst
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.color),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(start = 24.dp)
            )
            MultipleRowWhiteBox {
                val onClick = { color: KColor ->
                    if (isFirst) {
                        ManageSetting.saveSettings(
                            settings = ManageSetting.settings.copy(
                                firstUserSetting = userSetting.copy(color = color)
                            ),
                            context = context,
                            onSendMessageFailed = onSendMessageFailed
                        )
                    } else {
                        ManageSetting.saveSettings(
                            settings = ManageSetting.settings.copy(
                                secondUserSetting = userSetting.copy(color = color)
                            ),
                            context = context,
                            onSendMessageFailed = onSendMessageFailed
                        )
                    }
                }
                KColor.values().mapIndexed { index, color ->
                    Row(
                        modifier = Modifier
                            .clickable { onClick(color) }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .size(24.dp),
                            selected = userSetting.color == color,
                            onClick = { onClick(color) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                            )
                        )
                        Text(
                            text = color.label,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    if (index != KColor.values().lastIndex) Divider()
                }
            }
        }

    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun ColorScreenPreview() {
    val navController = rememberNavController()
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { ColorScreen(navController = navController, isFirst = true, onSendMessageFailed = {}) }
    }
}