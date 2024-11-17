package com.example.shareplate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareplate.R
import com.example.shareplate.data.Donor
import com.example.shareplate.data.FoodItem
import com.squareup.picasso.Picasso

class DonationsAdapter(
    private val donationsList: MutableList<FoodItem>,
    private val listener: OnDonationClickListener
) : RecyclerView.Adapter<DonationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donation, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = donationsList[position]
        holder.bind(donation, listener) // Pass the listener to the bind method
    }

    override fun getItemCount(): Int {
        return donationsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val mealTypeTextView: TextView = itemView.findViewById(R.id.mealTypeTextView)
        private val cuisineTextView: TextView = itemView.findViewById(R.id.cuisineTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val requestButton: Button = itemView.findViewById(R.id.requestButton)
        private val img: ImageView = itemView.findViewById(R.id.img)

        fun bind(donation: FoodItem, listener: OnDonationClickListener) { // Accept listener as parameter
            nameTextView.text = "Name: ${donation.name}"
            mealTypeTextView.text = "Meal Type: ${donation.mealType}"
            cuisineTextView.text = "Cuisine: ${donation.cuisine}"
            quantityTextView.text = "Quantity: ${donation.quantity}"

            // Load the image using Picasso
            Picasso.get()
                .load(donation.picture)
                .placeholder(R.mipmap.fooditem)
                .error(R.mipmap.logo)
                .into(img)

            // Set up click listener to trigger the navigation
            requestButton.setOnClickListener {
                listener.onRequestClick(donation) // Call the onRequestClick method
            }
        }
    }

    interface OnDonationClickListener {
        fun onRequestClick(foodItem: FoodItem)
    }
}

