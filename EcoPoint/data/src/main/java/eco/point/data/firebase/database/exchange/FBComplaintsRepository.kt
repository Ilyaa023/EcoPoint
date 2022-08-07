package eco.point.data.firebase.database.exchange

import com.google.firebase.database.FirebaseDatabase
import eco.point.domain.ComplaintsRepository
import eco.point.domain.models.Complaint
import eco.point.domain.models.enums.DataType

class FBComplaintsRepository: ComplaintsRepository() {
    private val complaints = FirebaseDatabase.getInstance(
        "https://my-ecopoint-project-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(DataType.EXCHANGE.key).child(DataType.COMPLAINTS.key)

    override fun setComplaint(complaint: Complaint) {
        complaints.child(complaint.cId).setValue(complaint)
    }
}