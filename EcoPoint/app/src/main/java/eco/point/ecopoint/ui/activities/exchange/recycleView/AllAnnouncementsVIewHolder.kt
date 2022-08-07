package eco.point.ecopoint.ui.activities.exchange.recycleView

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.MyAnnouncementItemBinding
import eco.point.ecopoint.ui.activities.exchange.viewAnnouncement.AnnouncementViewerActivity
import java.util.*

class AllAnnouncementsVIewHolder(item: View, private val activity: Activity): RecyclerView.ViewHolder(item) {
    private var binding: MyAnnouncementItemBinding

    init{
        binding = MyAnnouncementItemBinding.bind(item)
    }

    fun bind(announcement: Announcement){
        with(binding){
            announcementTitle.text = announcement.title
            creatorName.text = announcement.creator.name
            announcementDescription.text = announcement.description
            plasticImage.visibility =
                if (announcement.tag.contains(ExchangeKeys.PLASTIC.key)) View.VISIBLE else View.GONE
            glassImage.visibility =
                if (announcement.tag.contains(ExchangeKeys.GLASS.key)) View.VISIBLE else View.GONE
            metalImage.visibility =
                if (announcement.tag.contains(ExchangeKeys.METAL.key)) View.VISIBLE else View.GONE
            paperImage.visibility =
                if (announcement.tag.contains(ExchangeKeys.PAPER.key)) View.VISIBLE else View.GONE
            foodImage.visibility =
                if (announcement.tag.contains(ExchangeKeys.FOOD.key)) View.VISIBLE else View.GONE
            batteryImage.visibility =
                if (announcement.tag.contains(ExchangeKeys.BATTERY.key)) View.VISIBLE else View.GONE
            announcementCost.text = announcement.cost.toString()
            announcementUnits.text = announcement.units
            creatorCity.text = announcement.creator.city
            val months = activity.resources.getStringArray(R.array.Months)
            val dateTimeStr = activity.getString(R.string.my_announcements_publish) + " " +
                    when(Locale.getDefault().language){
                        "en" -> {
                            months[announcement.dateTime.get(GregorianCalendar.MONTH)] +
                                    " " + announcement.dateTime.get(GregorianCalendar.DAY_OF_MONTH).toString() +
                                    ", " + announcement.dateTime.get(GregorianCalendar.YEAR)
                        }
                        else -> {
                            announcement.dateTime.get(GregorianCalendar.DAY_OF_MONTH).toString() +
                                    " " + months[announcement.dateTime.get(GregorianCalendar.MONTH)] +
                                    " " + announcement.dateTime.get(GregorianCalendar.YEAR)
                        }
                    } + " " + activity.getString(R.string.my_announcements_at) + " " +
                    announcement.dateTime.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
                    announcement.dateTime.get(GregorianCalendar.MINUTE)

            dateTime.text = dateTimeStr
            participant.text = if (announcement.participant == 1)
                activity.getString(R.string.my_announcement_sell)
            else
                activity.getString(R.string.my_announcement_buy)

            root.setOnClickListener {
                activity.startActivity(Intent(activity.applicationContext, AnnouncementViewerActivity::class.java).apply{
                    putExtra("id", announcement.id)
                    putExtra("uId", announcement.creator.id)
                    putExtra("Reportable", true)
                })
            }
        }
    }
}