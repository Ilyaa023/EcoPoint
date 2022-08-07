package eco.point.domain.DataUseCases.user.rating

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetUserRating(private val repository: UserRepository) {
    fun execute(): Int = repository.getData(UserKeys.RATING).rating
    fun execute(callback: IFBCallback<Int>){
        repository.getData(UserKeys.RATING, object : IFBCallback<User> {
            override fun onReceive(user: User) {
                callback.onReceive(user.rating)
            }
        })
    }
}