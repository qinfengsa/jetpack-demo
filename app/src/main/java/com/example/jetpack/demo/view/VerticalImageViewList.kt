package com.example.jetpack.demo.ui.image

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.request.ImageRequest
import com.example.jetpack.demo.data.DemoConstant
import com.skydoves.landscapist.coil.CoilImage


@Composable
fun ImagePreview() {
    VerticalImageBrowserItem("1.jpg", 0)
}

@Composable
fun VerticalImageViewList(paths: LazyPagingItems<String>, contentPadding: PaddingValues) {
    LazyColumn {
        itemsIndexed(paths) { index, value ->
            value?.let {
                VerticalImageBrowserItem(it, index)
            }
        }
    }
}


@Composable
fun VerticalImageBrowserItem(path: String, page: Int = 0) {
    /**
     * 缩放比例
     */
    var scale by remember { mutableStateOf(1f) }

    /**
     * 偏移量
     */
    var offset by remember { mutableStateOf(Offset.Zero) }

    /**
     * 监听手势状态变换
     */
    val state =
        rememberTransformableState(onTransformation = { zoomChange, panChange, rotationChange ->
            scale = (zoomChange * scale).coerceAtLeast(1f)
            scale = if (scale > 5f) {
                5f
            } else {
                scale
            }
        })
    val url = "${DemoConstant.BASE_URL}/image/$path"

    CoilImage(
        imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillWidth,
        loading = {
            Text(
                text = "${page + 1}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        },
        failure = {
            Text(
                text = "网络错误",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        },
        modifier = Modifier
            .transformable(state = state)
            .graphicsLayer {  //布局缩放、旋转、移动变换
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y

                /*val pageOffset = pagerScope.calculateCurrentOffsetForPage(page = page).absoluteValue
                if (pageOffset == 1.0f) {
                    scale = 1.0f
                }*/
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale <= 1f) 2f else 1f
                        offset = Offset.Zero
                    },
                    onTap = {

                    }
                )
            }
    )
}
