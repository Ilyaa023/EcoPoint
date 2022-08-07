package eco.point.domain.DataUseCases.user.garbageCounter

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetUserGarbageCounter(val repository: UserRepository) {
    fun execute(): Int = repository.getData(UserKeys.GARBAGE_COUNTER).garbageCounter
    fun execute(callback: IFBCallback<Int>){
        repository.getData(UserKeys.GARBAGE_COUNTER, object : IFBCallback<User>{
            override fun onReceive(user: User) {
                callback.onReceive(user.garbageCounter)
            }
        })
    }
}