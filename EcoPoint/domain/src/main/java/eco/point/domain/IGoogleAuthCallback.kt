package eco.point.domain

interface IGoogleAuthCallback {
    fun onAuth(resultCode: Int)
}