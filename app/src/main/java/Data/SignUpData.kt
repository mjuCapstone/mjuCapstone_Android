package Data
data class SignUpData (
    var id : String,
    var pw : String,
    var nickname : String,
    var email : String,
    var height : Int,
    var weight : Int,
    var gender : String,
    var year : Int,
    var level : Int, // 1 ~ 3
    var dietPlan : Int // 1 ~ 5
)