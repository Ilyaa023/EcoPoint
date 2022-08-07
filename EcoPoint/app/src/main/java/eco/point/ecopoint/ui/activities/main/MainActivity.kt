package eco.point.ecopoint.ui.activities.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import eco.point.data.storage.LocalUserRepository
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding

    // TODO: onRequestPermissionsResult and singleton data in fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_main))
    }

//
//    override fun onRequestPermissionsResult(
//            requestCode: Int,
//            permissions: Array<out String>,
//            grantResults: IntArray
//    ){
//        /*if (requestCode != PERMISSION_REQUEST)
//            return*/
//        Log.e("TAG", "onRequestPermissionsResult: ${
//            ActivityCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED}", )
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED) {
//            map!!.isMyLocationEnabled = true
//            map!!.uiSettings.isMyLocationButtonEnabled = false
//            moveCamera(LocationManager.GPS_PROVIDER)
//            return
//        }
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED) {
//            map!!.isMyLocationEnabled = true
//            map!!.uiSettings.isMyLocationButtonEnabled = false
//            moveCamera(LocationManager.NETWORK_PROVIDER)
//            return
//        }
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED)
//            map!!.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    vm.getCityLatLng(
//                        LocalUserRepository(requireContext()),
//                        resources.getStringArray(R.array.Cities)
//                    ), 20f
//                )
//            )
//    }

}