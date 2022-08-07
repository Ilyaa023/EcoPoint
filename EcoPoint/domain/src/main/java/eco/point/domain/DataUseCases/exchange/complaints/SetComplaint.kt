package eco.point.domain.DataUseCases.exchange.complaints

import eco.point.domain.AnnouncementsRepository
import eco.point.domain.ComplaintsRepository
import eco.point.domain.models.Announcement
import eco.point.domain.models.Complaint
import eco.point.domain.models.User
import java.util.*

class SetComplaint(private val complaintRepository: ComplaintsRepository, private val announcementRepository: AnnouncementsRepository) {
    fun execute(uId: String, aId: String, text: String){
        val cId = uId + "_" + GregorianCalendar().timeInMillis
        complaintRepository.setComplaint(
            Complaint(
            cId = cId,
            cUser = User(id = uId),
            announcement =  Announcement(id = aId),
            text = text
        ))
        announcementRepository.setComplain(uId, aId, cId)
    }
}