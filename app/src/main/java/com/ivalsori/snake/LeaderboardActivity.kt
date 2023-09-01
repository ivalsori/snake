package com.ivalsori.snake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import java.io.File

class LeaderboardActivity : AppCompatActivity(), View.OnClickListener {
    private var leaders: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        leaders = File(filesDir, "leaders")
        if (!leaders!!.exists()) {
            leaders!!.createNewFile()
        }
        val leadersList = leaders!!.readLines().map { val list = it.split(" "); Pair(list[0], list[1]) }
        for (i in 1..leadersList.size) {
            val nameId = resources.getIdentifier("text_leader_${i}_name", "id", packageName)
            val scoreId = resources.getIdentifier("text_leader_${i}_score", "id", packageName)
            val name = findViewById<TextView>(nameId)
            val score = findViewById<TextView>(scoreId)
            name.text = leadersList[i - 1].first
            score.text = leadersList[i - 1].second
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_leader_clear -> {
                leaders = File(filesDir, "leaders")
                leaders?.delete()
                recreate()
            }
        }
    }
}