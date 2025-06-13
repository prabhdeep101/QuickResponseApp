package com.example.quickresponseapp.contacts

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.quickresponseapp.R
import com.example.quickresponseapp.databinding.FragmentAddContactBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddContactFragment : Fragment(R.layout.fragment_add_contact) {
    // ViewModel to hold the user data
    private val viewModel: AddEditContactViewModel by viewModels()
    // Reference contact image
    private lateinit var contactImageView: CircleImageView

    // Stores URI of contact picture
    private var selectedImageUri: String? = null
    // Picking an image from photo gallery
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // Save contact image to internal storage and update the image
            val savedPath = viewModel.saveImageToInternalStorage(requireContext(), it)
            if (savedPath != null) {
                contactImageView.setImageURI(Uri.fromFile(File(savedPath)))
                viewModel.contactImageUri = savedPath
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddContactBinding.bind(view)
        contactImageView = binding.contactImage

        binding.apply {
            // Populate with saved state from viewModel
            addContactname.setText(viewModel.contactName)
            addContactphone.setText(viewModel.contactPhone)
            addContactaddress.setText(viewModel.contactAddress)
            addContactrelationship.setText(viewModel.contactRelation)
            checkboxOranga.isChecked = viewModel.isOrangaTamarikiApproved
            checkboxDefault.isChecked = viewModel.isDefault

            // Save user input into viewModel when text changes
            addContactname.addTextChangedListener { viewModel.contactName = it.toString() }
            addContactphone.addTextChangedListener { viewModel.contactPhone = it.toString() }
            addContactaddress.addTextChangedListener { viewModel.contactAddress = it.toString() }
            addContactrelationship.addTextChangedListener { viewModel.contactRelation = it.toString() }

            // Save the checked states
            checkboxOranga.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isOrangaTamarikiApproved = isChecked
            }

            checkboxDefault.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isDefault = isChecked
            }

            // Saves the contact details with save button
            buttonSave.setOnClickListener {
                viewModel.onSaveClick()
            }

            // Navigate to emergency call screen
            emergencyButton.setOnClickListener {
                findNavController().navigate(R.id.action_addContactFragment_to_emergencyPageFragment)
            }

            // Navigate back to the contacts page
            backButton.setOnClickListener {
                findNavController().navigate(R.id.contactsFragment)
            }

            // Launch image picker when profile image is clicked (select image from gallery)
            contactImageView.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }

            // Load image if one is saved
            if (!viewModel.contactImageUri.isNullOrEmpty()) {
                contactImageView.setImageURI(Uri.parse(viewModel.contactImageUri))
            }
        }

        // Listen for any events from viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addEditContactEvent.collect { event ->
                    when (event) {
                        // Show message if input is invalid
                        is AddEditContactViewModel.AddEditContactEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }
                        // Navigate back and pass the result if the contact was saved
                        is AddEditContactViewModel.AddEditContactEvent.NavigateBackWithResult -> {
                            binding.addContactname.clearFocus()
                            setFragmentResult(
                                "add_edit_result",
                                bundleOf("add_edit_result" to event.result)
                            )
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }
}