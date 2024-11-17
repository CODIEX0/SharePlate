package com.example.shareplate.objects

import android.location.Location
import com.example.shareplate.R
import com.example.shareplate.data.Advice
import com.example.shareplate.data.FoodItem
import com.example.shareplate.data.Hotspot
import com.example.shareplate.data.Request
import com.example.shareplate.data.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions

//Singleton class for storing live data

object Global {

    var users: MutableList<User> = mutableListOf()
    lateinit var location: Location
    lateinit var liveLocation: MarkerOptions

    val mapTypes = listOf(
        GoogleMap.MAP_TYPE_TERRAIN,
        GoogleMap.MAP_TYPE_NORMAL,
        GoogleMap.MAP_TYPE_SATELLITE,
        GoogleMap.MAP_TYPE_HYBRID
    )

    var currentUser: User = User(
        uid = "tyg8g6r36",
        username = "Jack",
        email = "Jack@gmail.com",
        password = "123456",
        userImage = "8723fvt27f36f",
        userType = "donor",
        status = "Initial",
        createdAt =  getCurrentDateTime(),
        updatedAt =  getCurrentDateTime()
    )

    fun getCurrentDateTime(): String {
        // Get the current date and time as a string
        return java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
    }

    //google maps API key
    var googleMapsApiKey = "AIzaSyAbDn3_3FBcBVfgzKsAAmrFJ0Pg8zOqaas"

    val advices = listOf(
        Advice("Practice meal planning to minimize food waste and ensure balanced nutrition. Plan your meals for the week, make a shopping list, and stick to it to avoid buying unnecessary items.", R.mipmap.mealplanning),
        Advice("Plant native crops that require less water and adapt well to local conditions for sustainable farming. Native plants are more resistant to local pests and diseases, reducing the need for chemical interventions.", R.mipmap.nativecrop),
        Advice("Incorporate a variety of colorful vegetables into your meals to boost nutrient intake. Different colors often indicate different nutrients, so eating a rainbow of vegetables can help ensure a balanced diet.", R.mipmap.colorfulveggies),
        Advice("Use natural fertilizers, such as compost or manure, to enrich soil without chemicals. These organic options improve soil structure, increase nutrient content, and promote healthy plant growth.", R.mipmap.fertilizer),
        Advice("Store food properly to extend its shelf life and reduce waste. Use airtight containers for dry goods, and keep fruits and vegetables in the crisper drawer of your refrigerator.", R.mipmap.storefoodproperly),
        Advice("Plan your meals ahead to avoid buying unnecessary items. Create a weekly menu and shopping list to ensure you only purchase what you need.", R.mipmap.planmeals),
        Advice("Use leftovers creatively to make new meals and reduce waste. For example, turn leftover roast chicken into a hearty soup or salad.", R.mipmap.useleftovers),
        Advice("Donate excess food to local food banks or shelters. Many organizations accept non-perishable items and fresh produce.", R.mipmap.donatefood),
        Advice("Compost food scraps to create nutrient-rich soil for gardening. Items like vegetable peels, coffee grounds, and eggshells are great for composting.", R.mipmap.compostfood),
        Advice("Buy in bulk to reduce packaging waste and save money. Store bulk items in reusable containers to keep them fresh.", R.mipmap.buyinbulk),
        Advice("Support local farmers by purchasing fresh, seasonal produce. This helps reduce the carbon footprint associated with transporting food long distances.", R.mipmap.supportlocalfarmers),
        Advice("Educate yourself and others about the importance of food security. Understanding the global food system can help you make more sustainable choices.", R.mipmap.educatefoodsecurity),
        Advice("Grow your own vegetables and herbs to ensure fresh produce. Even a small garden or a few pots on a balcony can provide a steady supply of fresh food.", R.mipmap.growownvegetables),
        Advice("Use a shopping list to avoid impulse purchases and reduce waste. Stick to your list to ensure you only buy what you need.", R.mipmap.useshoppinglist)
    )

    val foodItems = mutableListOf(
        FoodItem("1", "Vegetable Stir-Fry", "Asian", "Dinner","", arrayListOf("Soy", "Gluten"), 2.0, "base64string_vegetablestirfry", "2024-10-29T18:00:00", "2024-10-29T18:00:00", Hotspot("h1", "d1", "2024-10-29T18:00:00", -34.0, 151.0)),
        FoodItem("2", "Lentil Soup", "Mediterranean", "Lunch","", arrayListOf("None"), 1.5, "base64string_lentilsoup", "2024-10-30T12:00:00", "2024-10-30T12:00:00", Hotspot("h2", "d2", "2024-10-30T12:00:00", -34.1, 151.1)),
        FoodItem("3", "Quinoa Salad", "American", "Dinner","", arrayListOf("None"), 3.0, "base64string_quinoasalad", "2024-10-31T18:30:00", "2024-10-31T18:30:00", Hotspot("h3", "d3", "2024-10-31T18:30:00", -34.2, 151.2)),
        FoodItem("4", "Chicken Tacos", "Mexican", "Lunch","", arrayListOf("Dairy", "Gluten"), 2.0, "base64string_chickentacos", "2024-10-29T13:00:00", "2024-10-29T13:00:00", Hotspot("h4", "d4", "2024-10-29T13:00:00", -34.3, 151.3)),
        FoodItem("5", "Beef Stir-Fry", "Asian", "Dinner","", arrayListOf("Soy", "Peanuts"), 2.5, "base64string_beefstirfry", "2024-11-01T19:00:00", "2024-11-01T19:00:00", Hotspot("h5", "d5", "2024-11-01T19:00:00", -34.4, 151.4))
    )


    val requests = mutableListOf(
        Request("req1", "donee1", "1", 1, "2024-10-29T18:00:00"),
        Request("req2", "donee2", "2", 1, "2024-10-30T12:00:00"),
        Request("req3", "donee3", "3", 2, "2024-10-31T18:30:00"),
        Request("req4", "donee4", "4", 1, "2024-10-29T13:00:00"),
        Request("req5", "donee5", "5", 2, "2024-11-01T19:00:00")
    )


}
