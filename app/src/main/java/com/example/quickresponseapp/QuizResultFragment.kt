package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {
    private val args: QuizResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val answersText: TextView = view.findViewById(R.id.answersText)
        val answers = args.answers.toList()
        answersText.text = answers.withIndex().joinToString("\n") { (i, ans) -> "Q${i + 1}: Answer = $ans \n" }

        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }
    }
}
