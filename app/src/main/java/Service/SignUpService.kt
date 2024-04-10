package Service

import Data.SignUpData
import Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("/")
    fun signUp(@Body signUpData : SignUpData): Call<SignUpResponse>
}