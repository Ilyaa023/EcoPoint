package eco.point.data.storage

import android.content.Context
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys
import eco.point.domain.models.enums.UserKeys.*
import java.util.*

class LocalUserRepository(val context: Context): UserRepository() {
    val prefs = context.getSharedPreferences(STORAGE_NAME.name, Context.MODE_PRIVATE)
    override fun setData(key: UserKeys, user: User) {
        val editor = prefs.edit()
        val keyS = key.key
        when (key) {
            NAME -> editor.putString(keyS, user.name)
            CITY -> editor.putString(keyS, user.city)
            BIRTH -> editor.putLong(keyS, user.birth.timeInMillis)
            LEVEL -> editor.putInt(keyS, user.level)
            BONUSES -> editor.putInt(keyS, user.bonuses)
            GARBAGE_COUNTER -> editor.putInt(keyS, user.garbageCounter)
            STATUS -> editor.putInt(keyS, user.status)
            VISITED -> editor.putBoolean(keyS, user.visited)
            SKIP -> editor.putBoolean(keyS, user.skiped)
            RATING -> editor.putInt(keyS, user.rating)
            else -> return
        }
        editor.apply()
    }

    override fun getData(key: UserKeys): User {
        val keyS = key.key
        return when (key){
            NAME -> User(name = prefs.getString(keyS, "")!!)
            CITY -> User(city = prefs.getString(keyS, "")!!)
            BIRTH -> {
                val calendar = GregorianCalendar()
                calendar.timeInMillis = prefs.getLong(keyS, GregorianCalendar().timeInMillis)
                User(birth = calendar)
            }
            LEVEL -> User(level = prefs.getInt(keyS, 0))
            BONUSES -> User(bonuses = prefs.getInt(keyS, 0))
            GARBAGE_COUNTER -> User(garbageCounter = prefs.getInt(keyS, 0))
            STATUS -> User(status = prefs.getInt(keyS, 0))
            VISITED -> User(visited = prefs.getBoolean(keyS, false))
            SKIP -> User(skiped = prefs.getBoolean(keyS, false))
            RATING -> User(rating = prefs.getInt(keyS, 0))
            else -> super.getData(key)
        }
    }

    fun deleteAll(){
        prefs.edit().clear().apply()
    }
}