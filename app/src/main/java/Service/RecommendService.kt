package Service

import Data.RecommendData
import Response.RecommendResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendService {
    @POST("/api/v1/chat/recommendation")
    fun recommend(@Body recommendData : RecommendData) : Call<RecommendResponse>
}