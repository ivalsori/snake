package com.ivalsori.snake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback

class GameActivity : AppCompatActivity(), View.OnClickListener {
    private var fieldView: FieldView? = null
    private var score: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        fieldView = findViewById(R.id.field_view)
        score = findViewById(R.id.number_game_score)
    }

    private val gameActivity: GameActivity
        get() = this

    private var timer = object : CountDownTimer(1000000000, 1000) {
        override fun onTick(p0: Long) {
            fieldView?.tick()
            score?.text = fieldView?.field?.score.toString()
            if (fieldView?.gameState == State.GAME_OVER) {
                val intent = Intent(gameActivity, EndActivity::class.java)
                intent.putExtra("score", score?.text)
                startActivity(intent)
                cancel()
                finish()
            }
        }

        override fun onFinish() {

        }
    }

    init {
        timer.start()
    }

    override fun onClick(p0: View?) {
        if (fieldView?.gameState == State.START) {
            fieldView?.gameState = State.RUNNING
        }
        when(p0?.id) {
            R.id.btn_game_down -> fieldView?.field?.turn(Direction.DOWN)
            R.id.btn_game_up -> fieldView?.field?.turn(Direction.UP)
            R.id.btn_game_left -> fieldView?.field?.turn(Direction.LEFT)
            R.id.btn_game_right -> fieldView?.field?.turn(Direction.RIGHT)
            R.id.btn_game_pause -> {
                if (fieldView?.gameState == State.RUNNING) {
                    fieldView?.gameState = State.PAUSE
                } else if (fieldView?.gameState == State.PAUSE) {
                    fieldView?.gameState = State.RUNNING
                }
            }
        }
    }
}