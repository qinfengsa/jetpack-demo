package com.example.jetpack.demo.data.repositories

import com.example.jetpack.demo.data.api.DemoApi

class DemoRepository(private val demoApi: DemoApi) {

    suspend fun getImagePage(page: Int) = demoApi.getImagePage(page)
}
