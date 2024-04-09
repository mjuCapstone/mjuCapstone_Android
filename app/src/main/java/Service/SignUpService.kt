package Service

import Data.SignUpData
import Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("/signup") // 실제 회원가입을 처리하는 서버의 엔드포인트로 변경해야 합니다.
    fun createUser(@Body signUpData : SignUpData): Call<SignUpResponse>
}