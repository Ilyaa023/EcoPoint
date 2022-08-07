package eco.point.domain.DataUseCases.exchange.announcements

import eco.point.domain.AnnouncementsRepository
import eco.point.domain.models.Announcement

class GetUserAnnouncement(val repository: AnnouncementsRepository) {
    fun execute(userId: String, id: String, callback: (Announcement?) -> Unit){
        repository.getUserAnnouncement(userId, id, callback)
    }
}