package Service

import Data.LoginData
import Data.SignUpData
import Response.LoginResponse
import Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface LoginService {
    @GET("/login")
    fun login(@Body loginData : LoginData): Call<LoginResponse>
}