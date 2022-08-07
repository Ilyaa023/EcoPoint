package eco.point.domain.DataUseCases.points

import eco.point.domain.IFBCallback
import eco.point.domain.PointsRepository
import eco.point.domain.models.Point
import eco.point.domain.models.enums.DataType

class GetAllPoints(private val repository: PointsRepository) {
    fun execute(size: DataType, callback: IFBCallback<ArrayList<Point>>){
        repository.getData(
            if (!(size.equals(DataType.SMALL_POINTS) || size.equals(DataType.BIG_POINTS)))
                DataType.SMALL_POINTS else size,
            callback
        )
    }
}