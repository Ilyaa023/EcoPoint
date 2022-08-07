package eco.point.domain.DataUseCases.exchange.announcements

import eco.point.domain.AnnouncementsRepository
import eco.point.domain.models.Announcement

class GetUserAnnouncements(val repository: AnnouncementsRepository) {
    fun execute(userId: String, callback: (ArrayList<Announcement>) -> Unit){
        repository.getUserAll(userId, callback)
    }
}