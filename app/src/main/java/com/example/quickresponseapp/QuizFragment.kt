package com.example.quickresponseapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    // User interface components
    private lateinit var questionText: TextView
    private lateinit var kauriImage: ImageView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    // Track current question
    private var currentIndex = 0
    // Store users answers
    private val answers = mutableListOf<String>()

    // Data class for holding the question and image
    data class Question(
        val text: String,
        val imageResId: Int
    )

    // List of all questions and their images
    private val questions by lazy {
        listOf(
            Question(requireContext().getString(R.string.question_are_you_safe), R.drawable.kaurileftwing),
            Question(requireContext().getString(R.string.question_are_you_hurt), R.drawable.bothwingsout),
            Question(requireContext().getString(R.string.question_do_you_feel_scared), R.drawable.kaurileftwing),
            Question(requireContext().getString(R.string.question_want_to_talk), R.drawable.kaurirightwing),
            Question(requireContext().getString(R.string.question_talk_to_police), R.drawable.bothwingsout),
            Question(requireContext().getString(R.string.question_talk_to_ambulance), R.drawable.kaurirightwing),
            Question(requireContext().getString(R.string.question_me_call_police), R.drawable.kaurileftwing),
            Question(requireContext().getString(R.string.question_me_call_ambulance), R.drawable.bothwingsout),
            Question(requireContext().getString(R.string.question_talk_to_someone_else), R.drawable.kaurirightwing)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialise views
        questionText = view.findViewById(R.id.questionText)
        kauriImage = view.findViewById(R.id.birdImage)
        yesButton = view.findViewById(R.id.yesButton)
        noButton = view.findViewById(R.id.noButton)

        // Load the first question
        loadQuestion()
        // Set button click actions
        yesButton.setOnClickListener { handleAnswer("Yes") }
        noButton.setOnClickListener { handleAnswer("No") }

        // Back button to go to previous question
        val backButton: TextView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            if (currentIndex > 0 && answers.isNotEmpty()) {
                // Go back one and remove answer submitted
                currentIndex = getPreviousIndex(currentIndex, answers.last())
                answers.removeAt(answers.size - 1)
                loadQuestion()
            } else {
                // If at first question, return to home
                findNavController().navigate(R.id.homeScreenFragment)
            }
        }

        // Home button
        val homeButton: ImageButton = view.findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        // Emergency button
        val emergencyButton: ImageButton = view.findViewById(R.id.emergency_button)
        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.emergencyPageFragment)
        }
    }

    // Loads current question and updates screen
    private fun loadQuestion() {
        val q = questions[currentIndex]
        questionText.text = q.text
        kauriImage.setImageResource(q.imageResId)
    }

    // Handles answers
    private fun handleAnswer(answer: String) {
        answers.add(answer)
        currentIndex = getNextIndex(currentIndex, answer)

        // If we reached special state
        if (currentIndex == -1) {
            showEmergencyCall("Police")
            return
        } else if (currentIndex == -2) {
            showEmergencyCall("Ambulance")
            return
        } else if (currentIndex == -3) {
            showSummary()
            return
        } else if (currentIndex == -4) {
            showContactsScreen()
            return
        }

        loadQuestion()
    }

    // Logic to determine next index
    private fun getNextIndex(index: Int, answer: String): Int {
        return when (index) {
            // Q1 → Q5 or Q2
            0 -> if (answer == "No") 4 else 1
            // Q2 → Q5 or Q3
            1 -> if (answer == "Yes") 4 else 2
            // Q3 → Q4 (no branch)
            2 -> 3
            // Q4 → Q5 (no branch)
            3 -> 4
            // Q5 → Emergency or Q6
            4 -> if (answer == "Yes") -1 else 5
            // Q6 → Emergency or Q7
            5 -> if (answer == "Yes") -2 else 6
            // Q7 → Emergency or Q8
            6 -> if (answer == "Yes") -1 else 7
            // Q8 → Emergency or Q9
            7 -> if (answer == "Yes") -2 else 8
            // Q9 → Contacts screen (-4) or Results (-3)
            8 -> if (answer == "Yes") -4 else -3
            else -> 0
        }
    }

    // Find previous question
    private fun getPreviousIndex(index: Int, previousAnswer: String): Int {
        return when (index) {
            1 -> 0
            2 -> 1
            3 -> {
                if (answers.size >= 2) {
                    val prevAnswer = answers[answers.size - 2]
                    if (prevAnswer == "No") 0 else 1
                } else 0
            }
            4 -> 3
            5 -> 4
            6 -> if (answers.size >= 2 && answers[answers.size - 2] == "Yes") 4 else 5
            7 -> 3
            else -> 0
        }
    }

    // Show the emergency call screen
    private fun showEmergencyCall(service: String) {
        val action = QuizFragmentDirections.actionQuizFragmentToEmergencyPageFragment()
        findNavController().navigate(action)
    }

    // Show the contact screen
    private fun showContactsScreen() {
        findNavController().navigate(R.id.contactsFragment)
    }

    // Show the results
    private fun showSummary() {
        val action = QuizFragmentDirections.actionQuizFragmentToQuizResultFragment(answers.toTypedArray())
        findNavController().navigate(action)
    }
}