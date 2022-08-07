package eco.point.domain.DataUseCases.auth.skiped

import eco.point.domain.UserRepository
import eco.point.domain.models.enums.UserKeys

class GetSkiped(val repository: UserRepository) {
    fun execute(): Boolean = repository.getData(UserKeys.SKIP).skiped
}