package com.example.shareplate

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.shareplate.data.User
import com.example.shareplate.databinding.ActivityRegisterBinding
import com.example.shareplate.objects.Global
import com.example.shareplate.objects.Global.users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var userDatabaseReference : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ArrayAdapter.createFromResource(
            this.baseContext,
            R.array.userTypes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.UserTypeSpinner.adapter = adapter
        }

        //Sign in user
        binding.txtSignIn.setOnClickListener {
            val signInIntent = Intent(this,LoginActivity::class.java)
            startActivity(signInIntent)
        }
        // Sign up user
        binding.btnSignUp.setOnClickListener {
            // Perform input validation
            performSignUp()
        }

        //rendering the logo to the screen
        val imgIcon : ImageView = findViewById(R.id.imgIcon)
        val drawable = ContextCompat.getDrawable(this, R.mipmap.logo)
        imgIcon.setImageDrawable(drawable)
    }

    private fun clearTextBox(){
        //clear edit text boxes
        binding.etUserName.setText("")
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        binding.etReEnterPassword.setText("")
    }

    private fun performSignUp() {

        val username = binding.etUserName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etReEnterPassword.text.toString()

        // Perform input validation
        if (username == "" || password == "" || confirmPassword == "" || email == "") {
            //clear text
            clearTextBox()
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            //clear passwords
            binding.etPassword.setText("")
            binding.etReEnterPassword.setText("")
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the username is already taken
        val isUsernameTaken = users.any { it.username == username }
        if (isUsernameTaken) {
            binding.etUserName.setText("")
            Toast.makeText(this, "Username Taken!", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the email is already taken
        val isEmailTaken = users.any { it.email == email }
        if (isEmailTaken) {
            binding.etEmail.setText("")
            Toast.makeText(this, "Email Taken!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new User object
        var newUser = User(null, username, password, email,"","","Initial")
        // Add the user to the Global.users list
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid
                    newUser.uid = uid
                    //add user as current user
                    Global.currentUser = newUser

                    userDatabaseReference?.child(uid ?: "")?.setValue(newUser)

                    //navigate the user to the login screen
                    Toast.makeText(this, "Hi ${newUser.username}, you are registered as a ${newUser.userType}!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
