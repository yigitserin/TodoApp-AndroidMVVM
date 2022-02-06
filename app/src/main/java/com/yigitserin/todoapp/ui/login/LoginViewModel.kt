package com.yigitserin.todoapp.ui.login

import androidx.lifecycle.ViewModel
import com.yigitserin.todoapp.R
import com.yigitserin.todoapp.data.repository.LoginRepository
import com.yigitserin.todoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel(){

    private val viewModelState = MutableStateFlow(LoginViewUIState())
    internal val uiState: StateFlow<LoginViewUIState> = viewModelState

    private val oneShotUiEventsChannel = Channel<LoginViewUIEvent>(Channel.BUFFERED)
    internal val oneShotUiEvents: Flow<LoginViewUIEvent> = oneShotUiEventsChannel.receiveAsFlow()

    suspend fun login(email: String, password: String){
        viewModelState.emit(LoginViewUIState(isLoading = true))
        val response = loginRepository.login(email, password)
        viewModelState.emit(LoginViewUIState(isLoading = false))
        if (response.status == Resource.Status.SUCCESS){
            oneShotUiEventsChannel.send(LoginViewUIEvent.NavigateToList)
        }else{
            oneShotUiEventsChannel.send(LoginViewUIEvent.ShowToast(R.string.ui_login_response_error))
        }
    }
}