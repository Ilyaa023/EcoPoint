package eco.point.domain.DataUseCases.user.bonuses

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetUserBonuses(val repository: UserRepository) {
    fun execute(): Int = repository.getData(UserKeys.BONUSES).bonuses
    fun execute(callback: IFBCallback<Int>){
        repository.getData(UserKeys.BONUSES, object : IFBCallback<User>{
            override fun onReceive(user: User) {
                callback.onReceive(user.bonuses)
            }
        })
    }
}