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

    private val viewModel: AddEditContactViewModel by viewModels()
    private val args: EditContactFragmentArgs by navArgs()
    private lateinit var contactImageView: CircleImageView

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
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
        val contact = args.contact

        contactImageView = binding.contactImage

        // Pre-fill the contact data
        viewModel.contactName = contact.name
        viewModel.contactPhone = contact.phone
        viewModel.contactAddress = contact.address
        viewModel.contactRelation = contact.relation
        viewModel.isOrangaTamarikiApproved = contact.isOrangaTamarikiApproved
        viewModel.isDefault = contact.isDefault

        binding.apply {
            editName.setText(viewModel.contactName)
            editPhone.setText(viewModel.contactPhone)
            editAddress.setText(viewModel.contactAddress)
            editRelationship.setText(viewModel.contactRelation)
            checkboxOranga.isChecked = viewModel.isOrangaTamarikiApproved
            checkboxDefault.isChecked = viewModel.isDefault

            editName.addTextChangedListener { viewModel.contactName = it.toString() }
            editPhone.addTextChangedListener { viewModel.contactPhone = it.toString() }
            editAddress.addTextChangedListener { viewModel.contactAddress = it.toString() }
            editRelationship.addTextChangedListener { viewModel.contactRelation = it.toString() }

            checkboxOranga.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isOrangaTamarikiApproved = isChecked
            }

            checkboxDefault.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isDefault = isChecked
            }

            buttonSave.setOnClickListener {
                viewModel.onSaveClick()
            }

            contactImageView.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }

            backButton.setOnClickListener {
                findNavController().navigate(R.id.contactsFragment)
            }

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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addEditContactEvent.collect { event: AddEditContactViewModel.AddEditContactEvent ->
                    when (event) {
                        is AddEditContactViewModel.AddEditContactEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }
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