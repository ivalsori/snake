package com.ivalsori.snake

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

enum class State {
    START,
    PAUSE,
    RUNNING,
    GAME_OVER
}

class FieldView(context: Context?, attrs: AttributeSet?): View(context, attrs) {
    var gameState = State.START


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        field.draw(canvas)
    }

    var field = Field(20, 16, context!!.resources)

    fun tick() {
        if (gameState == State.RUNNING) {
            if (!field.move()) {
                gameState = State.GAME_OVER
            }

            invalidate()
        }
    }
}