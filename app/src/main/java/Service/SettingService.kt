package Service

import Data.UserData
import Response.ChangeSettingResponse
import Response.GetSettingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SettingService {
    @GET("/api/v1/setting/get")
    fun getSetting() : Call<GetSettingResponse>

    @POST("/api/v1/setting/change")
    fun changeSetting(@Body userData : UserData) : Call<ChangeSettingResponse>
}