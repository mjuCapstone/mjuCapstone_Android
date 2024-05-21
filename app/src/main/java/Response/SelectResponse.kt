package Response
import Data.SelectData

data class SelectResponse(
    var status : Int,
    var message : String,
    var data : SelectData
)
