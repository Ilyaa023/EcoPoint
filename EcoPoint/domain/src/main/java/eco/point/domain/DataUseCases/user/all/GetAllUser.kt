package eco.point.domain.DataUseCases.user.all

import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class GetAllUser(private val repository: UserRepository) {
    fun execute(): User {
        return User(
            name = repository.getData(UserKeys.NAME).name,
            city = repository.getData(UserKeys.CITY).city,
            birth = repository.getData(UserKeys.BIRTH).birth,
            bonuses = repository.getData(UserKeys.BONUSES).bonuses,
            level = repository.getData(UserKeys.LEVEL).level,
            garbageCounter = repository.getData(UserKeys.GARBAGE_COUNTER).garbageCounter,
            status = repository.getData(UserKeys.STATUS).status,
            visited = repository.getData(UserKeys.VISITED).visited,
            skiped = repository.getData(UserKeys.SKIP).skiped
        )
    }

    fun execute(callback: IFBCallback<User>){
        repository.getData(callback)
    }
}