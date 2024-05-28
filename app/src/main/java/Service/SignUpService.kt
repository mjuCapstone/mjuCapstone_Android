package Service

import Data.UserData
import Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("/api/v1/auth/signup")
    fun signUp(@Body signUpData : UserData): Call<SignUpResponse>
}