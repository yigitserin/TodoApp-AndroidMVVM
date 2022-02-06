package com.yigitserin.todoapp.data.repository

import com.yigitserin.todoapp.data.entity.network.LoginRequestModel
import com.yigitserin.todoapp.data.entity.network.LoginResponseModel
import com.yigitserin.todoapp.data.local.AppPreferences
import com.yigitserin.todoapp.data.remote.LoginAPI
import com.yigitserin.todoapp.utils.Resource
import com.yigitserin.todoapp.utils.Validator
import javax.inject.Inject

class DefaultLoginRepository @Inject constructor(
    private val loginAPI: LoginAPI,
    private val validator: Validator,
    private val appPreferences: AppPreferences
): LoginRepository {

    override suspend fun login(email: String, password: String): Resource<LoginResponseModel> {
        return if (validator.validateEmail(email) && validator.validateTextNotEmpty(password)){
            return try {
                val response = loginAPI.login(LoginRequestModel(email, password))
                if(response.isSuccessful) {
                    response.body()?.let { responseModel ->
                        appPreferences.setLoginToken(responseModel.token)
                        return@let Resource.success(responseModel)
                    } ?: Resource.error("Response failed.", null)
                } else {
                    Resource.error("Request failed", null)
                }
            } catch(e: Exception) {
                Resource.error("Request failed.", null)
            }
        }else{
            Resource.error("Validation failed", null)
        }
    }
}