package Response

data class LoginResponse(
    var grantType : String,
    var accessToken : String,
    var accessTokenExpiresIn : Long,
    var refreshToken : String
)
