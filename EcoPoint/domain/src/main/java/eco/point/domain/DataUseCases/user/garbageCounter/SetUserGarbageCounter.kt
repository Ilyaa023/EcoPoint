package eco.point.domain.DataUseCases.user.garbageCounter

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserGarbageCounter(val repository: UserRepository) {
    fun execute(data: Int){
        repository.setData(UserKeys.GARBAGE_COUNTER, User(garbageCounter = data))
    }
}