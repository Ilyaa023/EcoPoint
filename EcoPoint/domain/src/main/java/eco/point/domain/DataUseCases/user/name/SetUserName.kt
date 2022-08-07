package eco.point.domain.DataUseCases.user.name

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserName(private val repository: UserRepository) {
    fun execute(name: String): Boolean{
        if (name.matches(Regex("([A-zА-я -]+){2,30}"))){
            repository.setData(UserKeys.NAME, User(name = name))
            return true
        }
        return false
    }
}