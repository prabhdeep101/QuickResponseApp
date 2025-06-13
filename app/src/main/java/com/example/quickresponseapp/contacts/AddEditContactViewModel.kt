package com.example.quickresponseapp.contacts

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    // Access to the database operations
    private val contactDao: ContactDao,
    // Retain UI state
    private val state: SavedStateHandle
) : ViewModel() {

    // Retrieve existing contact from save if editing
    val contact = state.get<Contact>("contact")
    // Holds URI for contact image
    var contactImageUri: String? = null

    // Fields bound to input from user and saved state if editing
    var contactName = state.get<String>("contactName") ?: contact?.name ?: ""
        set(value) {
            field = value
            state.set("contactName", value)
        }

    var contactPhone = state.get<String>("contactPhone") ?: contact?.phone ?: ""
        set(value) {
            field = value
            state.set("contactPhone", value)
        }
    var contactAddress = state.get<String>("contactAddress") ?: contact?.address ?: ""
        set(value) {
            field = value
            state.set("contactAddress", value)
        }

    var contactRelation = state.get<String>("contactRelation") ?: contact?.relation ?: ""
        set(value) {
            field = value
            state.set("contactRelation", value)
        }

    var isOrangaTamarikiApproved = state.get<Boolean>("isOrangaTamarikiApproved") ?: contact?.isOrangaTamarikiApproved ?: false
        set(value) {
            field = value
            state.set("isOrangaTamarikiApproved", value)
        }

    var isDefault = state.get<Boolean>("isDefault") ?: contact?.isDefault ?: false
        set(value) {
            field = value
            state.set("isDefault", value)
        }

    // Channel for sending events
    private val addEditContactEventChannel = Channel<AddEditContactEvent>()
    val addEditContactEvent = addEditContactEventChannel.receiveAsFlow()

    // Called when the save button is clicked
    fun onSaveClick() {
        // Validate the fields (make sure there is input in name, phone, and relation)
        if (contactName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if (contactPhone.isBlank()) {
            showInvalidInputMessage("Phone number cannot be empty")
            return
        }

        if (contactRelation.isBlank()) {
            showInvalidInputMessage("Relationship cannot be empty")
            return
        }

        // If editing, update existing contact
        if (contact != null) {
            val updatedContact = contact.copy(
                name = contactName,
                phone = contactPhone,
                address = contactAddress,
                relation = contactRelation,
                photoUri = contactImageUri,
                isOrangaTamarikiApproved = isOrangaTamarikiApproved,
                isDefault = isDefault
            )
            updateContact(updatedContact)
        } else {
            val newContact = Contact(
                // Otherwise create a new contact
                name = contactName,
                phone = contactPhone,
                address = contactAddress,
                relation = contactRelation,
                photoUri = contactImageUri,
                isOrangaTamarikiApproved = isOrangaTamarikiApproved,
                isDefault = isDefault
            )
            createContact(newContact)
        }
    }

    // Save selected image to internal storage
    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "contact_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Delete contact and navigate back to contacts screen
    fun onDeleteClick(contact: Contact) {
        viewModelScope.launch {
            contactDao.deleteContact(contact)
            addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(DELETE_CONTACT_RESULT_OK))
        }
    }

    // Insert new contact and navigate back to contacts page
    private fun createContact(contact: Contact) = viewModelScope.launch {
        contactDao.insertContact(contact)
        addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(ADD_CONTACT_RESULT_OK))
    }

    // Update the existing contact and navigate back to contacts page
    private fun updateContact(contact: Contact) = viewModelScope.launch {
        contactDao.insertContact(contact)
        addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(EDIT_CONTACT_RESULT_OK))
    }

    // Send the invalidation message to the user
    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditContactEventChannel.send(AddEditContactEvent.ShowInvalidInputMessage(text))
    }

    // Event class to communicate with user interface
    sealed class AddEditContactEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditContactEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditContactEvent()
    }

}