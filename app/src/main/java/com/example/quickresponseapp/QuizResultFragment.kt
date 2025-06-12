package com.example.quickresponseapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quickresponseapp.contacts.ContactsViewModel
import com.example.quickresponseapp.profile.ProfileDatabase
import com.example.quickresponseapp.profile.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    // Arguments from Nav graph
    private val args: QuizResultFragmentArgs by navArgs()
    // Contacts viewModel to access contacts details
    private val contactsViewModel: ContactsViewModel by viewModels()
    // List of questions for Text message results
    private val questions = listOf(
        "Are you safe?",
        "Are you hurt?",
        "Do you feel scared?",
        "Do you want to talk to someone?",
        "Do you want to talk to the Police?",
        "Do you want to talk to the Ambulance?",
        "Do you want me to call the Police?",
        "Do you want me to call the Ambulance?",
        "Do you want to talk to someone else?"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create variables for finding the answers text and dynamic message
        val answersText: TextView = view.findViewById(R.id.answersText)
        val dynamicMessage: TextView = view.findViewById(R.id.dynamicMessage)
        // Set value for answers using args
        val answers = args.answers.toList()

        // Set results text format
        val quizResults = answers.withIndex().joinToString("\n") { (i, ans) ->
            "${questions.getOrNull(i) ?: "Question ${i + 1}"}: $ans"
        }

        // Show results for results screen
        answersText.text = answers.withIndex().joinToString("\n") { (i, ans) ->
            "Q${i + 1}: $ans"
        }

        // Count Yes / No
        val numYes = answers.count { it == "Yes" }
        val numNo = answers.count { it == "No" }

        // Decide message for result screen
        val messageToShow = when {
            (answers.size >= 4 && (answers[2] == "Yes" || answers[3] == "Yes")) ->
                getString(R.string.result_message_support)
            else ->
                getString(R.string.result_message_default)
        }
        // display dynamic message
        dynamicMessage.text = messageToShow

        lifecycleScope.launch {
            // Access the profile Dao
            val profileDao = ProfileDatabase.getDatabase(requireContext()).profileDao()
            // userProfile variable
            val userProfile: UserProfile? = profileDao.getProfile()
            // Child name variable
            val childName = userProfile?.name ?: "the child"
            contactsViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
                // set all contacts to send text to
                val allContacts = contacts

                val messageBody = when {
                    // User is not ready to talk but we want adults to be aware that they may seek help soon
                    numYes == 0 -> """
                        Kia Ora, $childName has completed the safety quiz but were not quite ready to talk but here's their results:
                    """.trimIndent() + "\n" + quizResults

                    // Send message if user doesn't need immediate help but want some support
                    numYes > 0 -> """
                        Kia Ora, $childName has completed the safety quiz and would like some support. Here's their results:
                    """.trimIndent() + "\n" + quizResults

                    else -> ""
                }

                // Send text to all contacts
                for (contact in allContacts) {
                    val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${contact.phone}"))
                    smsIntent.putExtra("sms_body", messageBody)
                    startActivity(smsIntent)
                }
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
}
