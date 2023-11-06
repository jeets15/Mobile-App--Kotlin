package com.team2.todo.screens.listing.ui_components.completed_sale

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.team2.todo.R
import com.team2.todo.common_ui_components.EmptyList

/**
 * Created by Manu KJ on 11/6/23.
 */
@Composable
fun CompletedSaleList() {
    EmptyList(title = "Sale List Empty", drawableID = R.drawable.no_completed_list)
}