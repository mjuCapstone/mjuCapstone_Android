package Response

import Data.SelectData

data class RecommendResponse (
    val status : Int,
    val message : String,
    val data : SelectData
)