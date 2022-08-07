package eco.point.data.firebase.database.updates

import com.google.firebase.database.FirebaseDatabase
import eco.point.domain.UpdatesRepository
import eco.point.domain.models.enums.DataType

class FBUpdatesRepository: UpdatesRepository() {
    private val upd = FirebaseDatabase.getInstance(
        "https://my-ecopoint-project-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(DataType.UPDATE.key).child(DataType.UPD_VER.key)

    override fun getUpd(callback: (String) -> Unit) {
        upd.get().addOnCompleteListener { callback(it.result.value.toString()) }
    }

}