package com.example.masterand

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class RowData(var id: Int) {
    var selectedColors = mutableStateListOf(Color.White, Color.White, Color.White, Color.White)
    var callbackColors = mutableStateListOf(Color.White, Color.White, Color.White, Color.White)
    var isClickable by mutableStateOf(true)
    var isFilled by mutableStateOf(!selectedColors.contains(Color.White))


}