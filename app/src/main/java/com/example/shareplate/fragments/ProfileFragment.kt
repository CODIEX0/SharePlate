package com.example.shareplate.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.avianwatch.objects.Image.convertImageToBase64
import com.example.shareplate.R
import com.example.shareplate.data.User
import com.example.shareplate.databinding.FragmentProfileBinding
import com.example.shareplate.objects.FirebaseManager
import com.example.shareplate.objects.FirebaseManager.deleteUser
import com.example.shareplate.objects.FirebaseManager.readUser
import com.example.shareplate.objects.FirebaseManager.readUsers
import com.example.shareplate.objects.FirebaseManager.updateUser
import com.example.shareplate.objects.Global
import com.example.shareplate.objects.Global.currentUser
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.etUserName.setText(currentUser.username)
        binding.txtUserType.text = currentUser.userType
        binding.txtUserStatus.text = currentUser.status
        binding.etPassword.setText(currentUser.password)

        binding.txtViewPassword.setOnClickListener { performOnClick(it) }

        if (currentUser.userType != "broker") {
            binding.btnDelete.visibility = View.GONE
        }

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


        binding.btnDelete.setOnClickListener {

            readUser(currentUser.uid.toString()){user ->
                if (user != null) {
                    deleteUser(user.uid.toString()){deleted ->
                        if(deleted)
                            Toast.makeText(requireContext(), "Current profile deleted", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
        return binding.root
    }

    private fun clearTextBox(){
        //clear edit text boxes
        binding.etUserName.setText("")
        binding.etPassword.setText("")
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgUserPicture.setImageBitmap(imageBitmap)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

        binding.imgUserPicture.setImageURI(data?.data)
    }

    fun updateUser(){
        val username = currentUser.username
        val email = currentUser.email
        val password = currentUser.password
        val imageData = currentUser.userImage


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
        val updatedUser = User(currentUser.uid, username, password, email,imageData,currentUser.userType,currentUser.status)

        updateUser(currentUser.uid.toString(),updatedUser){
            if(it){
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Error updating profile!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        fun newInstance() =
            ProfileFragment().apply {
            }
    }
}