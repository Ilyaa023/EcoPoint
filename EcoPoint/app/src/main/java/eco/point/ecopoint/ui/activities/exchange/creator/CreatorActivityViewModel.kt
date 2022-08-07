package eco.point.ecopoint.ui.activities.exchange.creator

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.exchange.FBAnnouncementsRepository
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.DataUseCases.exchange.announcements.GetUserAnnouncement
import eco.point.domain.DataUseCases.exchange.announcements.SetMyAnnouncement
import eco.point.domain.DataUseCases.user.all.GetAllUser
import eco.point.domain.IFBCallback
import eco.point.domain.models.Announcement
import eco.point.domain.models.User
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.ecopoint.R
import java.util.*

class CreatorActivityViewModel(lRep: LocalUserRepository, eid: String? = null): ViewModel() {
    val plasticState = MutableLiveData(true)
    val glassState = MutableLiveData(true)
    val metalState = MutableLiveData(true)
    val paperState = MutableLiveData(true)
    val foodState = MutableLiveData(true)
    val batteryState = MutableLiveData(true)

    val SUCCESS = 1
    val ERROR = 2

    val idError = MutableLiveData(true)
    val titleError = MutableLiveData(true)
    val participantError = MutableLiveData(true)
    val descriptionError = MutableLiveData(true)
    val tagError = MutableLiveData(true)
    val costError = MutableLiveData(true)
    val connectError = MutableLiveData(true)
    val emailError = MutableLiveData(true)
    val vkError = MutableLiveData(true)
    val tgError = MutableLiveData(true)
    val telError = MutableLiveData(true)
    val allError = MutableLiveData(true)

    val completeData = MutableLiveData(false)

    private var title = ""
    private var description = ""
    private var cost: Double = -1.0
    private var units = ""
    private var email: String? = null
    private var vk: String? = null
    private var tg: String? = null
    private var tel: String? = null
    private var id: String = ""
    private var creator: User = User()
    private var lastDateTime: GregorianCalendar? = null

    var participant = Announcement.BUYER
    set(value) {
        field = if (value == Announcement.SELLER || value == Announcement.BUYER) value
        else Announcement.BUYER
    }

    fun changePlasticState(){ plasticState.value = !plasticState.value!! }
    fun changeGlassState(){ glassState.value = !glassState.value!! }
    fun changeMetalState(){ metalState.value = !metalState.value!! }
    fun changePaperState(){ paperState.value = !paperState.value!! }
    fun changeFoodState(){ foodState.value = !foodState.value!! }
    fun changeBatteryState(){ batteryState.value = !batteryState.value!! }

    fun setTitle(t: String){title = t}
    fun setDescription(d: String){description = d}
    fun setCost(c: String){ cost = try { c.toDouble() } catch (e: Exception) { -1.0 } }
    fun setUnits(u: String){units = u}
    fun setEmail(e: String){email = if (e != "") e else null}
    fun setVk(v: String){vk = if (v != "") v else null}
    fun setTg(t: String){tg = if (t != "") t else null}
    fun setTel(t: String){tel = if (t != "") t else null}

    fun getEmail(): String = FBUserRepository().getEmail()!!

    fun createAnnouncement(callback: (Int) -> Unit){
        var tag = ""
        if (plasticState.value!!) tag += ExchangeKeys.PLASTIC.key
        if (glassState.value!!) tag += ExchangeKeys.GLASS.key
        if (metalState.value!!) tag += ExchangeKeys.METAL.key
        if (paperState.value!!) tag += ExchangeKeys.PAPER.key
        if (foodState.value!!) tag += ExchangeKeys.FOOD.key
        if (batteryState.value!!) tag += ExchangeKeys.BATTERY.key
        val ant = Announcement(
            id = id,
            title = title,
            dateTime = if (lastDateTime == null) GregorianCalendar() else lastDateTime!!,
            creator = creator,
            participant = participant,
            description = description,
            tag = tag,
            vkLink = vk,
            tgLink = tg,
            telephone = tel,
            eMail = email,
            cost = cost,
            units = units
        )
        val bMap = SetMyAnnouncement(FBAnnouncementsRepository()).execute(ant){
            if (it)
                callback(ERROR)
        }
        connectError.value = bMap["Connect"]
        if (bMap.containsValue(false)){
            idError.value = bMap[ExchangeKeys.ID.key]
            titleError.value = bMap[ExchangeKeys.TITLE.key]
            participantError.value = bMap[ExchangeKeys.PARTICIPANT.key]
            descriptionError.value = bMap[ExchangeKeys.DESCRIPTION.key]
            tagError.value = bMap[ExchangeKeys.TAG.key]
            costError.value = bMap[ExchangeKeys.COST.key]
            if (connectError.value!!) {
                emailError.value = bMap[ExchangeKeys.EMAIL.key]
                telError.value = bMap[ExchangeKeys.TEL.key]
                vkError.value = bMap[ExchangeKeys.VK_LINK.key]
                tgError.value = bMap[ExchangeKeys.TG_LINK.key]
            } else {
                emailError.value = false
                telError.value = false
                vkError.value = false
                tgError.value = false
            }
        } else {
            callback(SUCCESS)
        }
    }

    init {
        eid?.let { id = it }
        GetAllUser(FBUserRepository()).execute(object : IFBCallback<User>{
            override fun onReceive(data: User) {
                creator = data
                if (id == "")
                    id = data.id + "_" + GregorianCalendar().timeInMillis.toString()
                GetUserAnnouncement(FBAnnouncementsRepository()).execute(data.id, id){
                    if (it == null)
                        allError.value = false
                    else{
                        plasticState.value = it.tag.contains(ExchangeKeys.PLASTIC.key)
                        glassState.value = it.tag.contains(ExchangeKeys.GLASS.key)
                        metalState.value = it.tag.contains(ExchangeKeys.METAL.key)
                        paperState.value = it.tag.contains(ExchangeKeys.PAPER.key)
                        foodState.value = it.tag.contains(ExchangeKeys.FOOD.key)
                        batteryState.value = it.tag.contains(ExchangeKeys.BATTERY.key)

                        title = it.title
                        description = it.description
                        cost = it.cost
                        units = it.units
                        email = it.eMail
                        vk = it.vkLink
                        tg = it.tgLink
                        tel = it.telephone
                        creator = it.creator
                        participant = it.participant
                        lastDateTime = it.dateTime
                        completeData.value = true
                    }
                }
            }
        })
    }

    fun getData(): Announcement {
        var tag = ""
        if (plasticState.value!!) tag += ExchangeKeys.PLASTIC.key
        if (glassState.value!!) tag += ExchangeKeys.GLASS.key
        if (metalState.value!!) tag += ExchangeKeys.METAL.key
        if (paperState.value!!) tag += ExchangeKeys.PAPER.key
        if (foodState.value!!) tag += ExchangeKeys.FOOD.key
        if (batteryState.value!!) tag += ExchangeKeys.BATTERY.key
        return Announcement(
            id = id,
            title = title,
            dateTime = lastDateTime!!,
            creator = creator,
            participant = participant,
            description = description,
            tag = tag,
            vkLink = vk,
            tgLink = tg,
            telephone = tel,
            eMail = email,
            cost = cost,
            units = units
        )
    }
}