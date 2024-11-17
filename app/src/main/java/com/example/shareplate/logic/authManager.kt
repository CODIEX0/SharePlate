package com.example.shareplate.logic

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthManager {
    private val mAuth: FirebaseAuth

    init {
        mAuth = FirebaseAuth.getInstance()
    }

    fun registerUser(
        email: String?,
        password: String?,
        userType: String?,
        listener: OnCompleteListener<AuthResult?>?
    ) {
        mAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(listener!!)
    }

    fun loginUser(email: String?, password: String?, listener: OnCompleteListener<AuthResult?>?) {
        mAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(listener!!)
    }

    fun logoutUser() {
        mAuth.signOut()
    }

    val currentUser: FirebaseUser?
        get() = mAuth.currentUser
}