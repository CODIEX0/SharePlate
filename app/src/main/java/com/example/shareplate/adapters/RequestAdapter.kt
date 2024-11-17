package com.example.shareplate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareplate.R
import com.example.shareplate.data.FoodItem
import com.example.shareplate.data.Hotspot
import com.example.shareplate.data.Request
import com.example.shareplate.data.User
import com.example.shareplate.objects.FirebaseManager
import com.example.shareplate.objects.Global.currentUser
import com.example.shareplate.objects.Global.getCurrentDateTime

class RequestsAdapter(private val requests: List<Request>) : RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val doneeNameTextView: TextView = itemView.findViewById(R.id.name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTime)
        private val foodNameTextView: TextView = itemView.findViewById(R.id.name)
        private val mealTypeTextView: TextView = itemView.findViewById(R.id.mealType)
        private val cuisineTextView: TextView = itemView.findViewById(R.id.cuisine)
        private val acceptRequestButton: Button = itemView.findViewById(R.id.acceptRequestButton)

        fun bind(request: Request) {
            var donee = User("","","","","","", "","","")
            FirebaseManager.readUser(currentUser.uid.toString()){ user->
                if(user != null)
                    donee = user
            }

            var foodItem = FoodItem("","","","", "", arrayListOf(""),0.0,"","","", Hotspot())
            FirebaseManager.readDonation(request.donationId){food->
                if(food != null)
                    foodItem = food
            }
            dateTimeTextView.text = getCurrentDateTime()
            foodNameTextView.text = foodItem.name
            doneeNameTextView.text = "Name: " + donee.username
            quantityTextView.text = "Quantity: " + request.quantity
            mealTypeTextView.text = "Meal Type: " + foodItem.mealType
            cuisineTextView.text = "Cuisine: " + foodItem.cuisine

            acceptRequestButton.setOnClickListener {
                // Handle accept request button click
            }
        }
    }
}