package Response

data class SignUpFailureResponse(
    var message : String,
    var error : String,
    var status : Int
)
