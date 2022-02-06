package com.yigitserin.todoapp.data.remote

import com.yigitserin.todoapp.data.entity.network.LoginRequestModel
import com.yigitserin.todoapp.data.entity.network.LoginResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {

    companion object{
        const val BASE_URL = "https://reqres.in/api/"
    }

    @POST("login")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): Response<LoginResponseModel>
}