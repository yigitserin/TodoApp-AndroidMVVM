package com.yigitserin.todoapp.ui.landing

sealed class LandingViewUIEvent{
    object NavigateToLogin : LandingViewUIEvent()
    object NavigateToList : LandingViewUIEvent()
}