package Response

import Data.MenuItem
import Data.UserData

data class GetSettingResponse(
    val status : Int,
    var message : String,
    var data : UserData
)
