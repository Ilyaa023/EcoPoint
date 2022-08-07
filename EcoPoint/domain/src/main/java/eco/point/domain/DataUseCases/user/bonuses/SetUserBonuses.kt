package eco.point.domain.DataUseCases.user.bonuses

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserBonuses(val repository: UserRepository) {
    fun execute(bonuses: Int){
        repository.setData(UserKeys.BONUSES, User(bonuses = bonuses))
    }
}