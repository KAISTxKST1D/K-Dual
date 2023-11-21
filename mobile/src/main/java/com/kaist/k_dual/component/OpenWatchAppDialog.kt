package com.kaist.k_dual.component

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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kaist.k_dual.R
import com.kaist.k_dual.ui.theme.KDualTheme
import com.kaist.k_dual.ui.theme.RedUISolid
import kotlinx.coroutines.delay

@Composable
fun OpenWatchAppDialog(
    isOpen: Boolean,
    isConnected: Boolean,
    onDismiss: () -> Unit,
) {
    val customColorScheme = lightColorScheme(
        surface = Color(0xFFFCECEC)
    )

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.check_lottie))
    val progress by animateLottieCompositionAsState(
        composition, isPlaying = !isOpen,
        iterations = 1
    )
    var animationFinished by remember { mutableStateOf(false) }

    LaunchedEffect(isOpen, isConnected) {
        if (isConnected && !isOpen) {
            while (progress < 1f) {
                delay(16L)
            }
            animationFinished = true
        }
    }

    if (isOpen || (isConnected && !animationFinished)) {
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
                                imageVector = ImageVector.vectorResource(R.drawable.close_24px),
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
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.watch_24px),
                                contentDescription = "Back arrow icon",
                            )
                        }
                        Text(
                            text = "Connect your Galaxy Watch.",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Ensure your Galaxy Watch is connected to your phone to set up the K-Dual app.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .padding(top = 16.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
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
                            if (isOpen) {
                                CircularProgressIndicator(
                                    color = RedUISolid,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                LottieAnimation(
                                    composition = composition,
                                    progress = { progress },
                                    modifier = Modifier.size(24.dp)
                                )
                            }
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
            isConnected = false,
            onDismiss = { /* Preview doesn't handle interactions */ },
        )
    }
}

