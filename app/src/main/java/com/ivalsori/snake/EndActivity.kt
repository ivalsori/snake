package com.ivalsori.snake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import java.io.File

class EndActivity : AppCompatActivity(), View.OnClickListener {
    private var score: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)
        val scoreString = intent.getStringExtra("score")
        score = findViewById(R.id.number_end_score)
        score?.text = scoreString
        if (scoreString == "317") {
            val result = findViewById<TextView>(R.id.text_end_result)
            result.text = getString(R.string.you_win)
        }
    }


    override fun onClick(p0: View?) {
        val leaders = File(filesDir, "leaders")
        if (!leaders.exists()) {
            leaders.createNewFile()
        }
        val scoreVal = score?.text.toString().toInt()
        val leadersList = leaders.readLines().map { val list = it.split(" "); Pair(list[0], list[1].toInt()) }.toMutableList()
        if (leadersList.size < 10 || leadersList[10].second < scoreVal) {
            val intent = Intent(this, EnterActivity::class.java)
            intent.putExtra("score", scoreVal)
            startActivity(intent)
        }
        finish()
    }

    override fun onBackPressed() {

    }
}