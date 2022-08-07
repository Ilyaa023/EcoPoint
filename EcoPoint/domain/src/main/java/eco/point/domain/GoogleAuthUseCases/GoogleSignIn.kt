package eco.point.domain.GoogleAuthUseCases

import eco.point.domain.IGoogleAuth
import eco.point.domain.IGoogleAuthCallback

class GoogleSignIn(
    private val googleAuth: IGoogleAuth,
    private val callback: IGoogleAuthCallback
) {
    fun execute(){
        googleAuth.signIn(callback = callback)
    }
}