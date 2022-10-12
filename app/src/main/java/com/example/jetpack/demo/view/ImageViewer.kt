package com.example.jetpack.demo.view

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.request.ImageRequest
import com.example.jetpack.demo.data.DemoConstant
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun ImageViewPreview() {
    // // 图片
    //    implementation 'com.github.skydoves:landscapist-coil:1.6.1'
    val viewModel: ImageViewModel = viewModel(
        factory = ImageViewModelFactory(LocalContext.current)
    )
    val page by rememberSaveable { mutableStateOf(1) }
    val paths = viewModel.getPages().collectAsLazyPagingItems()

    var showSetting by remember { mutableStateOf(false) }
    var sliderState by remember { mutableStateOf(1.toFloat()) }
    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 30)),
                modifier = Modifier.size(30.dp),
                onClick = { showSetting = !showSetting }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "选项"
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {

        HorizontalImageViewList(paths, it)
        BoxWithConstraints {
            ReaderMenu()
        }

    }
}


@Composable
fun SettingDialog(onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text(text = "setting", style = MaterialTheme.typography.h6) },
        text = {
            Column {
                Text("setting", modifier = Modifier.padding(bottom = 8.dp))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "OK")
            }
        },
        onDismissRequest = onDismiss
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageViewList(paths: LazyPagingItems<String>, state: PagerState, contentPadding: PaddingValues) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        HorizontalPager(
            count = paths.itemCount,
            state = state,
            reverseLayout = true,
        ) { page ->
            paths[page]?.let { ImageBrowserItem(it, page, this) }
        }
        ActionsRow(state)
    }
    /*Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier.height(10.dp).fillMaxWidth()
    ) {

    }*/
}


@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun ActionsRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    infiniteLoop: Boolean = false
) {
    Row(modifier) {
        val scope = rememberCoroutineScope()

        IconButton(
            enabled = infiniteLoop.not() && pagerState.currentPage > 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        ) {
            Icon(Icons.Default.FirstPage, null)
        }

        IconButton(
            enabled = infiniteLoop || pagerState.currentPage > 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        ) {
            Icon(Icons.Default.NavigateBefore, null)
        }

        TextField(value = "${pagerState.currentPage}", onValueChange = {

        })

        IconButton(
            enabled = infiniteLoop || pagerState.currentPage < pagerState.pageCount - 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        ) {
            Icon(Icons.Default.NavigateNext, null)
        }

        IconButton(
            enabled = infiniteLoop.not() && pagerState.currentPage < pagerState.pageCount - 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.pageCount - 1)
                }
            }
        ) {
            Icon(Icons.Default.LastPage, null)
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalImageViewList(paths: LazyPagingItems<String>, contentPadding: PaddingValues) {

    HorizontalPager(
        count = paths.itemCount,
        reverseLayout = true,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) { page ->
        paths[page]?.let { ImageBrowserItem(it, page, this) }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageBrowserItem(path: String, page: Int = 0, pagerScope: PagerScope) {
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
    Surface(
        color = Color.Gray,
        modifier = Modifier.fillMaxSize()
    ) {
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
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
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
                .fillMaxSize()
                .transformable(state = state)
                .graphicsLayer {  //布局缩放、旋转、移动变换
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y

                    val pageOffset = pagerScope.calculateCurrentOffsetForPage(page = page).absoluteValue
                    if (pageOffset == 1.0f) {
                        scale = 1.0f
                    }
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
                })
    }
}
