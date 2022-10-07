package eco.point.ecopoint.ui.activities.exchange

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.exchange.FBAnnouncementsRepository
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.domain.DataUseCases.exchange.announcements.GetAnnouncements
import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.TagKeys

class AnnouncementsViewModel: ViewModel() {
    companion object{
        const val MY = 3
    }
    val allAnts = ArrayList<Announcement>()
    val wasteTypesStates = MutableLiveData(hashMapOf(
        TagKeys.PLASTIC.key to true,
        TagKeys.GLASS.key to true,
        TagKeys.METAL.key to true,
        TagKeys.PAPER.key to true,
        TagKeys.FOOD.key to true,
        TagKeys.BATTERY.key to true
    ))

    val antsTypesStates = MutableLiveData(hashMapOf(
        Announcement.BUYER to true,
        Announcement.SELLER to true
    ))
    val myAntsState = MutableLiveData(true)

    val filteredAnts = MutableLiveData(ArrayList<Announcement>())

    fun getAnnouncements(isBanned: Boolean, callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        GetAnnouncements(FBAnnouncementsRepository()).execute{
            allAnts.clear()
            for (ant in it){
                if (ant.banned == isBanned) {
                    allAnts.add(ant)
                }
            }
            setAnnouncementsVisibility(callback)
        }
    }

    fun setAnnouncementsVisibility(tag: String, callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        val m = HashMap<String, Boolean>()
        wasteTypesStates.value!!.forEach{
            m[it.key] = if (tag != it.key) it.value else !it.value
        }
//        wasteTypesStates.value!![tag] = !wasteTypesStates.value!![tag]!!
        wasteTypesStates.value = m
        setAnnouncementsVisibility(callback)
    }
    fun setAnnouncementsVisibility(tag: Int, callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        val m = HashMap<Int, Boolean>()
        if (tag == MY)
            myAntsState.value = !myAntsState.value!!
        antsTypesStates.value!!.forEach{
            m[it.key] = if (tag != it.key) it.value else !it.value
        }
//        antsTypesStates.value!![tag] = !antsTypesStates.value!![tag]!!
        antsTypesStates.value = m
        setAnnouncementsVisibility(callback)
    }
    private fun setAnnouncementsVisibility(callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        val finalList = ArrayList<Announcement>()
        val uid = FBUserRepository().uid
        allAnts.forEach { ant ->
            if ((myAntsState.value!! && ant.creator.id == uid
                        || !myAntsState.value!! && ant.creator.id != uid)
                && (antsTypesStates.value!![Announcement.BUYER]!! && ant.participant == Announcement.BUYER
                        || antsTypesStates.value!![Announcement.SELLER]!! && ant.participant == Announcement.SELLER)) {
                var isAdded = false
                wasteTypesStates.value!!.forEach { s, b ->
                    if (!isAdded && b && ant.tag.contains(s)) {
                        finalList.add(ant)
                        isAdded = true
                    }
                }
            }
        }
        filteredAnts.value = finalList
//            wasteTypesStates.value!!.forEach { (wasteKey, wasteState) ->
//                antsTypesStates.value!!.forEach { (antKey, antState) ->
//                    Log.e("TAG", "setAnnouncementsVisibility: ${ant.tag}: $wasteKey,\t ${ant.creator.id}: $uid,\t ${ant.participant}: $antKey")
//                    Log.e("TAG", "setAnnouncementsVisibility: ${wasteState && ant.tag.contains(wasteKey)}" +
//                            ",\t ${myAntsState.value!! && uid == ant.creator.id},\t ${!(myAntsState.value!! && uid == ant.creator.id)}" +
//                            ",\t ${antState && antKey == ant.participant}")
//                    if (wasteState && ant.tag.contains(wasteKey)
//                        && (myAntsState.value!! && uid == ant.creator.id
//                                || !(myAntsState.value!! && uid == ant.creator.id))
//                        && antState && antKey == ant.participant && !list.contains(ant)){
//                        Log.e("TAG", "setAnnouncementsVisibility: add!!!")
//                        list.add(ant)
//                    }
//                }
//            }
//        filteredAnts.value = list



//        val ants = HashMap<Boolean, ArrayList<Announcement>>()
//        ants[true] = ArrayList()
//        ants[false] = ArrayList()
//        buyAnts.forEach { ant ->
//            visibilityMap.forEach { visibility ->
//                if (visibility.value && ant.tag.contains(visibility.key) && !ants[true]!!.contains(ant)){
//                    ants[true]!!.add(ant)
//                }
//            }
//        }
//        sellAnts.forEach { ant ->
//            visibilityMap.forEach { visibility ->
//                if (visibility.value && ant.tag.contains(visibility.key) && !ants[false]!!.contains(ant)){
//                    ants[false]!!.add(ant)
//                }
//            }
//        }
//        callback(ants)
    }
}