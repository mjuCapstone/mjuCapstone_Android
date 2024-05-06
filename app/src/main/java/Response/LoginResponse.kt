package Response

import Data.AutoLoginResultData
import Data.GoalNutritionInfo
import Data.HistoryInfo
import Data.MainData
import Data.TokenData
import com.doinglab.foodlens2.sdk.model.Nutrition

data class LoginResponse(
    var status : String,
    var message : String,
    var data : TokenData
)

data class AutoLoginResponse(
    var status : Int,
    var message : String,
    var data : MainData
)