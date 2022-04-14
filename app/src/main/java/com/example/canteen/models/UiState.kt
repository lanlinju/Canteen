package com.example.canteen.models

sealed class UiState {
    object LOADING : UiState()//正在加载数据
    object IDLE : UiState()//加载完毕或空闲状态
    data class ERROR(val message: String) : UiState()//出错状态
}