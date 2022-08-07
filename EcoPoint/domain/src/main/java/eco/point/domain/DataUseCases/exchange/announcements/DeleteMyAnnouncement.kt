package eco.point.domain.DataUseCases.exchange.announcements

import eco.point.domain.AnnouncementsRepository
import eco.point.domain.models.Announcement

class DeleteMyAnnouncement(val repository: AnnouncementsRepository) {
    fun execute(announcement: Announcement){
        repository.deleteMy(announcement)
    }
}