package com.yigitserin.todoapp.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yigitserin.todoapp.data.local.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val oneShotUiEventsChannel = Channel<LandingViewUIEvent>(Channel.BUFFERED)
    internal val oneShotUiEvents: Flow<LandingViewUIEvent> = oneShotUiEventsChannel.receiveAsFlow()

    fun checkLoginStatus(){
        viewModelScope.launch {
            if (appPreferences.isLoggedIn()){
                oneShotUiEventsChannel.send(LandingViewUIEvent.NavigateToList)
            }else{
                oneShotUiEventsChannel.send(LandingViewUIEvent.NavigateToLogin)
            }
        }
    }
}