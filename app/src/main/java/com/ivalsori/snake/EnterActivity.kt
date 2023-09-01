package com.ivalsori.snake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.io.File

class EnterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)
    }

    override fun onClick(p0: View?) {
        val score = intent.getIntExtra("score", 0)
        val leaders = File(filesDir, "leaders")
        if (!leaders.exists()) {
            leaders.createNewFile()
        }
        val leadersList = leaders.readLines().map { val list = it.split(" "); Pair(list[0], list[1].toInt()) }.toMutableList()
        var pos = leadersList.size
        val name = findViewById<EditText>(R.id.edit_enter_name)
        leadersList.add(Pair(name.text.toString(), score))
        while (pos > 0 && leadersList[pos].second > leadersList[pos - 1].second) {
            val temp = leadersList[pos]
            leadersList[pos] = leadersList[pos - 1]
            leadersList[pos - 1] = temp
            --pos
        }
        if (leadersList.size > 10) {
            leadersList.removeLast()
        }
        leaders.printWriter().use { out ->
            leadersList.forEach { out.println("${it.first} ${it.second}") }
        }
        finish()
    }

    override fun onBackPressed() {

    }
}