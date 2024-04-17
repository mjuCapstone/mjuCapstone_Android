package Response

import Data.TokenData

data class LoginResponse(
    var status : String,
    var message : String,
    var data : TokenData
)
