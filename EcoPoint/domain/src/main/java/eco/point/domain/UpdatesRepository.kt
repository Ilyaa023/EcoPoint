package eco.point.domain

abstract class UpdatesRepository {
    open fun getUpd(callback: (String) -> Unit){}
}