package com.example.jetpack.demo.data.page

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetpack.demo.data.repositories.DemoRepository

class ImagePageSource(
    private val demoRepository: DemoRepository
) : PagingSource<Int, String>() {

    private val TAG = "--ComicPage"

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val currentPage = params.key ?: 1
        return try {
            val result = demoRepository.getImagePage(currentPage)
            if (result.code == 200) {
                val data = result.data
                val pages = data.pages
                LoadResult.Page(
                    data = data.images,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (currentPage == pages) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}
