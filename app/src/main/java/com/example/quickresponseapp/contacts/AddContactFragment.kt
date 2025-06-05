package com.example.quickresponseapp.contacts

import android.os.Bundle
import android.view.View
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFragment : Fragment(R.layout.fragment_add_contact) {

    private val viewModel: AddEditContactViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddContactBinding.bind(view)

        binding.apply {
            addContactname.setText(viewModel.contactName)
            addContactphone.setText(viewModel.contactPhone)
            addContactaddress.setText(viewModel.contactAddress)
            addContactrelationship.setText(viewModel.contactRelation)
            checkboxOranga.isChecked = viewModel.isOrangaTamarikiApproved
            checkboxDefault.isChecked = viewModel.isDefault

            addContactname.addTextChangedListener { viewModel.contactName = it.toString() }
            addContactphone.addTextChangedListener { viewModel.contactPhone = it.toString() }
            addContactaddress.addTextChangedListener { viewModel.contactAddress = it.toString() }
            addContactrelationship.addTextChangedListener { viewModel.contactRelation = it.toString() }

            checkboxOranga.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isOrangaTamarikiApproved = isChecked
            }

            checkboxDefault.setOnCheckedChangeListener { _, isChecked ->
                viewModel.isDefault = isChecked
            }

            buttonSave.setOnClickListener {
                viewModel.onSaveClick()
            }

            binding.backButton.setOnClickListener {
                findNavController().navigateUp()
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