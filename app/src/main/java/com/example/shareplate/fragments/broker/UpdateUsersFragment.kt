package com.example.shareplate.fragments.broker

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shareplate.R
import com.example.shareplate.data.User
import com.example.shareplate.databinding.FragmentUpdateUsersBinding
import com.example.shareplate.objects.FirebaseManager
import com.example.shareplate.objects.Global
import com.github.dhaval2404.imagepicker.ImagePicker


class UpdateUsersFragment : Fragment() {
    lateinit var binding: FragmentUpdateUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateUsersBinding.inflate(inflater, container, false)

        binding.etUserName.setText(Global.currentUser.username)
        binding.userType.text = Global.currentUser.userType
        //binding. .text = Global.currentUser.status
        binding.etPassword.setText(Global.currentUser.password)

        binding.txtViewPassword.setOnClickListener { performOnClick(it) }


        binding.imgUserPicture.setOnClickListener{
            ImagePicker.with(this)
                .crop()                     //crop image(optional), check customization for more options
                .compress(1024)             //final image size will be less than 1 MB
                .maxResultSize(1080,1080)   //final image resolution will be less than 1080 x 1080
                .start()
        }


        binding.btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .crop()                     //crop image(optional), check customization for more options
                .compress(1024)             //final image size will be less than 1 MB
                .maxResultSize(1080,1080)   //final image resolution will be less than 1080 x 1080
                .start()
        }

        binding.btnUpdate.setOnClickListener{
            updateUser()
        }

        return binding.root
    }

    private fun clearTextBox(){
        //clear edit text boxes
        binding.etUserName.setText("")
        binding.etPassword.setText("")
    }

    fun updateUser(){
        val username = Global.currentUser.username
        val email = Global.currentUser.email
        val password = Global.currentUser.password
        val imageData = Global.currentUser.userImage


        // Perform input validation
        if (username == "" || password == "" || email == "") {
            //clear text
            clearTextBox()
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != null) {
            if (password.length < 6) {
                //clear passwords
                binding.etPassword.setText("")
                Toast.makeText(requireContext(), "Password should have greater than 6 characters", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Check if the username is already taken
        val isUsernameTaken = Global.users.any { it.username == username }
        if (isUsernameTaken) {
            binding.etUserName.setText("")
            Toast.makeText(requireContext(), "Username Taken!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new User object
        val updatedUser = User(
            Global.currentUser.uid, username, password, email,imageData,
            Global.currentUser.userType,
            Global.currentUser.status)

        FirebaseManager.updateUser(Global.currentUser.uid.toString(), updatedUser) {
            if (it) {
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error updating profile!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun performOnClick(view: View) {
        // Toggle password visibility when the user clicks the ImageView
        val currentInputType = binding.etPassword.inputType

        // Check if the current input type is for a password
        if (currentInputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Make the password visible
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            view.setBackgroundResource(R.mipmap.visible)
        } else {
            // Hide the password
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            view.setBackgroundResource(R.mipmap.invisible)
        }
    }
}