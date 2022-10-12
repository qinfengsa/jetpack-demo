package com.example.jetpack.demo.data.api.model

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    val captcha: String, val captchaToken: String,
    val login: String, val password: String
)

data class CaptchaRequest(@SerializedName("captchaToken") val captchaToken: String)
