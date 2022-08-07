package eco.point.data.firebase.database.exchange

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.domain.AnnouncementsRepository
import eco.point.domain.IFBCallback
import eco.point.domain.models.Announcement
import eco.point.domain.models.User
import eco.point.domain.models.enums.DataType
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.domain.models.enums.UserKeys
import java.util.*
import kotlin.collections.ArrayList

class FBAnnouncementsRepository: AnnouncementsRepository() {
    private val announcements = FirebaseDatabase.getInstance(
        "https://my-ecopoint-project-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(DataType.EXCHANGE.key).child(DataType.ANNOUNCEMENTS.key)

    override fun setMy(announcement: Announcement, callback: (Boolean) -> Unit) {
        FBUserRepository().getData(object : IFBCallback<User>{
            override fun onReceive(data: User) {
                callback(data.banned)
                if (!data.banned){
                    val uid = FirebaseAuth.getInstance().uid
                    val path = announcements.child(uid!!).child(announcement.id)
                    path.setValue(announcement)
//                    path.child(ExchangeKeys.CREATOR_ID.key).setValue(uid)
//                    path.child(ExchangeKeys.TITLE.key).setValue(announcement.title)
//                    path.child(ExchangeKeys.DATE_TIME.key).setValue(announcement.dateTime.timeInMillis)
//                    path.child(ExchangeKeys.CREATOR_NAME.key).setValue(announcement.creator.name)
//                    path.child(ExchangeKeys.PARTICIPANT.key).setValue(announcement.participant)
//                    path.child(ExchangeKeys.DESCRIPTION.key).setValue(announcement.description)
//                    path.child(ExchangeKeys.TAG.key).setValue(announcement.tag)
//                    path.child(ExchangeKeys.BANNED.key).setValue(false)
//                    path.child(ExchangeKeys.COST.key).setValue(announcement.cost)
//                    path.child(ExchangeKeys.UNITS.key).setValue(announcement.units)
//                    path.child(ExchangeKeys.CITY.key).setValue(data.city)
//                    announcement.telephone?.let {
//                        path.child(ExchangeKeys.TEL.key).setValue(announcement.telephone)
//                    }
//                    announcement.eMail?.let {
//                        path.child(ExchangeKeys.EMAIL.key).setValue(announcement.eMail)
//                    }
//                    announcement.vkLink?.let {
//                        path.child(ExchangeKeys.VK_LINK.key).setValue(announcement.vkLink)
//                    }
//                    announcement.tgLink?.let {
//                        path.child(ExchangeKeys.TG_LINK.key).setValue(announcement.tgLink)
//                    }
                }
            }
        })
    }

    override fun getUserAll(uId: String, callback: (ArrayList<Announcement>) -> Unit) {
        announcements.child(uId).get().addOnCompleteListener{ taskSnap ->
            if (taskSnap.isSuccessful){
                val ants = ArrayList<Announcement>()
                for (snap in taskSnap.result.children){
                    createAnnouncementFromSnapshot(snap, uId)?.let {
                        ants.add(it)
                    }
                }
                callback(ants)
            }
        }
    }

    override fun getAll(callback: (ArrayList<Announcement>) -> Unit) {
        announcements.get().addOnCompleteListener{ taskSnap ->
            if (taskSnap.isSuccessful){
                val ants = ArrayList<Announcement>()
                for (userSnapshot in taskSnap.result.children){
                    for (antSnapshot in userSnapshot.children) {
                        createAnnouncementFromSnapshot(antSnapshot, userSnapshot.key.toString())?.let {
                            if (!it.banned)
                                ants.add(it)
                        }
                    }
                }
                callback(ants)
            }
        }
    }

    override fun deleteMy(announcement: Announcement) {
        val uid = FirebaseAuth.getInstance().uid
        announcements.child(uid!!).child(announcement.id).removeValue()
    }

    override fun getUserAnnouncement(userId: String, id: String, callback: (Announcement?) -> Unit) {
        announcements.child(userId).child(id).get().addOnCompleteListener {
            callback(createAnnouncementFromSnapshot(it.result, userId))
        }
    }

    private fun createAnnouncementFromSnapshot(snap: DataSnapshot, uId: String): Announcement? {
        var ant: Announcement? = null
        try {
            val date = GregorianCalendar()
            date.timeInMillis = snap.child(ExchangeKeys.DATE_TIME.key).child("timeInMillis").value.toString().toLong()
            ant = Announcement(
                id = snap.key.toString(),
                title = snap.child(ExchangeKeys.TITLE.key).value.toString(),
                dateTime = date,
                creator = User(
                    id = uId,
                    name = snap.child(ExchangeKeys.CREATOR.key).child(ExchangeKeys.CREATOR_NAME.key).value.toString(),
                    city = snap.child(ExchangeKeys.CREATOR.key).child(ExchangeKeys.CITY.key).value.toString()
                ),
                participant = snap.child(ExchangeKeys.PARTICIPANT.key).value.toString().toInt(),
                description = snap.child(ExchangeKeys.DESCRIPTION.key).value.toString(),
                tag = snap.child(ExchangeKeys.TAG.key).value.toString(),
                complaintsCounter = snap.child(ExchangeKeys.COMPLAINTS.key).childrenCount,
                banned = snap.child(ExchangeKeys.BANNED.key).value.toString().toBoolean(),
                cost = snap.child(ExchangeKeys.COST.key).value.toString().toDouble(),
                units = snap.child(ExchangeKeys.UNITS.key).value.toString(),
            )
            snap.child(ExchangeKeys.EMAIL.key).value?.let { eMail ->
                ant.eMail = eMail.toString()
            }
            Log.e("TAG", "createAnnouncementFromSnapshot: ${ant.eMail}", )
            snap.child(ExchangeKeys.TEL.key).value?.let { tel ->
                ant.telephone = tel.toString()
            }
            snap.child(ExchangeKeys.VK_LINK.key).value?.let { vk ->
                ant.vkLink = vk.toString()
            }
            snap.child(ExchangeKeys.TG_LINK.key).value?.let { tg ->
                ant.tgLink = tg.toString()
            }
        } catch (e: Exception){ e.printStackTrace() }
        return ant
    }

    override fun setComplain(uId: String, aId: String, cId: String) {
        announcements.child(uId).child(aId).child(ExchangeKeys.COMPLAINTS.key).child(cId).setValue(false)
    }
}