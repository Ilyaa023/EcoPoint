package eco.point.domain

import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys

abstract class UserRepository {
    open fun setData(key: UserKeys, user: User){}
    open fun getData(key: UserKeys): User {return User()}
    open fun getData(key: UserKeys, callback: IFBCallback<User>){}
    open fun getData(callback: IFBCallback<User>){}
}