package eco.point.domain.DataUseCases.user.status

import eco.point.domain.UserRepository
import eco.point.domain.models.enums.UserKeys

class GetUserStatus(val repository: UserRepository) {
    fun execute(): Int = repository.getData(UserKeys.STATUS).status
}