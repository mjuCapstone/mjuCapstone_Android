package Service

import Response.HistoryResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HistoryService {
    @GET("/api/v1/item/date")
    fun history(@Query("date") date : String) : Call<HistoryResponse>
}