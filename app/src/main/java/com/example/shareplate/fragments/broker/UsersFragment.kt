package com.example.shareplate.fragments.broker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shareplate.R
import com.example.shareplate.adapters.UserAdapter
import com.example.shareplate.data.User
import com.example.shareplate.databinding.FragmentUsersBinding
import com.example.shareplate.objects.FirebaseManager.readUser
import com.example.shareplate.objects.FirebaseManager.readUsers
import com.example.shareplate.objects.Global

class UsersFragment : Fragment(), UserAdapter.OnItemClickListener {

    lateinit var binding: FragmentUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the LinearLayoutManager for the RecyclerView
        val doneeLayoutManager = LinearLayoutManager(requireContext())
        binding.doneeRv.layoutManager = doneeLayoutManager

        // Set up the LinearLayoutManager for the RecyclerView
        val donorLayoutManager = LinearLayoutManager(requireContext())
        binding.donorRv.layoutManager = donorLayoutManager

        // Retrieve updated users from DataManager
        readUsers{ users ->

            for(user in users){
                if(user.userType=="donor"){

                    // Create an instance of UserAdapter and pass the OnItemClickListener
                    val donorAdapter = UserAdapter(Global.users, this)
                    // Set the adapter to the RecyclerView
                    binding.donorRv.adapter = donorAdapter
                }else if(user.userType=="donee"){

                    // Create an instance of UserAdapter and pass the OnItemClickListener
                    val doneeAdapter = UserAdapter(Global.users, this)
                    // Set the adapter to the RecyclerView
                    binding.donorRv.adapter = doneeAdapter
                }
            }
        }
    }

    override fun onItemClick(user: User) {
        // Handle the click event and navigate to a different fragment
        // Add user data to a bundle
        val bundle = Bundle().apply {
            putString("uid", user.uid)
            putString("username", user.username)
            putString("email", user.email)
            putString("userImage", user.userImage)
            putString("userType", user.userType)
            putString("status", user.status)
            putString("createdAt", user.createdAt)
            putString("updatedAt", user.updatedAt)
        }

        // Instantiate the ViewUserFragment and set arguments
        val fragment = UsersFragment().apply {
            arguments = bundle
        }

        // Navigate to the ViewUserFragment, passing the bundle
        findNavController().navigate(R.id.action_navigation_users_to_navigation_update_users, bundle)
    }

    override fun onUpdateClick(user: User) {
        TODO("Not yet implemented")
    }
}
