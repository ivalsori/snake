package com.ivalsori.snake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_main_start -> {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_main_leaderboard -> {
                val intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_main_exit -> finish()
        }
    }
}