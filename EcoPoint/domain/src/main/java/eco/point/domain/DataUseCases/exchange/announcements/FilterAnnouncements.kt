package eco.point.domain.DataUseCases.exchange.announcements

import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.ExchangeKeys

class FilterAnnouncements {
    fun execute(allList: ArrayList<Announcement>, filter: ArrayList<ExchangeKeys>)
    : ArrayList<Announcement> {
        val filteredList = ArrayList<Announcement>()
        for (announcement in allList) {
            for (tag in filter) {
                if (announcement.tag.contains(tag.key)) filteredList.add(announcement)
            }
        }
        return filteredList
    }
}