package Response

import Data.MenuItem

data class HistoryResponse (
    val status : Int,
    var message : String,
    var data : List<MenuItem>
)