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
import androidx.navigation.fragment.navArgs
import com.example.quickresponseapp.R
import com.example.quickresponseapp.databinding.FragmentEditContactBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class EditContactFragment : Fragment(R.layout.fragment_edit_contact) {

    // ViewModel shared for editing contact
    private val viewModel: AddEditContactViewModel by viewModels()
    // Argument passed from navigation with contact to be edited
    private val args: EditContactFragmentArgs by navArgs()

    private lateinit var contactImageView: CircleImageView
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
        val binding = FragmentEditContactBinding.bind(view)

        // Retrieve contact
        val contact = args.contact

        contactImageView = binding.contactImage

        // Pre-fill the existing contact data
        viewModel.contactName = contact.name
        viewModel.contactPhone = contact.phone
        viewModel.contactAddress = contact.address
        viewModel.contactRelation = contact.relation
        viewModel.isOrangaTamarikiApproved = contact.isOrangaTamarikiApproved
        viewModel.isDefault = contact.isDefault

        binding.apply {
            // Populate input fields with existing values
            editName.setText(viewModel.contactName)
            editPhone.setText(viewModel.contactPhone)
            editAddress.setText(viewModel.contactAddress)
            editRelationship.setText(viewModel.contactRelation)
            checkboxOranga.isChecked = viewModel.isOrangaTamarikiApproved
            checkboxDefault.isChecked = viewModel.isDefault

            // Save updates on text changes
            editName.addTextChangedListener { viewModel.contactName = it.toString() }
            editPhone.addTextChangedListener { viewModel.contactPhone = it.toString() }
            editAddress.addTextChangedListener { viewModel.contactAddress = it.toString() }
            editRelationship.addTextChangedListener { viewModel.contactRelation = it.toString() }

            // Save checkbox states to ViewModel
            checkboxOranga.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isOrangaTamarikiApproved = isChecked
            }

            checkboxDefault.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isDefault = isChecked
            }

            // Save button to save updates
            buttonSave.setOnClickListener {
                viewModel.onSaveClick()
            }

            // Launch image picker when profile image is clicked (select image from gallery)
            contactImageView.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }

            // Back button back to contacts page
            backButton.setOnClickListener {
                findNavController().navigate(R.id.contactsFragment)
            }

            // Navigate to emergency call page
            emergencyButton.setOnClickListener {
                findNavController().navigate(R.id.action_editContactFragment_to_emergencyPageFragment)
            }

            // Set the image if it exists
            if (!viewModel.contactImageUri.isNullOrEmpty()) {
                contactImageView.setImageURI(Uri.parse(viewModel.contactImageUri))
            }
            // Delete contact button
            buttonDelete.setOnClickListener {
                viewModel.onDeleteClick(contact)
            }
        }

        // Listen for any events from viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addEditContactEvent.collect { event: AddEditContactViewModel.AddEditContactEvent ->
                    when (event) {
                        // Show message if input is invalid
                        is AddEditContactViewModel.AddEditContactEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }
                        // Navigate back and pass the result if the contact was saved
                        is AddEditContactViewModel.AddEditContactEvent.NavigateBackWithResult -> {
                            binding.editName.clearFocus()
                            setFragmentResult(
                                "add_edit_result",
                                bundleOf("add_edit_result" to event.result)
                            )
                            findNavController().navigate(R.id.contactsFragment)
                        }
                    }
                }
            }
        }
    }
}