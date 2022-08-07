package eco.point.domain.models

import java.util.*
import kotlin.collections.ArrayList

class Announcement(
        val id: String = "defAId_${GregorianCalendar().timeInMillis}",
        var title: String = "None Title",
        var dateTime: GregorianCalendar = GregorianCalendar(),
        val creator: User = User(),
        val participant: Int = 0,
        var description: String = "None description",
        var tag: String = "",
        var complaints: ArrayList<Complaint> = ArrayList(),
        var complaintsCounter: Long = 0,
        var vkLink: String? = null,
        var tgLink: String? = null,
        var telephone: String? = null,
        var eMail: String? = null,
        val banned: Boolean = false,
        val cost: Double = 0.0,
        val units: String = ""
) {
    companion object{
        val SELLER = 1
        val BUYER = 2
    }
}