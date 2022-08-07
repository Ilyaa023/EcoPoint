package eco.point.ecopoint.ui.activities.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.ecopoint.models.ActivityResultModel
import eco.point.data.firebase.googleAuth.GoogleSignInImpl
import eco.point.domain.GoogleAuthUseCases.GoogleReload
import eco.point.domain.GoogleAuthUseCases.GoogleSignIn
import eco.point.domain.IGoogleAuthCallback
import eco.point.domain.DataUseCases.user.garbageCounter.SetUserGarbageCounter
import eco.point.domain.DataUseCases.user.level.SetUserLevel
import eco.point.domain.DataUseCases.user.name.GetUserName
import eco.point.domain.DataUseCases.user.name.SetUserName
import eco.point.domain.DataUseCases.user.rating.SetUserRating
import eco.point.domain.DataUseCases.user.status.SetUserStatus
import eco.point.domain.models.User
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.DataUseCases.auth.skiped.SetSkiped
import eco.point.domain.DataUseCases.auth.visited.SetVisited
import eco.point.domain.DataUseCases.user.all.GetAllUser
import eco.point.domain.DataUseCases.user.birth.GetUserBirthDay
import eco.point.domain.DataUseCases.user.birth.SetUserBirthDay
import eco.point.domain.DataUseCases.user.bonuses.SetUserBonuses
import eco.point.domain.DataUseCases.user.city.GetUserCity
import eco.point.domain.DataUseCases.user.city.SetUserCity
import eco.point.domain.DataUseCases.user.status.GetUserStatus
import eco.point.domain.IFBCallback
import java.util.*

class AuthorizationViewModel(
        private val cities: Array<String>,
        var googleSignInImpl: GoogleSignInImpl,
        var userRepository: LocalUserRepository
) : ViewModel() {
    var userDate: MutableLiveData<GregorianCalendar> = MutableLiveData<GregorianCalendar>(
        GetUserBirthDay(userRepository).execute()
    )
    var city: MutableLiveData<String> = MutableLiveData<String>(
        GetUserCity(userRepository).execute()
    )
    var name: MutableLiveData<String> = MutableLiveData<String>(
        GetUserName(userRepository).execute()
    )
    var checkedRB = MutableLiveData(0)
    var correctDate = MutableLiveData(true)
    var correctCity = MutableLiveData(true)
    var correctName = MutableLiveData(true)

    fun signIn(resultModel: ActivityResultModel, callback: IGoogleAuthCallback) {
        googleSignInImpl.initResult(resultModel.result!!)
        GoogleSignIn(googleSignInImpl, callback).execute()
    }

    fun getToast(resultCode: Int): String{
        return when (resultCode){
            GoogleSignInImpl.COMPLETE -> "Complete"
            GoogleSignInImpl.CANCEL -> "Cancel"
            else -> "Fail"
        }
    }

    fun getAllLocalUserData(): User{
        return GetAllUser(userRepository).execute()
    }

    fun getAllCloudUserData(callback: IFBCallback<User>){
        GetAllUser(FBUserRepository()).execute(callback)
    }

    fun compareUserData(user1: User, user2: User): Boolean {
        if (
            user1.bonuses == user2.bonuses &&
            user1.garbageCounter == user2.garbageCounter &&
            user1.level == user2.level &&
            user1.rating == user2.rating
        )
            return true
        return false
    }

    fun sendBaseData(){
        val repository = FBUserRepository()
        SetUserName(repository).execute(GetUserName(userRepository).execute())
        SetUserCity(repository, cities)
            .execute(GetUserCity(userRepository).execute())
        SetUserBirthDay(repository)
            .execute(GetUserBirthDay(userRepository).execute())
        SetUserStatus(repository).execute(checkedRB.value!!)
    }
    fun sendOtherData(user: User){
        val repository = FBUserRepository()
        SetUserBonuses(repository).execute(user.bonuses)
        SetUserLevel(repository).execute(user.level)
        SetUserGarbageCounter(repository).execute(user.garbageCounter)
        SetUserRating(repository).execute(user.rating)
    }

    fun setBonuses(bonuses: Int){
        SetUserBonuses(userRepository).execute(bonuses)
    }
    fun setLevel(level: Int){
        SetUserLevel(userRepository).execute(level)
    }
    fun setGarbageCounter(garbageCounter: Int){
        SetUserGarbageCounter(userRepository).execute(garbageCounter)
    }
    fun checkDate(date: GregorianCalendar){
        correctDate.value = SetUserBirthDay(userRepository)
            .execute(date)
    }
    fun checkCity(city: String){
        this.city.value = city
        correctCity.value = SetUserCity(
            repository = userRepository, cities = cities
        ).execute(city)
    }
    fun checkName(name: String){
        this.name.value = name
        correctName.value = SetUserName(
            repository = userRepository
        ).execute(name = name)
    }
    fun setSkiped(state: Boolean){
        SetSkiped(userRepository).execute(state)
    }
    fun setVisited(state: Boolean){
        SetVisited(userRepository).execute(state)
    }
    fun setStatus(){
        SetUserStatus(userRepository).execute(checkedRB.value!!)
    }
    fun reload() {
        GoogleReload(auth = googleSignInImpl).execute()
    }
}