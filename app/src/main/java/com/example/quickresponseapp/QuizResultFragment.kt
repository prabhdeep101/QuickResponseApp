package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.todolist.R

class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val answersText: TextView = view.findViewById(R.id.answersText)
        val answers = requireActivity().intent.getStringArrayListExtra("answers") ?: arrayListOf()
        answersText.text = answers.withIndex().joinToString("\n") { (i, ans) -> "Q${i + 1}: Answer = $ans \n" }

        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(requireContext(), HomeScreen::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
