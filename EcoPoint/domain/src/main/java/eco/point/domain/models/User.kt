package eco.point.domain.models

import java.util.*

class User(
        var visited: Boolean = false,
        var skiped: Boolean = false,
        var id: String = "",
        var name: String = "",
        var city: String = "",
        var birth: GregorianCalendar = GregorianCalendar(),
        var bonuses: Int = 0,
        var garbageCounter: Int = 0,
        var level: Int = 0,
        var status: Int = 0,
        var rating: Int = 0,
        val banned: Boolean = false
) {
}