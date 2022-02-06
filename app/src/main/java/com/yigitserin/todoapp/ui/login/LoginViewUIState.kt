package com.yigitserin.todoapp.ui.login

data class LoginViewUIState(
    val isLoading:Boolean = false
)

sealed class LoginViewUIEvent{
    data class ShowToast(val messageId: Int) : LoginViewUIEvent()
    object NavigateToList : LoginViewUIEvent()
}