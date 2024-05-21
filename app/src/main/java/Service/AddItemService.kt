package Service

import Data.AddItemData
import Data.SignUpData
import Response.AddItemResponse
import Response.SearchResponse
import Response.SelectResponse
import Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

//메뉴를 추가하기 위한 API 요청 모음
interface AddItemService {
    @GET("api/v1/foods/string")
    fun search(@Query("name") name : String): Call<SearchResponse>
    @GET("api/v1/food")
    fun select(@Query("name") name : String): Call<SelectResponse>
    @POST("/api/v1/item")
    fun addItem(@Body addItemData: AddItemData): Call<AddItemResponse>
}