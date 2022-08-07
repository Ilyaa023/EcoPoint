package eco.point.domain.models

import java.util.*

class Complaint(
        val cId: String = "defCId_${GregorianCalendar().timeInMillis}",
        val cUser: User = User(),
        val announcement: Announcement = Announcement(),
        val confirm: Boolean = false,
        var text: String = ""
) {}