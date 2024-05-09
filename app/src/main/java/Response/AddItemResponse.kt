package Response

import Data.AddItemResponseData
import Data.SignUpResultData

data class AddItemResponse(
    var status : String,
    var message : String,
    var data : AddItemResponseData
)
