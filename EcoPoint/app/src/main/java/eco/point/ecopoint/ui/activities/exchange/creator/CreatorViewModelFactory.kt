package eco.point.ecopoint.ui.activities.exchange.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eco.point.data.storage.LocalUserRepository

class CreatorViewModelFactory(private val localRep: LocalUserRepository, private val eid: String? = null): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreatorActivityViewModel(localRep, eid) as T
    }
}