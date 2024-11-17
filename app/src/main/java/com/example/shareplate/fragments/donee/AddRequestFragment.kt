package com.example.shareplate.fragments.donee

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shareplate.data.FoodItem
import com.example.shareplate.data.Request
import com.example.shareplate.databinding.FragmentAddRequestBinding
import com.example.shareplate.objects.Global.currentUser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddRequestFragment : Fragment() {

    private var _binding: FragmentAddRequestBinding? = null
    private val binding get() = _binding!!

    // Donation data to populate the view
    private var donation: FoodItem? = null
    private var selectedPickupDateTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRequestBinding.inflate(inflater, container, false)

        // Retrieve the donation data from arguments
        val arguments = arguments

        // Check if arguments exist
        if (arguments != null) {
            // Retrieve the data from the bundle
            val id = arguments?.getString("id")
            val donorId = arguments?.getString("donorId")
            val name = arguments?.getString("name")
            val cuisine = arguments?.getString("cuisine")
            val mealType = arguments?.getString("mealType")
            val allergens = arguments?.getStringArrayList("allergens")
            val quantity = arguments?.getDouble("quantity")
            val picture = arguments?.getString("picture")
            val pickupTime = arguments?.getString("pickupTime")

            // Update UI elements
            binding.txtfoodName.text = "Item Name: $name"
            binding.txtmeatType.text = "Meal Type: $mealType"
            binding.txtAllergens.text = "Allergens: ${allergens?.joinToString(", ")}"
            binding.txtQuantity.text = "Quantity: $quantity"
            binding.txtTimePosted.text = "Time Posted: $pickupTime"
        }

        // Populate donation details in views
        donation?.let { populateDonationDetails(it) }

        // Set up button click for date and time picker
        binding.btnSelectPickupDateTime.setOnClickListener { showDateTimePicker() }

        // Handle request button click
        binding.btnRequest.setOnClickListener { requestFood() }

        return binding.root
    }

    private fun populateDonationDetails(donation: FoodItem) {
        binding.txtfoodName.text = "Item Name: ${donation.name}"
        binding.txtmeatType.text = "Meal Type: ${donation.mealType}"
        binding.txtAllergens.text = "Allergens: ${donation.allergens?.joinToString(", ")}"
        binding.txtQuantity.text = "Quantity: ${donation.quantity}"
        binding.txtTimePosted.text = "Time Posted: ${donation.pickupTime}"
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Show time picker after date is selected
                val timePicker = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        // Format date and time
                        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        selectedPickupDateTime = dateTimeFormat.format(calendar.time)
                        binding.txtPickupDateTime.text = "Pickup Date and Time: $selectedPickupDateTime"
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePicker.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Limit date selection to today through next 7 days
        datePicker.datePicker.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        datePicker.datePicker.maxDate = calendar.timeInMillis
        datePicker.show()
    }

    private fun requestFood() {
        val requestedQuantity = binding.etQuantity.text.toString().toIntOrNull()

        if (requestedQuantity == null || requestedQuantity <= 0 || selectedPickupDateTime.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Please enter a valid quantity and pick a date & time", Toast.LENGTH_SHORT).show()
            return
        }

        // Creating new request
        val newRequest = Request(
            requestId = UUID.randomUUID().toString(),
            doneeUid = currentUser.uid.toString(), // Replace with actual user ID
            donationId = donation?.id ?: "",
            quantity = requestedQuantity,
            pickupTime = selectedPickupDateTime!!
        )

        // Send request (e.g., save to database)
        saveRequestToDatabase(newRequest)
    }

    private fun saveRequestToDatabase(request: Request) {
        // Placeholder for saving logic (Firebase, API, etc.)
        // Firebase.database.getReference("requests").child(request.requestId).setValue(request)

        Toast.makeText(requireContext(), "Request sent successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
