package eco.point.domain.DataUseCases.auth.visited

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetVisited(val repository: UserRepository) {
    fun execute(data: Boolean){
        repository.setData(UserKeys.VISITED, User(visited = data))
    }
}