package eco.point.domain.DataUseCases.user.birth

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys
import java.util.*

class GetUserBirthDay(private val repository: UserRepository) {
    fun execute(): GregorianCalendar{
        return repository.getData(UserKeys.BIRTH).birth
    }

    fun execute(callback: IFBCallback<GregorianCalendar>){
        repository.getData(key = UserKeys.BIRTH, callback = object : IFBCallback<User>{
            override fun onReceive(user: User) {
                callback.onReceive(user.birth)
            }
        })
    }
}