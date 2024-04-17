package Response

import Data.SignUpResultData


data class SignUpResponse(
    var status : String,
    var message : String,
    var data : SignUpResultData
)
