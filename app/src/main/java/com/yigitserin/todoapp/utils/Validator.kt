package com.yigitserin.todoapp.utils

import android.util.Patterns.EMAIL_ADDRESS
import javax.inject.Inject

class Validator @Inject constructor(){
    fun validateEmail(email: String): Boolean{
        return email.isNotEmpty() && EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateTextNotEmpty(text: String): Boolean{
        return text.isNotEmpty()
    }
}