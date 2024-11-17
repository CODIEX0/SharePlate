package com.example.shareplate.objects

import com.example.shareplate.data.FoodItem
import com.example.shareplate.data.Hotspot
import com.example.shareplate.data.Request
import com.example.shareplate.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseManager {
    private val database = FirebaseDatabase.getInstance()

    // User CRUD Operations
    private val usersRef: DatabaseReference = database.getReference("users")

    fun createUser(user: User, onComplete: (Boolean) -> Unit) {
        user.uid?.let {
            usersRef.child(it).setValue(user).addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
        }
    }

    fun readUsers(callback: (MutableList<User>) -> Unit) {
        val users = mutableListOf<User>()

        // Query the users based on the specified UID
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate over the retrieved data snapshots
                for (snapshot in dataSnapshot.children) {
                    // Retrieve the user object from the snapshot
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        // Add the user to the list
                        users.add(it)
                    }
                }
                // Invoke the callback function with the retrieved users
                callback(users)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                callback(mutableListOf()) // Pass an empty list in case of error
            }
        })
    }

    fun readUser(uid: String, onComplete: (User?) -> Unit) {
        usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onComplete(snapshot.getValue(User::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }

    fun updateUser(uid: String, updatedUser: User, onComplete: (Boolean) -> Unit) {
        usersRef.child(uid).setValue(updatedUser).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun deleteUser(uid: String, onComplete: (Boolean) -> Unit) {
        usersRef.child(uid).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    // Donation CRUD Operations
    private val donationsRef: DatabaseReference = database.getReference("donations")

    fun createDonation(donation: FoodItem, onComplete: (Boolean) -> Unit) {
        donation.id?.let {
            donationsRef.child(it).setValue(donation).addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
        }
    }

    fun readDonation(donationId: String, onComplete: (FoodItem?) -> Unit) {
        donationsRef.child(donationId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onComplete(snapshot.getValue(FoodItem::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }

    fun getDonations(callback: (MutableList<FoodItem>) -> Unit) {
        val foodItems = mutableListOf<FoodItem>()
        // Query all foodItems
        hotspotsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate over the retrieved data snapshots
                for (snapshot in dataSnapshot.children) {
                    // Retrieve the foodItem object from the snapshot
                    val foodItem = snapshot.getValue(FoodItem::class.java)
                    foodItem?.let {
                        // Add the foodItem to the list
                        for (foodItem in foodItems){
                            foodItems.add(it)
                        }
                    }
                }
                // Invoke the callback function with the retrieved foodItems
                callback(foodItems)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                callback(mutableListOf()) // Pass an empty list in case of error
            }
        })
    }

    fun updateDonation(donationId: String, updatedDonation: FoodItem, onComplete: (Boolean) -> Unit) {
        donationsRef.child(donationId).setValue(updatedDonation).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun deleteDonation(donationId: String, onComplete: (Boolean) -> Unit) {
        donationsRef.child(donationId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    // Hotspot CRUD Operations
    private val hotspotsRef: DatabaseReference = database.getReference("hotspots")

    fun createHotspot(hotspot: Hotspot, onComplete: (Boolean) -> Unit) {
        hotspotsRef.child(hotspot.id).setValue(hotspot).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getAllHotspots(callback: (MutableList<Hotspot>) -> Unit) {
        val hotspots = mutableListOf<Hotspot>()
        // Query all hotspots
        hotspotsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate over the retrieved data snapshots
                for (snapshot in dataSnapshot.children) {
                    // Retrieve the hotspot object from the snapshot
                    val hotspot = snapshot.getValue(Hotspot::class.java)
                    hotspot?.let {
                        // Add the hotspot to the list
                        for (hotspot in hotspots){
                            hotspots.add(it)
                        }
                    }
                }
                // Invoke the callback function with the retrieved hotspots
                callback(hotspots)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                callback(mutableListOf()) // Pass an empty list in case of error
            }
        })
    }

    fun readHotspot(id: String, onComplete: (Hotspot?) -> Unit) {
        hotspotsRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onComplete(snapshot.getValue(Hotspot::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }

    fun updateHotspot(id: String, updatedHotspot: Hotspot, onComplete: (Boolean) -> Unit) {
        hotspotsRef.child(id).setValue(updatedHotspot).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun deleteHotspot(id: String, onComplete: (Boolean) -> Unit) {
        hotspotsRef.child(id).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    // Request CRUD Operations
    private val requestsRef: DatabaseReference = database.getReference("requests")

    fun createRequest(request: Request, onComplete: (Boolean) -> Unit) {
        requestsRef.child(request.requestId).setValue(request).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun readRequest(requestId: String, onComplete: (Request?) -> Unit) {
        requestsRef.child(requestId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onComplete(snapshot.getValue(Request::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }

    fun updateRequest(requestId: String, updatedRequest: Request, onComplete: (Boolean) -> Unit) {
        requestsRef.child(requestId).setValue(updatedRequest).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun deleteRequest(requestId: String, onComplete: (Boolean) -> Unit) {
        requestsRef.child(requestId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}

