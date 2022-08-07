package eco.point.domain.DataUseCases.user.rating

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserRating(private val repository: UserRepository) {
    fun execute(rating: Int){
            repository.setData(UserKeys.RATING, User(rating = rating))
    }
}