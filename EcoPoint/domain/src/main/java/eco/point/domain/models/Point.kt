package eco.point.domain.models

import eco.point.domain.models.enums.DataType
import java.util.*

class Point (
        var id: String = "defPID_${GregorianCalendar().timeInMillis}",
        var address: String = "None address",
        var description: String = "None description",
        var organization: String = "None organization",
        var tag: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var startTime: String = "None",
        var endTime: String = "None",
        size: DataType = DataType.SMALL_POINTS
        ){
    var size = if (!(size.equals(DataType.SMALL_POINTS) || size.equals(DataType.BIG_POINTS)))
        DataType.SMALL_POINTS else size
    set(value) {
        if (!(value.equals(DataType.SMALL_POINTS) || value.equals(DataType.BIG_POINTS)))
            field = DataType.SMALL_POINTS
        else
            field = value
    }
}