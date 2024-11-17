package com.example.shareplate.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.example.shareplate.R
import com.example.shareplate.adapters.DonationsAdapter
import com.example.shareplate.data.FoodItem
import com.example.shareplate.databinding.FragmentDonationBinding
import com.example.shareplate.fragments.donee.AddRequestFragment
import com.example.shareplate.fragments.donor.AddDonationFragment
import com.example.shareplate.objects.FirebaseManager.getDonations
import com.example.shareplate.objects.FirebaseManager.readDonation
import com.example.shareplate.objects.Global.currentUser
import com.example.shareplate.objects.Global.foodItems

class DonationFragment : Fragment(), DonationsAdapter.OnDonationClickListener {

    private lateinit var donationsRecyclerView: RecyclerView
    private lateinit var donationsAdapter: DonationsAdapter
    private lateinit var donationsList: MutableList<FoodItem>
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentDonationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationBinding.inflate(inflater, container, false)

        if (currentUser.userType != "donee") {
            binding.ibAddDonation.visibility = View.GONE
        }

        binding.ibAddDonation.setOnClickListener {
            replaceFragment(AddDonationFragment())
        }

        var donations = mutableListOf<FoodItem>()
        // Initialize the RecyclerView
        donationsRecyclerView = binding.rvDonations
        donationsRecyclerView.layoutManager = LinearLayoutManager(context)

        for(item in foodItems){
            item.id?.let { donation ->
                readDonation(donation) { foodItem ->
                    foodItem?.let { donations.add(it) }
                }
            }
        }

        donationsList = foodItems

        getDonations {

            if (it != null) {
                donationsList = it
            } else {
                 for(item in foodItems){
                     item.id?.let { id ->
                         readDonation(id) { donation ->
                             donation?.let { donations.add(it) }
                         }
                     }
                 }

            }
        }
          //foodItems // Initialize the list here

        // Initialize the adapter with the listener
        donationsAdapter = DonationsAdapter(donationsList, this)
        donationsRecyclerView.adapter = donationsAdapter

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("donations")
        fetchDonations()

        return binding.root
    }

    private fun fetchDonations() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationsList.clear()
                for (donationSnapshot in snapshot.children) {
                    val donation = donationSnapshot.getValue(FoodItem::class.java)
                    donation?.let { donationsList.add(it) }
                }
                donationsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
    override fun onRequestClick(foodItem: FoodItem) {

        if (currentUser.userType == "donee") {
            // Create a confirmation dialog
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Request")
                .setMessage("Are you sure you want to request this item: ${foodItem.name}?")
                .setPositiveButton("Yes") { dialog, which ->
                    val bundle = Bundle().apply {
                        putString("id", foodItem.id)
                        putString("donorId", foodItem.donorId)
                        putString("name", foodItem.name)
                        putString("cuisine", foodItem.cuisine)
                        putString("mealType", foodItem.mealType)
                        putStringArrayList("allergens", foodItem.allergens)
                        putDouble("quantity", foodItem.quantity)
                        putString("picture", foodItem.picture)
                        putString("pickupTime", foodItem.pickupTime)

                    }

                    try{
                        val fragment = AddRequestFragment()
                        fragment.arguments = bundle

                        // User confirmed, navigate to AddRequestFragment
                        findNavController().navigate(R.id.action_donationFragment_to_addRequestFragment, bundle)
                    }catch (e:Exception){
                        Toast.makeText(activity,e.message, Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, e.message.toString())
                    }
                }
                .setNegativeButton("No") { dialog, which ->
                    // User cancelled the dialog
                    dialog.dismiss()
                }
                .show()
        }else{
            AlertDialog.Builder(requireContext())
                .setTitle("Access Denied")
                .setMessage("Only donees can request for donations!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment) // replace with the new fragment
        fragmentTransaction.addToBackStack(null) //add to back stack
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance() =
            DonationFragment().apply {
            }
    }
}
