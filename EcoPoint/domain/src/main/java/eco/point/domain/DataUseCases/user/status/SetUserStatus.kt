package eco.point.domain.DataUseCases.user.status

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserStatus(val repository: UserRepository) {
    fun execute(data: Int){
        repository.setData(UserKeys.STATUS, User(status = data))
    }
}