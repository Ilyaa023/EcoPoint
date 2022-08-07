package eco.point.domain.DataUseCases.auth.visited

import eco.point.domain.UserRepository
import eco.point.domain.models.enums.UserKeys

class GetVisited(val repository: UserRepository) {
    fun execute(): Boolean = repository.getData(UserKeys.VISITED).visited
}