package eco.point.data.firebase.database.points

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import eco.point.domain.IFBCallback
import eco.point.domain.PointsRepository
import eco.point.domain.models.Point
import eco.point.domain.models.enums.DataType
import eco.point.domain.models.enums.PointKeys

class FBPointRepository: PointsRepository() {
    val points = FirebaseDatabase.getInstance(
        "https://my-ecopoint-project-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(DataType.POINTS.key)

    /*override fun setData(point: Point) {
        val pointsToSet = points.child(point.size.key)
        pointsToSet.child(point.id).child(PointKeys.ADDRESS.key).setValue(point.address)
        pointsToSet.child(point.id).child(PointKeys.DESCRIPTION.key).setValue(point.description)
        pointsToSet.child(point.id).child(PointKeys.ORGANIZATION.key).setValue(point.organization)
        pointsToSet.child(point.id).child(PointKeys.TAG.key).setValue(point.tag)
        pointsToSet.child(point.id).child(PointKeys.LATITUDE.key).setValue(point.latitude)
        pointsToSet.child(point.id).child(PointKeys.LONGITUDE.key).setValue(point.longitude)
    }

    override fun setData(key: PointKeys, point: Point) {
        val data = when(key){
            PointKeys.ADDRESS -> point.address
            PointKeys.DESCRIPTION  -> point.description
            PointKeys.ORGANIZATION -> point.organization
            PointKeys.TAG -> point.tag
            PointKeys.LATITUDE -> point.latitude
            PointKeys.LONGITUDE -> point.longitude
            else -> null
        }
        points.child(point.size.key).child(point.id).child(key.key).setValue(data)
    }*/

    override fun getData(size: DataType, callback: IFBCallback<ArrayList<Point>>) {
        points.child(size.key).get().addOnCompleteListener {
            if (it.isSuccessful){
                val res = it.result
                val pointsList = ArrayList<Point>()
                for (snapshot: DataSnapshot in res.children){
                    pointsList.add(Point(
                        id = snapshot.key.toString(),
                        address = snapshot.child(PointKeys.ADDRESS.key).value.toString(),
                        description = snapshot.child(PointKeys.DESCRIPTION.key).value.toString(),
                        organization = snapshot.child(PointKeys.ORGANIZATION.key).value.toString(),
                        tag = snapshot.child(PointKeys.TAG.key).value.toString(),
                        latitude =
                        snapshot.child(PointKeys.LATITUDE.key).value.toString().toDouble(),
                        longitude =
                        snapshot.child(PointKeys.LONGITUDE.key).value.toString().toDouble(),
                        startTime = snapshot.child(PointKeys.START_TIME.key).value.toString(),
                        endTime = snapshot.child(PointKeys.END_TIME.key).value.toString(),
                        size = size
                    ))
                }
                callback.onReceive(pointsList)
            }else Log.e(this.javaClass.name, "getData: ${it.exception}")
        }
    }

    override fun getData(size: DataType, id: String, callback: IFBCallback<Point>) {
        points.child(size.key).child(id).get().addOnCompleteListener {
            if (it.isSuccessful){
                    callback.onReceive(Point(
                        id = it.result.key.toString(),
                        address = it.result.child(PointKeys.ADDRESS.key).value.toString(),
                        description = it.result.child(PointKeys.DESCRIPTION.key).value.toString(),
                        organization = it.result.child(PointKeys.ORGANIZATION.key).value.toString(),
                        tag = it.result.child(PointKeys.TAG.key).value.toString(),
                        latitude =
                        it.result.child(PointKeys.LATITUDE.key).value.toString().toDouble(),
                        longitude =
                        it.result.child(PointKeys.LONGITUDE.key).value.toString().toDouble(),
                        startTime = it.result.child(PointKeys.TAG.key).value.toString(),
                        endTime = it.result.child(PointKeys.END_TIME.key).value.toString(),
                        size = size
                    ))
            }else Log.e(this.javaClass.name, "getData: ${it.exception}")
        }
    }
}