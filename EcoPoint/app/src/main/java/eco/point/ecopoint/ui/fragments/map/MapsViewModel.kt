package eco.point.ecopoint.ui.fragments.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import eco.point.data.firebase.database.points.FBPointRepository
import eco.point.domain.DataUseCases.points.GetAllPoints
import eco.point.domain.DataUseCases.user.city.GetUserCity
import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.Point
import eco.point.domain.models.enums.DataType
import eco.point.domain.models.enums.PointKeys

class MapsViewModel: ViewModel() {

    companion object{
        val CONTAINERS_VISIBILITY = "containersVisibility"
        val POINTS_VISIBILITY = "pointsVisibility"
    }

    val BIG_TAG = "big"
    val SMALL_TAG = "small"
    private val visibilityMap = hashMapOf(
        PointKeys.PLASTIC.key to true,
        PointKeys.GLASS.key to true,
        PointKeys.METAL.key to true,
        PointKeys.PAPER.key to true,
        PointKeys.FOOD.key to true,
        PointKeys.BATTERY.key to true,
        CONTAINERS_VISIBILITY to true,
        POINTS_VISIBILITY to true
    )

    val pointsList = MutableLiveData<ArrayList<Point>>(ArrayList())
    val containersList = MutableLiveData<ArrayList<Point>>(ArrayList())
    private val allPointsList = MutableLiveData<HashMap<String, Point>>()
    val allVisiblePoints = MutableLiveData<HashMap<String, Point>>()

    private var wasBig = false
    private var wasSmall = false

    fun getPoints(){
        GetAllPoints(FBPointRepository()).execute(
            DataType.SMALL_POINTS,
            object : IFBCallback<ArrayList<Point>>{
            override fun onReceive(data: ArrayList<Point>) {
                wasSmall = true
                containersList.value = data
                if (wasBig && wasSmall)
                    allPointsList.value = getAllPointsMap()
            }
        })
        GetAllPoints(FBPointRepository()).execute(
            DataType.BIG_POINTS,
            object : IFBCallback<ArrayList<Point>>{
            override fun onReceive(data: ArrayList<Point>) {
                wasBig = true
                pointsList.value = data
                if (wasBig && wasSmall) {
                    allPointsList.value = getAllPointsMap()
                    setMarkersVisibility()
                }
            }
        })
    }

    fun getCityLatLng(repository: UserRepository, cities: Array<String>): LatLng{
        return when (GetUserCity(repository).execute()){
            cities[0] -> LatLng(58.010879, 56.226206)
            cities[1] -> LatLng(55.7908, 49.1145)
            cities[2] -> LatLng(55.0, 83.0)
            else -> LatLng(58.010879, 56.226206)
        }
    }

    fun getAllPointsMap(): HashMap<String, Point>{
        val map = HashMap<String, Point>()
        for (point in pointsList.value!!){
            map[BIG_TAG + "_" + point.id] = point
        }
        for (container in containersList.value!!){
            map[SMALL_TAG + "_" + container.id] = container
        }
        return map
    }

    fun setMarkersVisibility(tag: String): Boolean{

        visibilityMap[tag] = !visibilityMap[tag]!!
        setMarkersVisibility()
        return visibilityMap[tag]!!
    }
    private fun setMarkersVisibility(){
        val points = HashMap<String, Point>()
        if (visibilityMap[CONTAINERS_VISIBILITY] == true){
            containersList.value!!.forEach{ container ->
                visibilityMap.forEach { (s, b) ->
                    val id = SMALL_TAG + "_" + container.id
                    if (s != CONTAINERS_VISIBILITY && s != POINTS_VISIBILITY
                        && b && container.tag.contains(s) && !points.containsKey(id))
                        points[id] = container
                }
            }
        }
        if (visibilityMap[POINTS_VISIBILITY] == true){
            pointsList.value!!.forEach{ point ->
                visibilityMap.forEach { (s, b) ->
                    val id = BIG_TAG + "_" + point.id
                    if (s != CONTAINERS_VISIBILITY && s != POINTS_VISIBILITY
                        && b && point.tag.contains(s) && !points.containsKey(id))
                        points[id] = point
                }
            }
        }
//        allPointsList.value!!.forEach { point ->
//            visibilityMap.forEach { visibility ->
//                if (visibility.value && point.value.tag.contains(visibility.key)){
//                    if (!points.containsKey(point.key)){
//                        points[point.key] = point.value
//                    }
//                }
//            }
//        }
        allVisiblePoints.value = points
    }

    fun getPointInfo(address: String): Point?{
        allVisiblePoints.value!!.forEach{ (s, p) ->
            if (p.address == address){
                return p
            }
        }
        return null
    }
}