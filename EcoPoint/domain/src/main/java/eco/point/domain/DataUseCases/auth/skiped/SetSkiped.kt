package eco.point.domain.DataUseCases.auth.skiped

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetSkiped(val repository: UserRepository) {
    fun execute(data: Boolean){
        repository.setData(UserKeys.SKIP, User(skiped = data))
    }
}