package eco.point.domain

interface IFBCallback<T> {
    fun onReceive(data: T)
}