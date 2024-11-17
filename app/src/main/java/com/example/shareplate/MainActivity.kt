package com.example.shareplate

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.shareplate.databinding.ActivityMainBinding
import com.example.shareplate.fragments.AdviceFragment
import com.example.shareplate.fragments.DonationFragment
import com.example.shareplate.fragments.HomeFragment
import com.example.shareplate.fragments.MapFragment
import com.example.shareplate.fragments.ProfileFragment
import com.example.shareplate.fragments.donor.RequestFragment
import com.example.shareplate.objects.Global.currentUser

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding

    val permissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val permissionCode = 78

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request for permissions
        if (!allPermissionsGranted()) {
            askForPermission()
        }

        val bottomBar = findViewById<BottomNavigationView>(R.id.bv_bar)

        // Inflate the relevant menu based on user type



        //bottomBar.inflateMenu(R.menu.bottom_nav_menu)
        bottomBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_donations -> inflateFragment(DonationFragment.newInstance())
                R.id.nav_home -> inflateFragment(HomeFragment.newInstance())
                R.id.nav_advice -> inflateFragment(AdviceFragment.newInstance())
                R.id.nav_directions -> inflateFragment(MapFragment.newInstance())
                R.id.nav_profile -> inflateFragment(ProfileFragment.newInstance())
            }
            true
        }
        bottomBar.selectedItemId = R.id.nav_home
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    private fun inflateFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionCode) { // Replace with your request code
            // Check if context is non-null before calling `canDrawOverlays`
            val context = this@MainActivity ?: return  // Use `applicationContext` or `this`

            if (Settings.canDrawOverlays(context)) {
                // Permission granted; continue with your logic
            } else {
                // Permission denied; handle accordingly
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}