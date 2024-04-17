package Service

import Data.SignUpData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("/api/v1/auth/signup")
    fun signUp(@Body signUpData : SignUpData): Call<ResponseBody>
}