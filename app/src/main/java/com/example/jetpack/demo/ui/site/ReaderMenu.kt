package com.example.jetpack.demo.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt


@Composable
fun ReaderMenu() {
    val currentPage by remember { mutableStateOf(1) }
    ReaderMenuToolbar({})
    ReaderModeSetting()
    ReaderProgressSlider(
        currentPage = currentPage, pageCount = 20, onNewPageClicked = {},
    )
    NavigateChapters()
}


@Composable
private fun ReaderMenuToolbar(onCloseSideMenuClicked: () -> Unit) {
    Surface(elevation = 2.dp) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onCloseSideMenuClicked) {
                Icon(Icons.Rounded.ChevronLeft, null)
            }
        }
    }
}


@Composable
fun ReaderModeSetting() {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("阅读模式", Modifier.weight(0.25f), maxLines = 2, fontSize = 14.sp)
        Spacer(Modifier.width(8.dp))
        /*Spinner(
            modifier = Modifier.weight(0.75f),
            items = displayModes,
            selectedItemIndex = selectedModeIndex
        ) {
            onSetReaderMode(modes[it])
        }*/
    }
}


@Composable
fun ReaderProgressSlider(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int,
    onNewPageClicked: (Int) -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = currentPage.toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    var isValueChanging by remember { mutableStateOf(false) }
    Slider(
        animatedProgress,
        onValueChange = {
            if (!isValueChanging) {
                isValueChanging = true
                onNewPageClicked(it.roundToInt())
            }
        },
        valueRange = 0F..pageCount.toFloat(),
        onValueChangeFinished = { isValueChanging = false },

        )
}

@Composable
private fun NavigateChapters() {
    Divider(Modifier.padding(horizontal = 4.dp, vertical = 8.dp))
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        OutlinedButton({ }, Modifier.weight(0.5F)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("上一章", fontSize = 10.sp)
            }
        }
        OutlinedButton({ }, Modifier.weight(0.5F)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("下一章", fontSize = 10.sp)
                Icon(Icons.Rounded.NavigateNext, "下一章")
            }
        }
    }
}
