package eco.point.domain

interface IGoogleAuth {
    fun signIn(callback: IGoogleAuthCallback)
    fun reload()
    fun signOut()
}