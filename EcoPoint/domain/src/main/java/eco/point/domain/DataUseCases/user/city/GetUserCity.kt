package eco.point.domain.DataUseCases.user.city

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetUserCity(private val repository: UserRepository) {
    fun execute(): String = repository.getData(UserKeys.CITY).city
    fun execute(callback: IFBCallback<String>){
        repository.getData(UserKeys.CITY, object : IFBCallback<User> {
            override fun onReceive(user: User) {
                callback.onReceive(user.city)
            }
        })
    }
}