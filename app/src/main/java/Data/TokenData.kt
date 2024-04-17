package Data

data class TokenData(
    var grantType : String,
    var accessToken : String,
    var accessTokenExpiresIn : Long,
    var refreshToken : String
)
