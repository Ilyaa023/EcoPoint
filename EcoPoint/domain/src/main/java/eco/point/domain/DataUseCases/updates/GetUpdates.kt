package eco.point.domain.DataUseCases.updates

import eco.point.domain.UpdatesRepository

class GetUpdates(val repository: UpdatesRepository) {
    fun execute(callback: (String) -> Unit){
        repository.getUpd(callback)
    }
}