package com.example.jetpack.demo.ui.bills

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.example.jetpack.demo.R
import com.example.jetpack.demo.data.Bill
import com.example.jetpack.demo.data.UserData
import com.example.jetpack.demo.ui.component.BillRow
import com.example.jetpack.demo.ui.component.StatementBody


@Composable
fun BillsScreen(
    bills: List<Bill> = remember {
        UserData.bills
    }
) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Bills" },
        items = bills,
        amounts = { bill -> bill.amount },
        colors = { bill -> bill.color },
        amountsTotal = bills.map { bill -> bill.amount }.sum(),
        circleLabel = stringResource(R.string.due),
        rows = { bill ->
            BillRow(bill.name, bill.due, bill.amount, bill.color)
        }
    )
}