package eco.point.domain.DataUseCases.user.level

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserLevel(val repository: UserRepository) {
    fun execute(data: Int){
        repository.setData(UserKeys.LEVEL, User(level = data))
    }
}