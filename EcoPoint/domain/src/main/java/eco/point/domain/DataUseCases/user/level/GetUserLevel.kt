package eco.point.domain.DataUseCases.user.level

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetUserLevel(val repository: UserRepository) {
    fun execute(): Int = repository.getData(UserKeys.LEVEL).level
    fun execute(callback: IFBCallback<Int>){
        repository.getData(UserKeys.LEVEL, object : IFBCallback<User>{
            override fun onReceive(user: User) {
                callback.onReceive(user.level)
            }
        })
    }
}