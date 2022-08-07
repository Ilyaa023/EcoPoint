package eco.point.domain.DataUseCases.user.birth

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys
import java.util.*

class SetUserBirthDay(val repository: UserRepository) {
    fun execute(date: GregorianCalendar): Boolean {
        val nowDate = GregorianCalendar()
        val dif = nowDate.get(Calendar.YEAR) - date.get(Calendar.YEAR)
        if (dif > 6 && dif < 120) {
            repository.setData(UserKeys.BIRTH, User(birth = date))
            return true
        }
        return false
    }
}