package com.kaist.k_dual.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.toColorInt
import com.kaist.k_dual.ui.theme.Signature

@Composable
fun Toggle(
    modifier: Modifier = Modifier,
    state: ToggleState,
    size: Dp = 24.dp,
    offStateColor: Color = Color("#E6E0E9".toColorInt()),
    onStateColor: Color = Signature,
    borderWidth: Dp = 1.dp,
    thumbOnColor: Color = Color.White,
    thumbOffColor: Color = Color("#79747E".toColorInt()),
    onChange: (ToggleState) -> Unit,
    animSpec: AnimationSpec<Float> = tween(durationMillis = 250)
) {

    val animValue = animateFloatAsState(
        targetValue = if (state is ToggleState.Left) 0f else 0.44f,
        animationSpec = animSpec, label = ""
    )

    val animThumbColor = animateColorAsState(
        targetValue = if (state is ToggleState.Left) thumbOffColor else thumbOnColor,
        animationSpec = tween(durationMillis = 250), label = ""
    )

    val animBcColor = animateColorAsState(
        targetValue = if (state is ToggleState.Left) offStateColor else onStateColor,
        animationSpec = tween(durationMillis = 250), label = ""
    )

    ConstraintLayout(
        modifier = modifier
            .wrapContentSize()
            .size(
                width = size.times(1.75F), height = size
            )
            .border(
                color = animThumbColor.value,
                width = borderWidth,
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .background(animBcColor.value)
            .clickable {
                if (state is ToggleState.Left) onChange(ToggleState.Right)
                else onChange(ToggleState.Left)
            }
    ) {
        val startGuideLine = createGuidelineFromStart(animValue.value)
        val thumb = createRef()

        Box(
            modifier = Modifier
                .size(size)
                .padding(size.times(0.2F))
                .clip(RoundedCornerShape(50))
                .background(animThumbColor.value)
                .constrainAs(thumb) {
                    start.linkTo(startGuideLine)
                }
        )
    }
}

sealed class ToggleState {
    operator fun not(): ToggleState {
        if (this == Left) {
            return Right
        }
        return Left
    }

    object Left : ToggleState()
    object Right : ToggleState()
}

@Preview
@Composable
fun PreviewSwitch() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val state = remember { mutableStateOf<ToggleState>(ToggleState.Right) }

        Toggle(
            modifier = Modifier,
            state = state.value,
            size = 50.dp,
            onChange = {
                state.value = it
            }
        )
    }
}