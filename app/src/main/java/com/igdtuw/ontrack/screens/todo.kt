package com.igdtuw.ontrack.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.igdtuw.ontrack.getFakeTodos

@Composable
fun TodoListPage(){
    val todoList = getFakeTodos()

}