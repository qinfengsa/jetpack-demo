package com.example.jetpack.demo.view

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jetpack.demo.data.DemoDIGraph
import com.example.jetpack.demo.data.api.model.ImageData
import com.example.jetpack.demo.data.page.ImagePageSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class ImageViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImageViewModel(context) as T
    }
}

class ImageViewModel(context: Context) : ViewModel() {

    private val demoRepository = DemoDIGraph.createDemoRepository(context)

    fun getPages(page: Int): Flow<ImageData> = flow {
        val result = demoRepository.getImagePage(page)
        if (result.code == 200) {
            emit(result.data)
        }
    }.catch { e ->
        Log.e("error", "error:$e")
    }.flowOn(Dispatchers.IO)


    fun getPages(updatePageInfo: (Int) -> Unit = {}): Flow<PagingData<String>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ImagePageSource(demoRepository, updatePageInfo)
            }
        ).flow
    }
}
