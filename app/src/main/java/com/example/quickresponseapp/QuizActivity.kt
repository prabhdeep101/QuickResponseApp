package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R

class QuizActivity : AppCompatActivity() {
    private lateinit var questionText: TextView
    private lateinit var kauriImage: ImageView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    private var currentIndex = 0
    private val answers = mutableListOf<String>()
    data class Question(
        val text: String,
        val imageResId: Int
    )
    private val questions = listOf(
        Question("Are you safe?", R.drawable.kaurileftwing),
        Question("Are you hurt?", R.drawable.kaurirightwing),
        Question("Do you feel scared?", R.drawable.kaurileftwing),
        Question("Do you want to talk to someone?", R.drawable.kaurirightwing),
        Question("Do you want to talk to the Police?", R.drawable.kaurileftwing),
        Question("Do you want to talk to the Ambulance?", R.drawable.kaurirightwing),
        Question("Do you want me to call the Police?", R.drawable.kaurileftwing),
        Question("Do you want me to call the Ambulance?", R.drawable.kaurirightwing),
        Question("Do you want to message someone?", R.drawable.kaurileftwing),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.questionText)
        kauriImage = findViewById(R.id.birdImage)
        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)

        loadQuestion()

        yesButton.setOnClickListener { handleAnswer("Yes") }
        noButton.setOnClickListener { handleAnswer("No") }
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]
        questionText.text = q.text
        kauriImage.setImageResource(q.imageResId)
    }

    private fun handleAnswer(answer: String) {
        answers.add(answer)
        when (currentIndex) {
            0 -> currentIndex = 1 // Q1 → Q2
            1 -> currentIndex = 2 // Q2 → Q3
            2 -> currentIndex = 3 // Q3 → Q4
            3 -> currentIndex = if (answer == "Yes") 4 else 8 // Q5 or Q9
            4 -> currentIndex = 5 // Q6
            5 -> currentIndex = 6 // Q7
            6 -> if (answer == "Yes") {
                showEmergencyCall("Police")
                return
            } else currentIndex = 7
            7 -> if (answer == "Yes") {
                showEmergencyCall("Ambulance")
                return
            } else currentIndex = 8
            8 -> showSummary()
        }
        loadQuestion()
    }

    private fun showEmergencyCall(service: String) {
        val intent = Intent(this, EmergencyCallActivity::class.java)
        intent.putExtra("service", service)
        startActivity(intent)
        finish()
    }

    private fun showSummary() {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putStringArrayListExtra("answers", ArrayList(answers))
        startActivity(intent)
        finish()
    }
}
