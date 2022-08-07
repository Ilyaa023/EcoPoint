package eco.point.ecopoint.ui.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.models.Point
import eco.point.domain.models.enums.PointKeys
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.FragmentMapsBinding


class MapsFragment : Fragment(){
    private val PERMISSION_REQUEST = 1

    private lateinit var binding: FragmentMapsBinding
    private lateinit var vm: MapsViewModel
    private var clusterManagerAllPoints: ClusterManager<PointItem>? = null

    private var map: GoogleMap? = null
    private var activeMarkerLatLng: LatLng? = null
    private var isFineLoc = false
    private var isCoarseLoc = false
    private var isFirst = true
    private var menuState = false

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = false
        uiSettings.isMapToolbarEnabled = false
        uiSettings.isCompassEnabled = false
        vm.getPoints()
        if (isFirst) {
            map!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    vm.getCityLatLng(
                        LocalUserRepository(requireContext()),
                        resources.getStringArray(R.array.Cities)
                    ), 15f
                )
            )
            isFirst = false
        }
        checkPermission()
        initMapButtons()
        vm.allVisiblePoints.observe(viewLifecycleOwner){
            setUpClustererPoints(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this)[MapsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



    private fun setUpClustererPoints(points: HashMap<String, Point>) {
        if (clusterManagerAllPoints != null)
            clusterManagerAllPoints!!.clearItems()
        else {
            clusterManagerAllPoints = ClusterManager(context, map)
            clusterManagerAllPoints!!.renderer = OwnIconRenderer(
                requireContext(), map, clusterManagerAllPoints
            )
            map?.setOnCameraIdleListener(clusterManagerAllPoints)
            clusterManagerAllPoints!!.setOnClusterItemClickListener {
                val point = it.marker.title?.let { it1 -> vm.getPointInfo(it1) }
                if (point != null) {
                    binding.pointTitle.text = point.address
                    binding.pointDescription.text = if (point.description == "")
                        "Здесь будет описание, когда его напишут"
                    else point.description
                    if (point.startTime != Point().startTime || point.startTime != "") {
                        binding.pointHideableLayout.visibility = View.VISIBLE
                        binding.pointStart.text = point.startTime
                        binding.pointEnd.text = point.endTime
                    } else {
                        binding.hideableLayout.visibility = View.GONE
                    }
                    binding.mapPlasticImage.visibility =
                        if (point.tag.contains(PointKeys.PLASTIC.key)) View.VISIBLE else View.GONE
                    binding.mapGlassImage.visibility =
                        if (point.tag.contains(PointKeys.GLASS.key)) View.VISIBLE else View.GONE
                    binding.mapMetalImage.visibility =
                        if (point.tag.contains(PointKeys.METAL.key)) View.VISIBLE else View.GONE
                    binding.mapPaperImage.visibility =
                        if (point.tag.contains(PointKeys.PAPER.key)) View.VISIBLE else View.GONE
                    binding.mapFoodImage.visibility =
                        if (point.tag.contains(PointKeys.FOOD.key)) View.VISIBLE else View.GONE
                    binding.mapBatteryImage.visibility =
                        if (point.tag.contains(PointKeys.BATTERY.key)) View.VISIBLE else View.GONE
                    binding.pointInfoHiderButton.setOnClickListener {
                        binding.pointInfoLayout.animate()
                            .translationY(0.0f)
                            .duration = 300
                    }
                    binding.pointInfoLayout.animate()
                        .translationY(-binding.pointInfoLayout.height.toFloat())
                        .duration = 300
                } else {
                    Toast.makeText(requireContext(), getString(R.string.fail), Toast.LENGTH_SHORT).show()
                }
                activeMarkerLatLng = it.marker.position
                false
            }
        }
        points.forEach {
            if (it.key.contains(vm.BIG_TAG)){
                val item =
                    PointMarker(it.value, requireContext())
                clusterManagerAllPoints!!.addItem(item)
            }
            if (it.key.contains(vm.SMALL_TAG)){
                val item =
                    ContainerMarker(it.value, requireContext())
                clusterManagerAllPoints!!.addItem(item)
            }
        }
        map!!.moveCamera(CameraUpdateFactory.zoomTo(map!!.cameraPosition.zoom - 0.00001f))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initMapButtons(){
        binding.mapMinus.setOnClickListener {
            map!!.animateCamera(CameraUpdateFactory.zoomOut())
        }
        binding.mapPlus.setOnClickListener {
            map!!.animateCamera(CameraUpdateFactory.zoomIn())
        }
        binding.mapFindMe.setOnClickListener {
            checkPermission()
        }
        binding.mapNavigation.setOnClickListener {
            if (activeMarkerLatLng != null) {
                val gmmIntentUri = Uri.parse(
                    "google.navigation:q="
                            + "${activeMarkerLatLng?.latitude}, "
                            + "${activeMarkerLatLng?.longitude}"
                )
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.`package` = "com.google.android.apps.maps"
                startActivity(mapIntent)
            } else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.main_error_navigation),
                    Toast.LENGTH_SHORT
                ).show()
        }

        //binding.hideableLayout.visibility = View.GONE

        binding.hiderButton.setOnClickListener {
            if (menuState){
                //binding.hiderButton.setImageDrawable(requireActivity().getDrawable(android.R.drawable.arrow_down_float))
                binding.hiderView.visibility = View.GONE
                binding.parentOfHideable.animate()
                    .translationY((-binding.hideableLayout.height).toFloat())
                    .setDuration(300)
                    .setListener(null)
            } else {
                //binding.hiderButton.setImageDrawable(requireActivity().getDrawable(android.R.drawable.arrow_up_float))
                binding.hiderView.visibility = View.VISIBLE
                binding.hideableLayout.visibility = View.VISIBLE
                binding.parentOfHideable.animate()
                    .translationY(0.0f)
                    .setListener(null)
                    .duration = 300
            }
            menuState = !menuState
        }

        binding.hiderView.setOnClickListener{
            binding.hiderButton.performClick()
            binding.pointInfoHiderButton.performClick()
        }


        binding.containersButton.setOnClickListener {
            if (vm.setMarkersVisibility(MapsViewModel.CONTAINERS_VISIBILITY)){
                binding.containersButton.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.containersButton.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.containersButton.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_container, null),
                    null, null, null
                )
            } else {
                binding.containersButton.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.containersButton.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.containersButton.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_container_idle, null),
                    null, null, null
                )
            }
        }

        binding.pointsButton.setOnClickListener {
            if (vm.setMarkersVisibility(MapsViewModel.POINTS_VISIBILITY)){
                binding.pointsButton.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.pointsButton.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.pointsButton.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_point_active, null),
                    null, null, null
                )
            } else {
                binding.pointsButton.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.pointsButton.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.pointsButton.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_point_idle, null),
                    null, null, null
                )
            }
        }
        binding.leftTypesArrow.setOnClickListener {
            binding.mapScrollView.fullScroll(View.FOCUS_LEFT)
        }
        binding.rightTypesArrow.setOnClickListener {
            binding.mapScrollView.fullScroll(View.FOCUS_RIGHT)
        }

        binding.buttPlastic.setOnClickListener {
            if (vm.setMarkersVisibility(PointKeys.PLASTIC.key)){
                binding.buttPlastic.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.buttPlastic.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.buttPlastic.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_plastic_active, null),
                    null, null, null
                )
            } else {
                binding.buttPlastic.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.buttPlastic.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.buttPlastic.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_plastic_idle, null),
                    null, null, null
                )
            }
        }
        binding.buttGlass.setOnClickListener {
            if (vm.setMarkersVisibility(PointKeys.GLASS.key)){
                binding.buttGlass.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.buttGlass.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.buttGlass.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_glass_active, null),
                    null, null, null
                )
            } else {
                binding.buttGlass.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.buttGlass.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.buttGlass.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_glass_idle, null),
                    null, null, null
                )
            }
        }
        binding.buttPaper.setOnClickListener {
            if (vm.setMarkersVisibility(PointKeys.PAPER.key)){
                binding.buttPaper.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.buttPaper.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.buttPaper.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_paper_active, null),
                    null, null, null
                )
            } else {
                binding.buttPaper.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.buttPaper.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.buttPaper.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_paper_idle, null),
                    null, null, null
                )
            }
        }
        binding.buttMetal.setOnClickListener {
            if (vm.setMarkersVisibility(PointKeys.METAL.key)){
                binding.buttMetal.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.buttMetal.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.buttMetal.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_metal_active, null),
                    null, null, null
                )
            } else {
                binding.buttMetal.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.buttMetal.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.buttMetal.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_metal_idle, null),
                    null, null, null
                )
            }
        }
        binding.buttFood.setOnClickListener {
            if (vm.setMarkersVisibility(PointKeys.FOOD.key)){
                binding.buttFood.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.buttFood.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.buttFood.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_food_active, null),
                    null, null, null
                )
            } else {
                binding.buttFood.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.buttFood.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.buttFood.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_food_idle, null),
                    null, null, null
                )
            }
        }
        binding.buttBattery.setOnClickListener {
            if (vm.setMarkersVisibility(PointKeys.BATTERY.key)){
                binding.buttBattery.background =
                    resources.getDrawable(R.drawable.background_button_green_little, null)
                binding.buttBattery.setTextColor(resources.getColor(R.color.dark_green, null))
                binding.buttBattery.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_battery_active, null),
                    null, null, null
                )
            } else {
                binding.buttBattery.background =
                    resources.getDrawable(R.drawable.background_button_white_little, null)
                binding.buttBattery.setTextColor(
                    resources.getColor(R.color.green, null)
                )
                binding.buttBattery.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(R.drawable.ic_filter_battery_idle, null),
                    null, null, null
                )
            }
        }
    }

    private fun checkPermission(){
        isFineLoc = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        isCoarseLoc = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!(isFineLoc || isCoarseLoc)) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST)
            map!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    vm.getCityLatLng(
                        LocalUserRepository(requireContext()),
                        resources.getStringArray(R.array.Cities)
                    ), 15f
                )
            )
        }
        moveCamera(true)
        if (isFineLoc) {
            map!!.isMyLocationEnabled = true
            map!!.uiSettings.isMyLocationButtonEnabled = false
            return
        }
        if (isCoarseLoc) {
            map!!.isMyLocationEnabled = true
            map!!.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    private fun moveCamera(zoom: Boolean = true){
        if (isFineLoc) {
            moveCamera(LocationManager.GPS_PROVIDER, zoom)
            return
        }
        if (isCoarseLoc) {
            moveCamera(LocationManager.NETWORK_PROVIDER, zoom)
        }
    }

    @SuppressLint("MissingPermission")
    private fun moveCamera(provider: String, zoom: Boolean){
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val loc = locationManager.getLastKnownLocation(provider)
        if (zoom)
            try {
                map!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            loc!!.latitude, loc.longitude
                        ), 15f
                    )
                )
            } catch (e: Exception){}
        else
            map!!.animateCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    loc!!.latitude,
                    loc.longitude
                )
            )
        )
    }


}