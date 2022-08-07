package eco.point.domain.DataUseCases.exchange.announcements

import eco.point.domain.AnnouncementsRepository
import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.domain.models.enums.UserKeys

class SetMyAnnouncement(val repository: AnnouncementsRepository) {
    fun execute(announcement: Announcement, callback: (Boolean) -> Unit): HashMap<String, Boolean>{
        val errors = HashMap<String, Boolean>()

        errors[ExchangeKeys.ID.key] = announcement.id != Announcement().id

        errors[ExchangeKeys.TITLE.key] =
            announcement.title != Announcement().title
                    && announcement.title.matches(Regex("([A-zА-я0-9 ,.+/<>():;&`'\"!?-]+){1,50}"))

        errors[ExchangeKeys.PARTICIPANT.key] =
            announcement.participant == Announcement.SELLER
                    || announcement.participant == Announcement.BUYER

        errors[ExchangeKeys.DESCRIPTION.key] =
            announcement.description != Announcement().description
                    && announcement.description.isNotEmpty()
                    && announcement.description.length < 10000

        errors[ExchangeKeys.TAG.key] =
            announcement.tag.contains(ExchangeKeys.PLASTIC.key)
                    || announcement.tag.contains(ExchangeKeys.METAL.key)
                    || announcement.tag.contains(ExchangeKeys.PAPER.key)
                    || announcement.tag.contains(ExchangeKeys.FOOD.key)
                    || announcement.tag.contains(ExchangeKeys.BATTERY.key)
                    || announcement.tag.contains(ExchangeKeys.GLASS.key)

        errors["Connect"] =
            (announcement.eMail != null && announcement.eMail != "")
                    || (announcement.tgLink != null && announcement.tgLink != "")
                    || (announcement.vkLink != null && announcement.vkLink != "")
                    || (announcement.telephone != null && announcement.telephone != "")

        errors[ExchangeKeys.EMAIL.key] = true
        errors[ExchangeKeys.TEL.key] = true
        errors[ExchangeKeys.VK_LINK.key] = true
        errors[ExchangeKeys.TG_LINK.key] = true

        announcement.eMail?.let {
            errors[ExchangeKeys.EMAIL.key] = it.matches(Regex(
                        "^([A-z0-9_-]+(\\.[A-z0-9_-]+)*)@(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,4})\$"
                ))
        }
        announcement.telephone?.let {
            errors[ExchangeKeys.TEL.key] = it.matches(Regex("^\\+?[0-9]{11}$"))
        }
        announcement.vkLink?.let {
                errors[ExchangeKeys.VK_LINK.key] = it.matches(Regex(
                    "^(http://|https://)?(www.)?(vk\\.com|vkontakte\\.ru)/(id\\d|[a-zA-Z0-9_.])+$"
                ))
        }
        announcement.tgLink?.let {
                errors[ExchangeKeys.TG_LINK.key] = it.matches(Regex(
                    "^(http://|https://)?(www.)?(t\\.me)/([a-zA-Z0-9_.]{5,32})$"
                ))
        }
        errors[ExchangeKeys.COST.key] = announcement.cost >= 0

        if (!errors.containsValue(false))
            repository.setMy(announcement, callback)
        return errors
    }
}