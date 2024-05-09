package Service

import Data.AddItemData
import Data.SignUpData
import Response.AddItemResponse
import Response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AddItemService {
    @POST("/api/v1/item")
    fun addItem(@Body addItemData: AddItemData): Call<AddItemResponse>
}