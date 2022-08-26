package org.qinfengsa.appdemo.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack.demo.R
import com.example.jetpack.demo.ui.theme.JetpackdemoTheme

@SuppressLint("ResourceType")
@Composable
fun PhotoGraphCard(modifier: Modifier) {
    Row(modifier) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            // image
            Image(
                painter = painterResource(id = R.drawable.avatar_5),
                contentDescription = "test"
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider() {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview
@Composable
fun PhotoGraphCardPreview() {
    JetpackdemoTheme() {

        PhotoGraphTopBar()
    }
}

@Composable
fun PhotoGraphTopBar() {
    Scaffold(topBar = {
        Row() {
            Text(text = "PhotoGraph")
            IconButton(onClick = {  /* doSomething() */  }) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            }
        }

    }) { innerPadding ->
        PhotoGraphCard(
            Modifier
                .padding(innerPadding)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.surface)
                .clickable(onClick = {

                })
                .padding(16.dp)
        )
    }
}
