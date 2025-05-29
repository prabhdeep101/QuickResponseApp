package com.example.quickresponseapp.contacts

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import kotlinx.coroutines.NonDisposableHandle.parent

class ContactsAdapter(
    private val contacts: List<Contact>,
) {

    inner class ContactViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.image_profile)
        val nameText: TextView = itemView.findViewById(R.id.contact_name)
        val defaultLabel: TextView = itemView.findViewById(R.id.contact_default)
        val relationshipText: TextView = itemView.findViewById(R.id.text_title)
        val callButton: LinearLayout = itemView.findViewById(R.id.btn_call)
        val messageButton: LinearLayout = itemView.findViewById(R.id.btn_msg)
    }

}