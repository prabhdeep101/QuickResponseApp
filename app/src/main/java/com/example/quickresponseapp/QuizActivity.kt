package com.example.quickresponseapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    private val questions = listOf(
        "Are you safe?",
        "Are you hurt?",
        "Do you feel scared?",
        "Do you want to talk to someone?",
        "Do you want to talk to police?",
        "Do you want to talk to ambulance?",
        "Do you want me to call the police?",
        "Do you want me to call the ambulance?",
        "Do you want to message someone?"
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.questionText)
        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)

        updateQuestion()

        yesButton.setOnClickListener {
            nextQuestion()
        }

        noButton.setOnClickListener {
            nextQuestion()
        }
    }

    private fun updateQuestion() {
        if (currentIndex < questions.size) {
            questionText.text = questions[currentIndex]
        } else {
            questionText.text = "Thanks for completing the quiz!"
            yesButton.isEnabled = false
            noButton.isEnabled = false
        }
    }

    private fun nextQuestion() {
        currentIndex++
        updateQuestion()
    }
}
