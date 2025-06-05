package com.example.quickresponseapp
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R


class QuizResultActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        val answersText: TextView = findViewById(R.id.answersText)
        val answers = intent.getStringArrayListExtra("answers") ?: arrayListOf()
        answersText.text = answers.withIndex().joinToString("\n") { (i, ans) -> "Q${i + 1}: $ans" }

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, HomeScreenFragment::class.java))
            finish() // Optional: closes this screen


        }

    }
}

