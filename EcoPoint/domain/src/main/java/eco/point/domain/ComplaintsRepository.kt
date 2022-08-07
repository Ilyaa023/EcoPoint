package eco.point.domain

import eco.point.domain.models.Complaint

abstract class ComplaintsRepository {
    open fun getAll(callback: (ArrayList<Complaint>) -> Unit){}
    open fun setComplaint(complaint: Complaint){}
}