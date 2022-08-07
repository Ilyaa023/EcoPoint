package eco.point.domain.DataUseCases.exchange.announcements

import eco.point.domain.AnnouncementsRepository
import eco.point.domain.models.Announcement

class GetAnnouncements(val repository: AnnouncementsRepository) {
    fun execute(callback: (ArrayList<Announcement>) -> Unit){
        repository.getAll(callback)
    }
}