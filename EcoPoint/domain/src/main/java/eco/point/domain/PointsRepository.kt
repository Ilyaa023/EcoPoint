package eco.point.domain

import eco.point.domain.models.Point
import eco.point.domain.models.enums.DataType
import eco.point.domain.models.enums.PointKeys

abstract class PointsRepository {
    @Deprecated("don't set data")
    open fun setData(key: PointKeys, point: Point) {}
    @Deprecated("don't set data")
    open fun setData(point: Point) {}
    open fun getData(size: DataType, id: String, callback: IFBCallback<Point>) {}
    open fun getData(size: DataType, callback: IFBCallback<ArrayList<Point>>) {}
}