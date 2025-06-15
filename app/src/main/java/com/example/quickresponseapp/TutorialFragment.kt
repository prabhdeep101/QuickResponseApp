package com.example.quickresponseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TutorialFragment : Fragment(R.layout.fragment_tutorial) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.tutorial_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val videoList = listOf(
            TutorialVideo("Register",  R.raw.register),
            TutorialVideo("Edit Profile", R.raw.edit_profile),
            TutorialVideo("Change Language", R.raw.changing_language),
            TutorialVideo("Add Contact", R.raw.add_contact),
            TutorialVideo("Edit Contact", R.raw.edit_contact),
            TutorialVideo("Delete Contact", R.raw.delete_contact),
            TutorialVideo("Emergency Call", R.raw.emergency_call),
            TutorialVideo("Messaging", R.raw.messaging),
            TutorialVideo("Quiz", R.raw.quiz),
            TutorialVideo("Menu Navigation", R.raw.menu_access)
        )

        recyclerView.adapter = TutorialAdapter(videoList)
    }
}