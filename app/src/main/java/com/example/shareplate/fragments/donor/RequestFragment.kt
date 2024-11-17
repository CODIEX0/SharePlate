package com.example.shareplate.fragments.donor


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareplate.R
import com.example.shareplate.adapters.RequestsAdapter
import com.example.shareplate.data.Request
import com.example.shareplate.databinding.FragmentRequestBinding
import com.example.shareplate.fragments.donee.AddRequestFragment
import com.example.shareplate.objects.Global
import com.example.shareplate.objects.Global.requests
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RequestFragment : Fragment() {

    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var requestsAdapter: RequestsAdapter
    private lateinit var requestsList: MutableList<Request>
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestBinding.inflate(inflater, container, false)

        if (Global.currentUser.userType != "donor") {
            binding.ibAddRequest.visibility = View.GONE
        }

        binding.ibAddRequest.setOnClickListener {
            replaceFragment(AddRequestFragment())
        }

        requestsRecyclerView = binding.rvRequests
        requestsRecyclerView.layoutManager = LinearLayoutManager(context)
        requestsList = requests
        requestsAdapter = RequestsAdapter(requestsList)
        requestsRecyclerView.adapter = requestsAdapter

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("requests")
        fetchRequests()

        return binding.root
    }

    private fun fetchRequests() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestsList.clear()
                for (requestSnapshot in snapshot.children) {
                    val request = requestSnapshot.getValue(Request::class.java)
                    request?.let { requestsList.add(it) }
                }
                requestsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment) // replace with the new fragment
        fragmentTransaction.addToBackStack(null) //add to back stack
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance() =
            RequestFragment().apply {
            }
    }
}