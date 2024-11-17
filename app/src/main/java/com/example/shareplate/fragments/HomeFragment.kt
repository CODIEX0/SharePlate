package com.example.shareplate.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shareplate.R
import com.example.shareplate.databinding.FragmentHomeBinding
import com.example.shareplate.fragments.broker.UpdateUsersFragment
import com.example.shareplate.fragments.donee.AddRequestFragment
import com.example.shareplate.fragments.donor.AddDonationFragment
import com.example.shareplate.fragments.donor.RequestFragment
import com.example.shareplate.objects.Global.currentUser

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.cvAddRequest.setOnClickListener {
            if (currentUser.userType == "donee") {
                replaceFragment(AddRequestFragment())

            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Access Denied")
                    .setMessage("Only Donees have access!")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        binding.cvAddDonation.setOnClickListener {
            if(currentUser.userType == "donor"){
                replaceFragment(AddDonationFragment())
            } else{
            AlertDialog.Builder(requireContext())
                .setTitle("Access Denied")
                .setMessage("Only Donorsrs have access!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            }
        }

        binding.cvDonations.setOnClickListener {

            replaceFragment(DonationFragment())
        }

        binding.cvProfile.setOnClickListener {

            replaceFragment(ProfileFragment())
        }

        binding.cvRequests.setOnClickListener {
            if(currentUser.userType == "donor"){
                replaceFragment(RequestFragment())
            } else{
                AlertDialog.Builder(requireContext())
                    .setTitle("Access Denied")
                    .setMessage("Only Donors have access!")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        binding.cvUpdateUsers.setOnClickListener {
            if (currentUser.userType == "broker") {
                replaceFragment(UpdateUsersFragment())
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Access Denied")
                    .setMessage("Only Brokers have access!")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment) // replace with the new fragment
        fragmentTransaction.addToBackStack(null) //add to back stack
        fragmentTransaction.commit()
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}