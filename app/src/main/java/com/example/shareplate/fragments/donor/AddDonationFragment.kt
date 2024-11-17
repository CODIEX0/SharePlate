package com.example.shareplate.fragments.donor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.avianwatch.objects.Image
import com.example.shareplate.R
import com.example.shareplate.data.FoodItem
import com.example.shareplate.data.Hotspot
import com.example.shareplate.databinding.FragmentAddDonationBinding
import com.example.shareplate.objects.FirebaseManager
import com.example.shareplate.objects.Global.currentUser
import com.example.shareplate.objects.Global.getCurrentDateTime
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.UUID

class AddDonationFragment : Fragment() {
    private lateinit var binding: FragmentAddDonationBinding
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddDonationBinding.inflate(inflater, container, false)

        binding.btnCamera.setOnClickListener {
            captureImage()
        }

        // Set up the spinners with sample data
        val mealTypes = arrayOf("Breakfast", "Lunch", "Dinner")
        val cuisines = arrayOf("Italian", "Chinese", "Indian")
        val allergens = arrayOf("None", "Peanuts", "Gluten")

        binding.mealTypeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealTypes)
        binding.cuisineSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cuisines)
        binding.allergensSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, allergens)

        binding.btnDonate.setOnClickListener {
            val name = binding.etItemName.text.toString()
            val quantity = binding.etQuantity.text.toString().toDoubleOrNull() ?: 0.0
            val mealType = binding.mealTypeSpinner.selectedItem.toString()
            val cuisine = binding.cuisineSpinner.selectedItem.toString()
            val allergen = binding.allergensSpinner.selectedItem.toString()
            val pickUpDate = binding.txtPickupDateTime.text.toString()
            val expiryDate = binding.txtExpiryDateTime.text.toString()
            val picture = binding.imgDonationImage
            val uid = UUID.randomUUID().toString()

            if (name.isNotEmpty() && quantity > 0 && selectedLatitude != null && selectedLongitude != null) {
                val newDonation = FoodItem(
                    id = uid,
                    donorId = currentUser.uid,
                    name = name,
                    cuisine = cuisine,
                    mealType = mealType,
                    allergens = arrayListOf(allergen),
                    quantity = quantity,
                    picture = Image.convertImageToBase64(picture),
                    pickupTime = pickUpDate,
                    expiryDate = expiryDate,
                    location = Hotspot(UUID.randomUUID().toString(), uid, getCurrentDateTime(), selectedLatitude!!, selectedLongitude!!),
                    collected = false
                )

                FirebaseManager.createDonation(newDonation) { isSuccessful ->
                    if (isSuccessful) {
                        Toast.makeText(requireContext(), "Donation added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to add donation", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        setFragmentResultListener("locationRequestKey") { _, bundle ->
            selectedLatitude = bundle.getDouble("latitude")
            selectedLongitude = bundle.getDouble("longitude")
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val img = data?.data!!
                binding.imgDonationImage.setImageURI(img)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }catch(ex:Exception){
            Toast.makeText(activity, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun captureImage(){
        try{
            ImagePicker.with(this)
                .crop()                     //crop image(optional), check customization for more options
                .compress(1024)             //final image size will be less than 1 MB
                .maxResultSize(1080,1080)   //final image resolution will be less than 1080 x 1080
                .start()
        }catch(ex: Exception){
            Toast.makeText(activity, ex.message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun newInstance() =
            AddDonationFragment().apply {
            }
    }
}