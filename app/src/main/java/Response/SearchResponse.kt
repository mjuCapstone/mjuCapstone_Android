package Response

import Data.AddItemResponseData
import Data.MenuItem

data class SearchResponse (
    var status : Int,
    var message : String,
    var data : List<String>
)