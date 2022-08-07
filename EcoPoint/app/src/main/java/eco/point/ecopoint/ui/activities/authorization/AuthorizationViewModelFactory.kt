package eco.point.ecopoint.ui.activities.authorization

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eco.point.data.firebase.googleAuth.GoogleSignInImpl
import eco.point.data.storage.LocalUserRepository

class AuthorizationViewModelFactory
    (
        private var activity: Activity,
        private var cities: Array<String>
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthorizationViewModel(
            cities = cities,
            googleSignInImpl = GoogleSignInImpl(activity),
            userRepository = LocalUserRepository(activity.applicationContext)
        ) as T
    }
}