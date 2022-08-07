package eco.point.ecopoint.ui.activities.exchange.myAnnouncements

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.exchange.FBAnnouncementsRepository
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.domain.DataUseCases.exchange.announcements.GetUserAnnouncements
import eco.point.domain.DataUseCases.user.all.GetAllUser
import eco.point.domain.IFBCallback
import eco.point.domain.models.Announcement
import eco.point.domain.models.User

class MyAnnouncementsViewModel: ViewModel() {
    val antsMap = MutableLiveData<HashMap<Boolean, ArrayList<Announcement>>>(HashMap())
    val pubAnts = ArrayList<Announcement>()
    val banAnts = ArrayList<Announcement>()
    fun getAnnouncements(callback: (HashMap<Boolean, ArrayList<Announcement>>) -> Unit){
        GetAllUser(FBUserRepository()).execute(object : IFBCallback<User>{
            override fun onReceive(data: User) {
                GetUserAnnouncements(FBAnnouncementsRepository()).execute(data.id){
                    pubAnts.clear()
                    banAnts.clear()
                    for (ant in it){
                        if (ant.banned)
                            banAnts.add(ant)
                        else
                            pubAnts.add(ant)
                    }
                    antsMap.value!![true] = pubAnts
                    antsMap.value!![false] = banAnts
                    callback(antsMap.value!!)
                }
            }
        })
    }
}