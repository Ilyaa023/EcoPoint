package eco.point.domain.DataUseCases.user.name

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetUserName(private val repository: UserRepository) {
    fun execute(): String = repository.getData(UserKeys.NAME).name
    fun execute(callback: IFBCallback<String>){
        repository.getData(UserKeys.NAME, object : IFBCallback<User> {
            override fun onReceive(user: User) {
                callback.onReceive(user.name)
            }
        })
    }
}