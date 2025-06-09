package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class QuizFragment : Fragment(R.layout.fragment_quiz) {
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
        Question("Do you want me to call the Ambulance?", R.drawable.kaurirightwing)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionText = view.findViewById(R.id.questionText)
        kauriImage = view.findViewById(R.id.birdImage)
        yesButton = view.findViewById(R.id.yesButton)
        noButton = view.findViewById(R.id.noButton)

        loadQuestion()

        yesButton.setOnClickListener { handleAnswer("Yes") }
        noButton.setOnClickListener { handleAnswer("No") }

        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]
        questionText.text = q.text
        kauriImage.setImageResource(q.imageResId)
    }

    private fun handleAnswer(answer: String) {
        answers.add(answer)
        when (currentIndex) {
            0 -> currentIndex = if (answer == "No") 3 else 1
            1 -> currentIndex = if (answer == "Yes") 3 else 2
            2 -> currentIndex = 3
            3 -> currentIndex = if (answer == "Yes") 4 else 7
            4 -> currentIndex = if (answer == "Yes") 6 else 5
            5 -> currentIndex = if (answer == "Yes") 6 else 7
            6 -> {
                if (answer == "Yes") {
                    showEmergencyCall("Police")
                    return
                } else currentIndex = 7
            }
            7 -> {
                if (answer == "Yes") {
                    showEmergencyCall("Ambulance")
                    return
                } else showSummary()
            }
        }
        loadQuestion()
    }

    private fun showEmergencyCall(service: String) {
        // Add emergency call screen
    }

    private fun showSummary() {
        val action = QuizFragmentDirections.actionQuizFragmentToQuizResultFragment(answers.toTypedArray())
        findNavController().navigate(action)
    }
}