package Service

import Data.LoginData
import Response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/auth/login")
    fun login(@Body loginData : LoginData): Call<LoginResponse>
}