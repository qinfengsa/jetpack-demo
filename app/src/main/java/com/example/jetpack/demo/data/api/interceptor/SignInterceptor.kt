/*
package com.example.jetpack.demo.data.api.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.apache.commons.codec.digest.DigestUtils
import java.util.*

class SignInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()

        val request: Request = requestBuilder
            .headers(getHeaders(original))
            .build()
        val response = chain.proceed(request)
        val responseBody = response.peekBody(1024 * 1024.toLong())
        val result = responseBody.string()
        println("请求Headers>>${request.headers}")
        println("请求URL>>${request.url}")
        println("请求结果>>>$result")
        return response
    }

    private fun getHeaders(request: Request): Headers {
        val builder = Headers.Builder()
        val timestamp = (System.currentTimeMillis() / 1000).toString()
        val nonce = DigestUtils.md5Hex(UUID.randomUUID().toString())
        val uri = request.url.toUri().path
        println("uri:$uri")
        val body = bodyToString(request)
        println("body:$body")
        // 请求签名(基于apiKey、时间戳、随机数、URL、请求内容的签名)
        val sign = DigestUtils.md5Hex(timestamp + nonce + uri + body)
        builder.add("timestamp", timestamp)
        builder.add("nonce", nonce)
        builder.add("sign", sign)
        if (request.method.lowercase() == "post") {
            builder.add("Content-Type", "application/json; charset=UTF-8")
        }
        return builder.build()
    }

    */
/**
 * post 请求参数获取
 *//*

    private fun bodyToString(request: Request): String {
        val copy = request.newBuilder().build()
        val buffer = Buffer()
        copy.body?.writeTo(buffer)
        return buffer.readUtf8()
    }
}
*/
