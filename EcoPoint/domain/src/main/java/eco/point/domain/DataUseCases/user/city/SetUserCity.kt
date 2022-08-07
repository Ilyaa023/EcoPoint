package eco.point.domain.DataUseCases.user.city

import eco.point.domain.UserRepository
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

class SetUserCity(private val repository: UserRepository, private val cities: Array<String>) {
    fun execute(city: String): Boolean{
        if (cities.contains(city)){
            repository.setData(UserKeys.CITY, User(city = city))
            return true
        }
        return false
    }
}