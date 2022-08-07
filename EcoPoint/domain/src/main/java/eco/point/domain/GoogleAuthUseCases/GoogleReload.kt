package eco.point.domain.GoogleAuthUseCases

import eco.point.domain.IGoogleAuth

class GoogleReload(private val auth: IGoogleAuth?) {
    fun execute(){
        auth?.reload()
    }
}