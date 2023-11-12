package com.example.k_dual.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.k_dual.R
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun BackButtonTitleRow(modifier: Modifier = Modifier, navController: NavController, title: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
            contentDescription = "Back arrow icon",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(100))
                .clickable {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                }
                .padding(6.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF454545)
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun BackButtonTitleRowPreview() {
    val navController = rememberNavController()
    KDualTheme {
        BackButtonTitleRow(modifier = Modifier, navController = navController, title = "Title")
    }
}