package com.yigitserin.todoapp.data.repository

import com.yigitserin.todoapp.data.entity.network.LoginResponseModel
import com.yigitserin.todoapp.utils.Resource

interface LoginRepository {
    suspend fun login(email: String, password: String): Resource<LoginResponseModel>
}