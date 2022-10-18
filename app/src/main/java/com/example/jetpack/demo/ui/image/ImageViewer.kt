package com.example.jetpack.demo.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.paging.compose.itemsIndexed
import coil.request.ImageRequest
import com.example.jetpack.demo.data.DemoConstant
import com.example.jetpack.demo.ui.image.VerticalImageBrowserItem
import com.google.accompanist.pager.*
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun ImageViewPreview() {
    val viewModel: ImageViewModel = viewModel(
        factory = ImageViewModelFactory(LocalContext.current)
    )
    val page by rememberSaveable { mutableStateOf(1) }
    var total by rememberSaveable { mutableStateOf(0) }
    val paths = viewModel.getPages {
        total = it
    }.collectAsLazyPagingItems()


    var sliderState by remember { mutableStateOf(1.toFloat()) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalImageViewList(paths, total, it)
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalImageViewList(
    paths: LazyPagingItems<String>,
    total: Int,
    contentPadding: PaddingValues
) {
    var currentPage by remember { mutableStateOf(0) }
    val pageState = rememberPagerState(initialPage = 0)
    val columnState = rememberLazyListState(currentPage, 0)
    val scope = rememberCoroutineScope()
    var showSetting by remember { mutableStateOf(false) }
    var horizontal by remember { mutableStateOf(true) }

    LaunchedEffect(columnState) {
        snapshotFlow { columnState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .collect {
                currentPage = it
            }
    }

    if (horizontal) {
        HorizontalPager(
            count = paths.itemCount,
            state = pageState,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showSetting = true
                        },
                        onTap = {

                        }
                    )
                }
                .padding(contentPadding)
        ) { page ->
            paths[page]?.let { ImageBrowserItem(it, page, this) }
        }
    } else {
        LazyColumn(
            state = columnState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showSetting = true
                        },
                        onTap = {

                        }
                    )
                }
                .padding(contentPadding)) {
            itemsIndexed(paths) { index, value ->
                value?.let {
                    VerticalImageBrowserItem(it, index)
                }
            }
        }
    }

    AnimatedVisibility(
        showSetting,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showSetting = false
                        },
                        onTap = {

                        }
                    )
                },
            color = Color.Gray.copy(alpha = 0.2f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "美人",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.8f)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (horizontal) {
                            Text(
                                text = "${currentPage + 1}/$total",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(5.dp)
                            )
                            ReaderProgressSlider(
                                currentPage = pageState.currentPage, pageCount = total, onNewPageClicked = {
                                    scope.launch {
                                        pageState.animateScrollToPage(it)
                                        currentPage = it
                                    }
                                }
                            )
                        } else {
                            Text(
                                text = "${currentPage + 1}/$total",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(5.dp)
                            )
                            ReaderProgressSlider(
                                currentPage = currentPage, pageCount = total, onNewPageClicked = {
                                    currentPage = it
                                    scope.launch {
                                        columnState.scrollToItem(it, scrollOffset = 0)
                                    }
                                }
                            )
                        }

                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.8f))
                            .padding(top = 5.dp, bottom = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ScreenRotation,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.CenterHorizontally)

                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "屏幕方向", color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    horizontal = !horizontal
                                }
                        ) {
                            Icon(
                                imageVector = if (horizontal) Icons.Default.SwapVert else Icons.Default.SwapHoriz,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "翻页方向", color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "设置", color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    showSetting = false
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "隐藏", color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

            }
        }
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
        )
    }
}
