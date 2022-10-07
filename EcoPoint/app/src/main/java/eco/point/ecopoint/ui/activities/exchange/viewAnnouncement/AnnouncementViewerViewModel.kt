package eco.point.ecopoint.ui.activities.exchange.viewAnnouncement

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.exchange.FBAnnouncementsRepository
import eco.point.data.firebase.database.exchange.FBComplaintsRepository
import eco.point.domain.DataUseCases.exchange.announcements.GetUserAnnouncement
import eco.point.domain.DataUseCases.exchange.complaints.SetComplaint
import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.ecopoint.R
import java.util.*

class AnnouncementViewerViewModel: ViewModel() {
    fun getAnnouncement(uId: String, id: String, callback: (Announcement?) -> Unit){
        GetUserAnnouncement(FBAnnouncementsRepository()).execute(uId, id, callback)
    }

    fun getDateString(dt: GregorianCalendar, activity: Activity): String {
        val months = activity.resources.getStringArray(R.array.Months)
        val dateTimeStr =
            when(Locale.getDefault().language){
                "en" -> {
                    months[dt.get(GregorianCalendar.MONTH)] +
                            " " + dt.get(GregorianCalendar.DAY_OF_MONTH).toString() +
                            ", " + dt.get(GregorianCalendar.YEAR)
                }
                else -> {
                    dt.get(GregorianCalendar.DAY_OF_MONTH).toString() +
                            " " + months[dt.get(GregorianCalendar.MONTH)] +
                            " " + dt.get(GregorianCalendar.YEAR)
                }
            }// + " " + activity.getString(R.string.my_announcements_at) + " " +
            //dt.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
            //dt.get(GregorianCalendar.MINUTE)
        return dateTimeStr
    }

    fun getPlasticVisibility(tag: String): Int =
        if (tag.contains(ExchangeKeys.PLASTIC.key)) View.VISIBLE else View.GONE
    fun getGlassVisibility(tag: String): Int =
        if (tag.contains(ExchangeKeys.GLASS.key)) View.VISIBLE else View.GONE
    fun getMetalVisibility(tag: String): Int =
        if (tag.contains(ExchangeKeys.METAL.key)) View.VISIBLE else View.GONE
    fun getPaperVisibility(tag: String): Int =
        if (tag.contains(ExchangeKeys.PAPER.key)) View.VISIBLE else View.GONE
    fun getFoodVisibility(tag: String): Int =
        if (tag.contains(ExchangeKeys.FOOD.key)) View.VISIBLE else View.GONE
    fun getBatteryVisibility(tag: String): Int =
        if (tag.contains(ExchangeKeys.BATTERY.key)) View.VISIBLE else View.GONE

    fun getConnectVisibility(s: String?): Int =
        if (s!= null && s.isNotEmpty()) View.VISIBLE else View.GONE

    fun getCorrectLink(link: String):String =
        if (link.contains("https://") || link.contains("http://"))
            link
        else "https://$link"

    fun sendReport(uId: String, aId: String, text: String){
        SetComplaint(FBComplaintsRepository(), FBAnnouncementsRepository())
            .execute(uId, aId, text)
    }
}