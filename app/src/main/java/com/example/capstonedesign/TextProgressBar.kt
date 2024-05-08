package com.example.capstonedesign

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ProgressBar

class TextProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {

    private var textPaint: Paint = Paint()
    private var progressText = "20g / 30g"

    init {
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 30f
    }
    public fun setProgressBar(now : Int, goal : Int){
        progress = now
        max = goal
        progressText = now.toString() + "g / " + goal.toString() + "g"
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = width / 2f
        val y = (height / 2 - (textPaint.descent() + textPaint.ascent()) / 2)
        canvas.drawText(progressText, x, y, textPaint)
    }
}