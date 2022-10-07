package eco.point.data.firebase.database.user

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.DataType
import eco.point.domain.models.enums.UserKeys
import eco.point.domain.models.enums.UserKeys.*
import java.util.*

class FBUserRepository: UserRepository() {
    private val database = FirebaseDatabase.getInstance(
        "https://my-ecopoint-project-default-rtdb.europe-west1.firebasedatabase.app/"
    )
    private val users = database.getReference(DataType.USERS.key)
    var uid = FirebaseAuth.getInstance().uid

    override fun setData(key: UserKeys, user: User) {
        val data = when (key){
            NAME -> user.name
            CITY -> user.city
            BIRTH -> user.birth.timeInMillis
            BONUSES -> user.bonuses
            LEVEL -> user.level
            GARBAGE_COUNTER -> user.garbageCounter
            RATING -> user.rating
            STATUS -> user.status
            else -> null
        }
        uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            users.child(uid!!).child(key.key).setValue(data)
        }
    }

    override fun getData(key: UserKeys, callback: IFBCallback<User>) {
        uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            users.child(uid!!).child(key.key).get().addOnCompleteListener{
                if (it.isSuccessful){
                    callback.onReceive(when (key) {
                        NAME -> User(name = it.result.value.toString())
                        CITY -> User(city = it.result.value.toString())
                        BIRTH -> {
                            val calendar = GregorianCalendar()
                            calendar.timeInMillis = it.result.value.toString().toLong()
                            User(birth = calendar)
                        }
                        LEVEL -> User(level = it.result.value.toString().toInt())
                        BONUSES ->
                            User(bonuses = it.result.value.toString().toInt())
                        GARBAGE_COUNTER ->
                            User(garbageCounter = it.result.value.toString().toInt())
                        RATING -> User(rating = it.result.value.toString().toInt())
                        BANNED -> User(banned = it.result.value.toString().toBoolean())
                        else -> User()
                    })
                } else Log.e("TAG", "getData: ${it.exception}")
            }
        }
    }

    override fun getData(callback: IFBCallback<User>) {
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            users.child(uid).get().addOnCompleteListener{
                if (it.isSuccessful){
                    val name = it.result.child(NAME.key).value.toString()
                    val city = it.result.child(CITY.key).value.toString()
                    val bDate = it.result.child(BIRTH.key).value.toString()
                    val birth = GregorianCalendar()
                    if (bDate == "null")
                        birth.timeInMillis = GregorianCalendar().timeInMillis
                    else
                        birth.timeInMillis = bDate.toLong()
                    var level = 0
                    var bonuses = 0
                    var garbageCounter = 0
                    if (it.result.hasChild(LEVEL.key)) {
                        val levelS = it.result.child(LEVEL.key).value.toString()
                        if (levelS != "null") level = levelS.toInt()
                    }else
                        level = -1
                    if (it.result.hasChild(BONUSES.key)) {
                        val bonusesS = it.result.child(BONUSES.key).value.toString()
                        if (bonusesS != "null") bonuses = bonusesS.toInt()
                    }else
                        bonuses = -1
                    if (it.result.hasChild(GARBAGE_COUNTER.key)) {
                        val garbageCounterS = it.result.child(GARBAGE_COUNTER.key).value.toString()
                        if (garbageCounterS != "null") garbageCounter = garbageCounterS.toInt()
                    }else
                        garbageCounter = -1
                    callback.onReceive(
                        User(
                        name = name,
                        city = city,
                        birth = birth,
                        level = level,
                        bonuses = bonuses,
                        garbageCounter = garbageCounter,
                        id = uid,
                        banned = it.result.child(BANNED.key).value.toString().toBoolean()
                    )
                    )
                } else Log.e("TAG", "getData: ${it.exception}")
            }
        }
    }

    fun getEmail(): String?{
        return FirebaseAuth.getInstance().currentUser!!.email
    }
}