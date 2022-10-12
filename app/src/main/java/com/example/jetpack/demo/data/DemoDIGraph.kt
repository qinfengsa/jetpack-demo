package com.example.jetpack.demo.data

import android.content.Context
import com.example.jetpack.demo.data.api.DemoApi
import com.example.jetpack.demo.data.repositories.DemoRepository

object DemoDIGraph {

    private val demoApi = DemoApi.invoke()

    fun createDemoRepository(context: Context): DemoRepository {
        return DemoRepository(
            demoApi = demoApi
        )
    }
}
