package Data

data class AddItemResponseData(
    var id : Int,
    var name : String,
    var kcal : Int,
    var carbobydrate : Int,
    var protein : Int,
    var fat : Int,
    var fileName : String
)
