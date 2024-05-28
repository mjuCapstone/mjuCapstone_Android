package Response

import Data.MenuItem

data class ChangeSettingResponse(
    val status : Int,
    var message : String,
    var data : List<MenuItem>
)
