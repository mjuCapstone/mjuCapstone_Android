package Data
data class SignUpData (
    var id : String,
    var pw : String,
    var pwCheck : String,
    var nickname : String,
    var email : String,
    var height : Int,
    var weight : Int,
    var gender : Boolean,
    var year : Int,
    var level : Int, // 1 ~ 3
    var goal : Int // 1 ~ 5
)