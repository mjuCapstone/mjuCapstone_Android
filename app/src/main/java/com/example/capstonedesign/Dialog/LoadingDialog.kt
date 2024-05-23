package com.example.capstonedesign.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.capstonedesign.R

class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_loading, null)
        setContentView(view)

        val imageView: ImageView = view.findViewById(R.id.img_gif)
        Glide.with(context)
            .asGif()
            .load(R.drawable.loading_animation) // GIF 파일을 drawable 폴더에 넣어야 합니다.
            .into(imageView)
    }
}