package Util

import android.content.Context
import android.widget.Toast

class Util {
    public fun print(c : Context, message : String){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }
}