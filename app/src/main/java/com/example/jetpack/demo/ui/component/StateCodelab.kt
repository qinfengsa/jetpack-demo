package com.example.jetpack.demo.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack.demo.ui.theme.JetpackdemoTheme


@Composable
fun WaterCounter(modifier: Modifier = Modifier) {

    Column(modifier = modifier.padding(16.dp)) {
        // 由于系统会在配置更改后（在本例中，即改变屏幕方向）重新创建 activity，因此已保存状态会被忘记
        // rememberSaveable 相比 remember 会自动保存可保存在 Bundle 中的任何值
        var count by rememberSaveable { mutableStateOf(0) }
        if (count > 0) {
            var showTask by rememberSaveable { mutableStateOf(true) }
            if (showTask) {
                WellnessTaskItem(taskName = "Have you taken your 15 minute walk today?",
                    checked = false,
                    onCheckedChange = {},
                    onClose = { showTask = false })
            }
            Text(text = "You've had $count glasses.")
        }
        Row(modifier = modifier.padding(top = 8.dp)) {
            Button(onClick = { count++ }, enabled = count < 10) {
                Text(text = "Add one")
            }
            Button(onClick = { count = 0 }, modifier.padding(start = 8.dp)) {
                Text(text = "Clear water count")
            }
        }

    }
}

@Composable
fun WellnessTaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = taskName, modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
    }
}

@Composable
fun WellnessTaskItem(taskName: String, onClose: () -> Unit, modifier: Modifier = Modifier) {
    var checkState by rememberSaveable {
        mutableStateOf(false)
    }
    WellnessTaskItem(
        taskName = taskName,
        checked = checkState,
        onCheckedChange = { newValue -> checkState = newValue },
        onClose = onClose,
        modifier = modifier
    )
}


data class WellnessTask(val id: Int, var label: String)


private fun getWellnessTaskList() = List(30) { i -> WellnessTask(i, "Task # $i") }

@Composable
fun WellnessTaskList(
    tasks: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier

) {
    LazyColumn(modifier = modifier) {
        items(tasks) { task ->
            WellnessTaskItem(taskName = task.label, onClose = { onCloseTask(task) })
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        onClick = { /*TODO*/ }) {
        Column {
            WaterCounter(modifier)
            val tasks = remember {
                getWellnessTaskList().toMutableStateList()
            }
            WellnessTaskList(tasks, { task -> tasks.remove(task) })
        }

    }
}


@Preview
@Composable
fun WellnessScreenPreview() {
    JetpackdemoTheme() {
        WellnessScreen()
    }

}