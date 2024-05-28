package Data
data class UserData (
    var email : String,
    var password : String,
    var nickname : String,
    var height : Int,
    var weight : Int,
    var gender : String,
    var birth : Int,
    var level : Int, // 1 ~ 3
    var dietPlan : Int // 1 ~ 5
)