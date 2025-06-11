package com.example.quickresponseapp.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.quickresponseapp.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_editprofile) {
    private val viewModel: ProfileViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImage = view.findViewById<ImageView>(R.id.editProfileImage)
        val nameInput = view.findViewById<EditText>(R.id.editName)
        val addressInput = view.findViewById<EditText>(R.id.editAddress)
        val guardianName = view.findViewById<EditText>(R.id.editGuardianName)
        val guardianPhone = view.findViewById<EditText>(R.id.editGuardianPhone)
        val orangaCheck = view.findViewById<CheckBox>(R.id.editOrangaCheck)
        val updateButton = view.findViewById<Button>(R.id.updateProfileButton)

        // Load existing data (stub for now, replace with your DB read logic)
        // TODO: Fetch from Firebase or local storage and pre-fill inputs

        val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    profileImage.setImageURI(uri)
                }
            }
        }

        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        updateButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val address = addressInput.text.toString().trim()
            val gName = guardianName.text.toString().trim()
            val gPhone = guardianPhone.text.toString().trim()
            val isOranga = orangaCheck.isChecked

            if (name.isEmpty() || address.isEmpty() || gName.isEmpty() || gPhone.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val phoneRegex = Regex("^\\d{8,15}$")
            if (!gPhone.matches(phoneRegex)) {
                Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val profileImagePath = selectedImageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                val file = File(requireContext().filesDir, "${name.replace(" ", "_")}_profile.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                file.absolutePath
            } ?: "default"

            viewModel.saveProfile(
                UserProfile(
                    name = name,
                    address = address,
                    guardianName = gName,
                    guardianPhone = gPhone,
                    orangaAppointed = isOranga,
                    profileImagePath = profileImagePath
                )
            )
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }

        viewModel.getProfile { profile ->
            profile?.let {
                nameInput.setText(it.name)
                addressInput.setText(it.address)
                guardianName.setText(it.guardianName)
                guardianPhone.setText(it.guardianPhone)
                orangaCheck.isChecked = it.orangaAppointed

                if (it.profileImagePath != "default") {
                    val imageFile = File(it.profileImagePath)
                    if (imageFile.exists()) {
                        profileImage.setImageURI(Uri.fromFile(imageFile))
                    }
                }
            }
        }
    }
}