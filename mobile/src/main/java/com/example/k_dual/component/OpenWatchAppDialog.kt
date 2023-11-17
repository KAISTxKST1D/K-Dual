package com.example.k_dual.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import com.example.k_dual.R
import com.example.k_dual.ui.theme.KDualTheme
import com.example.k_dual.ui.theme.RedUISolid

@Composable
fun OpenWatchAppDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
) {
    val customColorScheme = lightColorScheme(
        surface = Color(0xFFFCECEC)
    )

    if (isOpen) {
        MaterialTheme(colorScheme = customColorScheme) {
            Dialog(
                onDismissRequest = { onDismiss() },
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = Color(0xFFFCECEC))
                            .padding(24.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Image(
                                // TODO. Change vector resource to close button
                                imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                                contentDescription = "Back arrow icon",
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(100))
                                    .clickable { onDismiss() }
                                    .padding(6.dp),
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.watch_24px),
                                contentDescription = "Back arrow icon",
                            )
                        }
                        Text(
                            // TODO. Change text
                            text = "Open the K-Dual app\non your Galaxy Watch",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            // TODO. Change text
                            text = "To set up the K-Dual app for the smart watch, the K-Dual app on your Galaxy Watch must be open at the same time. Please open this app on your Galaxy Watch as well.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .padding(top = 16.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "K",
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(100))
                                        .background(color = Color(0xFFFFDDDD))
                                        .width(40.dp)
                                        .height(40.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                                Text(
                                    text = "Connection Status",
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                            CircularProgressIndicator(
                                color = RedUISolid,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun OpenWatchAppDialogPreview() {
    KDualTheme {
        OpenWatchAppDialog(
            isOpen = true,
            onDismiss = { /* Preview doesn't handle interactions */ },
        )
    }
}

