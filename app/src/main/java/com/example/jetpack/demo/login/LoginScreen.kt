package com.example.jetpack.demo.login

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack.demo.ui.theme.JetpackdemoTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen() {

    Scaffold {
        TopAppBar(
            title = {
                Text(
                    text = "登录",
                    textAlign = TextAlign.Center,
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowLeft,
                    contentDescription = null
                )
            },
            actions = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        )
        var username by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var captcha by remember { mutableStateOf(TextFieldValue("")) }
        var hasError by remember { mutableStateOf(false) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val usernameInteractionState = remember { MutableInteractionSource() }
        LazyColumn(modifier = Modifier.padding(top = 60.dp).fillMaxSize().padding(horizontal = 16.dp)) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            item {
                Text(
                    text = "Welcome Back",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "We have missed you, Let's start by Sign In!",
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = username,
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    // colors = TextFieldDefaultsMaterial.outlinedTextFieldColors(),
                    label = { Text(text = "username") },
                    placeholder = { Text(text = "abc@gmail.com") },
                    onValueChange = {
                        username = it
                    },
                    interactionSource = usernameInteractionState,
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    /*colors = TextFieldDefaultsMaterial.outlinedTextFieldColors(),*/
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text(text = "密码") },
                    placeholder = { Text(text = "请输入密码") },
                    onValueChange = {
                        password = it
                    },
                    interactionSource = passwordInteractionState,
                    visualTransformation = passwordVisualTransformation,
                )
            }
            item {
                var loading by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        if (invalidInput(username.text, password.text)) {
                            hasError = true
                            loading = false
                        } else {
                            loading = true
                            hasError = false
                            // onLoginSuccess.invoke()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(text = "登录")
                    }
                }
            }

        }
    }
}
 

fun invalidInput(username: String, password: String) =
    username.isBlank() || password.isBlank()


@Preview
@Composable
fun LoginPreview() {
    JetpackdemoTheme {
        LoginScreen()
    }
}
