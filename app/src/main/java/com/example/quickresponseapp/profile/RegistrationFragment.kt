package com.example.quickresponseapp.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quickresponseapp.R
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var profileImageView: CircleImageView
    private var selectedImagePath: String? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            profileImageView.setImageURI(it)
            selectedImagePath = it.toString() // or convert to file path if needed
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val birthdayInput = view.findViewById<EditText>(R.id.birthdayInput)
        val addressInput = view.findViewById<EditText>(R.id.addressInput)
        val guardianName = view.findViewById<EditText>(R.id.guardianName)
        val guardianPhone = view.findViewById<EditText>(R.id.guardianPhone)
        val orangaCheck = view.findViewById<CheckBox>(R.id.orangaCheck)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        profileImageView = view.findViewById(R.id.profileImage)

        profileImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        birthdayInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                // Format selected date as DD/MM/YYYY
                val formatted = String.format("%02d/%02d/%04d", d, m + 1, y)
                birthdayInput.setText(formatted)
            }, year, month, day)

            datePicker.show()
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val address = addressInput.text.toString().trim()
            val caregiverName = guardianName.text.toString().trim()
            val caregiverPhone = guardianPhone.text.toString().trim()
            val isOranga = orangaCheck.isChecked

            val birthdayStr = birthdayInput.text.toString().trim()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthday: Date? = try {
                dateFormat.parse(birthdayStr)
            } catch (e: Exception) {
                null
            }

            if (name.isEmpty() || birthdayStr.isEmpty() || birthday == null || address.isEmpty() ||
                caregiverName.isEmpty() || caregiverPhone.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields with valid input",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val profile = UserProfile(
                id = 0,
                name = name,
                birthday = birthday,
                address = address,
                guardianName = caregiverName,
                guardianPhone = caregiverPhone,
                orangaAppointed = isOranga,
                profileImagePath = selectedImagePath ?: "default"
            )

            viewModel.saveProfile(profile)

            Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.homeScreenFragment)
        }
    }
}