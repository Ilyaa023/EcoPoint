package eco.point.ecopoint.ui.activities.exchange.allAnnouncements

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.exchange.FBAnnouncementsRepository
import eco.point.domain.DataUseCases.exchange.announcements.GetAnnouncements
import eco.point.domain.models.Announcement
import eco.point.domain.models.Point
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.domain.models.enums.PointKeys

class AllAnnouncementsViewModel: ViewModel() {
    private val antsMap = HashMap<Boolean, ArrayList<Announcement>>()
    val buyAnts = ArrayList<Announcement>()
    val sellAnts = ArrayList<Announcement>()
    private val visibilityMap = hashMapOf(
        ExchangeKeys.PLASTIC.key to true,
        ExchangeKeys.GLASS.key to true,
        ExchangeKeys.METAL.key to true,
        ExchangeKeys.PAPER.key to true,
        ExchangeKeys.FOOD.key to true,
        ExchangeKeys.BATTERY.key to true
    )

    fun getAnnouncements(callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        GetAnnouncements(FBAnnouncementsRepository()).execute{
            buyAnts.clear()
            sellAnts.clear()
            for (ant in it){
                if (!ant.banned)
                    if (ant.participant == Announcement.BUYER)
                        buyAnts.add(ant)
                    if (ant.participant == Announcement.SELLER)
                        sellAnts.add(ant)
            }
            antsMap[true] = buyAnts
            antsMap[false] = sellAnts
            setAnnouncementsVisibility(callback)
        }
    }

    fun setAnnouncementsVisibility(tag: String, callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit): Boolean{
        visibilityMap[tag] = !visibilityMap[tag]!!
        setAnnouncementsVisibility(callback)
        return visibilityMap[tag]!!
    }
    private fun setAnnouncementsVisibility(callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        val ants = HashMap<Boolean, ArrayList<Announcement>>()
        ants[true] = ArrayList()
        ants[false] = ArrayList()
        buyAnts.forEach { ant ->
            visibilityMap.forEach { visibility ->
                if (visibility.value && ant.tag.contains(visibility.key) && !ants[true]!!.contains(ant)){
                    ants[true]!!.add(ant)
                }
            }
        }
        sellAnts.forEach { ant ->
            visibilityMap.forEach { visibility ->
                if (visibility.value && ant.tag.contains(visibility.key) && !ants[false]!!.contains(ant)){
                    ants[false]!!.add(ant)
                }
            }
        }
        callback(ants)
    }
}