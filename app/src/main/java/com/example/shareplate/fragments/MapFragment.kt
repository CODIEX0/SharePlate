package com.example.shareplate.fragments

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shareplate.R
import com.example.shareplate.objects.Global
import com.example.shareplate.data.UserPreferences
import com.example.shareplate.databinding.BslOptionsBinding
import com.example.shareplate.databinding.FragmentMapBinding
import com.example.shareplate.fragments.donor.AddDonationFragment
import com.example.shareplate.objects.FirebaseManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.log
import java.util.Locale

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    lateinit var binding: FragmentMapBinding
    lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var radiusCircle: Circle? = null
    private lateinit var userPreferences: UserPreferences
    private var hotspotsLoaded = false
    private lateinit var latLng: LatLng
    private lateinit var mapTypes: List<Int>
    private var currentMapTypeIndex = 0 // To keep track of the current map type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase and user preferences
        userPreferences = UserPreferences()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and initialize the map fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.SearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchLocation(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mapTypes = Global.mapTypes

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set button listeners
        binding.bd.setOnClickListener{ showBottomDialog() }
        binding.btnRefresh.setOnClickListener { replaceFragment(MapFragment()) }
        binding.btnSatelliteToggle.setOnClickListener { toggleMapType() }
        binding.btnCurrentLocation.setOnClickListener { requestLocationPermissionAndZoom() }


        val collectButton = binding.collectBtn
        collectButton.setOnClickListener {
            //alerting the donors that the user is coming to collect the food
        }

        return binding.root
    }


    private fun showToastMessage(title: String, message: String) {
        Toast.makeText(requireContext(), "$title: $message", Toast.LENGTH_LONG).show()
    }
    private fun showBottomDialog() {
        val binding = BslOptionsBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)

        binding.layoutProfile.setOnClickListener {
            dialog.dismiss()

            replaceFragment(ProfileFragment())
        }

        binding.layoutViewDonations.setOnClickListener {
            dialog.dismiss()

            replaceFragment(DonationFragment())
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.Theme_SharePlate
        dialog.window!!.setGravity(Gravity.BOTTOM)

        dialog.show()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment) // replace with the new fragment
        fragmentTransaction.addToBackStack(null) //add to back stack
        fragmentTransaction.commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener { latLng ->
            // Add a marker at the clicked location
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            passLocationToFragment(latLng)
        }
        mMap.setOnInfoWindowClickListener(this)
        zoomToCurrentLocation()

        // Check location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setupLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun searchLocation(locationName: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val latLng = LatLng(address.latitude, address.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(latLng).title(locationName))
                passLocationToFragment(latLng)
            } else {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun passLocationToFragment(latLng: LatLng) {
        val bundle = Bundle()
        bundle.putDouble("latitude", latLng.latitude)
        bundle.putDouble("longitude", latLng.longitude)
        parentFragmentManager.setFragmentResult("locationRequestKey", bundle)
        val fragment = AddDonationFragment()
        replaceFragment(fragment)
    }

    private fun setupLocationUpdates() {
        // Create location request for high accuracy updates
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 100000 // Update location every 100 seconds
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation ?: return
                latLng = LatLng(location.latitude, location.longitude)
                Global.location = location

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                // Draw radius boundary
                drawRadiusBoundary(location.latitude, location.longitude, userPreferences)

                // Add marker at user's current location
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                mMap.addMarker(markerOptions)

                // Load hotspots if not already loaded
                if (!hotspotsLoaded) {
                    loadHotspots(location.latitude, location.longitude, userPreferences)
                    hotspotsLoaded = true
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun drawRadiusBoundary(latitude: Double, longitude: Double, userPreferences: UserPreferences) {
        // Remove the previous circle if it exists
        radiusCircle?.remove()

        // Get the user's preferences (e.g., maxRadius) from the UserPreferences class
        val localUserPreferences = UserPreferences()

        // Convert maxRadius to meters if unitSystem is "Metric," otherwise use yards
        val maxRadiusInMeters = if (userPreferences.unitSystem == "Metric") {
            userPreferences.maxRadius.toDouble() * 1000 // Convert kilometers to meters
        } else {
            userPreferences.maxRadius.toDouble() * 1760.0 // Convert Miles to yards
        }
        // Create a circle option with the user's selected radius
        val circleOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(maxRadiusInMeters)
            .strokeColor(ContextCompat.getColor(requireContext(), R.color.blue_dark))
            .strokeWidth(2f)
            .fillColor(ContextCompat.getColor(requireContext(), R.color.transparent_blue_dark))

        // Add the circle to the map
        radiusCircle = mMap.addCircle(circleOptions)
    }

    private fun loadHotspots(latitude: Double, longitude: Double, userPreferences: UserPreferences) {
        // Fetch user-specific hotspots and add markers
        FirebaseManager.getDonations {
            val donations = it
            if (donations.isNotEmpty()) {
                requireActivity().runOnUiThread {
                    donations.forEachIndexed { index, donation ->
                        val hotspotLocation = LatLng(donation.location.lat, donation.location.lng)
                        val markerOptions = MarkerOptions()
                            .position(hotspotLocation)
                            .title(donation.name)
                            .snippet(donation.mealType)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        mMap.addMarker(markerOptions)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Couldn't get user's hotspots.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun requestLocationPermissionAndZoom() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            zoomToCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun zoomToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                val zoomLevel = calculateZoomLevel(userLocation, userPreferences.maxRadius.toDouble())
                drawRadiusBoundary(it.latitude, it.longitude, userPreferences)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel))
            }
        }
    }

    private fun toggleMapType() {
        currentMapTypeIndex = (currentMapTypeIndex + 1) % mapTypes.size
        mMap.mapType = mapTypes[currentMapTypeIndex]
    }

    override fun onInfoWindowClick(marker: Marker) {
        getDirections(marker.position)
    }

    private fun getDirections(destination: LatLng) {
        // Use Google Directions API to fetch directions and display the route
        val apiKey = Global.googleMapsApiKey
        val geoApiContext = GeoApiContext.Builder().apiKey(apiKey).build()

        GlobalScope.launch(Dispatchers.IO) {
            val result: DirectionsResult = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin("${Global.location.latitude},${Global.location.longitude}")
                .destination("${destination.latitude},${destination.longitude}")
                .await()

            if (result.routes.isNotEmpty()) {
                val route = result.routes[0]
                val points = PolylineEncoding.decode(route.overviewPolyline.encodedPath)
                val options = PolylineOptions().color(Color.GREEN)
                points.forEach { point -> options.add(LatLng(point.lat, point.lng)) }

                withContext(Dispatchers.Main) {
                    mMap.addPolyline(options)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "No directions found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (::fusedLocationClient.isInitialized && ::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                setupLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateZoomLevel(userLocation: LatLng, radius: Double): Float {
        // Calculate appropriate zoom level based on radius (in kilometers)
        val scale = radius / 500.0 // Adjusting the scale factor based on radius
        return (16 - log(scale) / log(2.0)).toFloat()
    }

    companion object {
        fun newInstance() =
            MapFragment().apply {
            }
    }
}