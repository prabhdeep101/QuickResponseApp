package com.example.quickresponseapp.contacts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val contactDao: ContactDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val contact = state.get<Contact>("contact")

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

    private val addEditContactEventChannel = Channel<AddEditContactEvent>()
    val addEditContactEvent = addEditContactEventChannel.receiveAsFlow()

    fun onSaveClick() {
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

        if (contact != null) {
            val updatedContact = contact.copy(
                name = contactName,
                phone = contactPhone,
                address = contactAddress,
                relation = contactRelation,
                isOrangaTamarikiApproved = isOrangaTamarikiApproved,
                isDefault = isDefault
            )
            updateContact(updatedContact)
        } else {
            val newContact = Contact(
                name = contactName,
                phone = contactPhone,
                address = contactAddress,
                relation = contactRelation,
                isOrangaTamarikiApproved = isOrangaTamarikiApproved,
                isDefault = isDefault
            )
            createContact(newContact)
        }
    }

    fun onDeleteClick(contact: Contact) {
        viewModelScope.launch {
            contactDao.deleteContact(contact)
            addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(DELETE_CONTACT_RESULT_OK))
        }
    }

    private fun createContact(contact: Contact) = viewModelScope.launch {
        contactDao.insertContact(contact)
        addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(ADD_CONTACT_RESULT_OK))
    }

    private fun updateContact(contact: Contact) = viewModelScope.launch {
        contactDao.insertContact(contact)
        addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(EDIT_CONTACT_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditContactEventChannel.send(AddEditContactEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditContactEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditContactEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditContactEvent()
    }

}