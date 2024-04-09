package Service

import Data.IdCheckData
import Response.IdCheckResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface IdCheckService {
    @GET("/idcheck")
    fun idCheck(@Body checkData : IdCheckData): Call<IdCheckResponse>
}