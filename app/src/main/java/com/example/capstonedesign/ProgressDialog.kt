package com.example.capstonedesign

import android.R
import android.app.Dialog
import android.content.Context
import android.view.Window


class ProgressDialog : Dialog {
    constructor(context: Context?):super(context!!){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
}