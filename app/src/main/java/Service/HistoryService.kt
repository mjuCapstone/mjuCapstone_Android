package Service

import Response.HistoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HistoryService {
    @GET("/api/v1/item/date")
    fun history(@Header("date") date : String) : Call<HistoryResponse>
}