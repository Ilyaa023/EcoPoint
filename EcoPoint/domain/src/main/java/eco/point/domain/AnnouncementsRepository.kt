package eco.point.domain

import eco.point.domain.models.Announcement

abstract class AnnouncementsRepository {
    open fun getAll(callback: (ArrayList<Announcement>) -> Unit){}
    open fun getUserAll(uId: String, callback: (ArrayList<Announcement>) -> Unit){}
    open fun setMy(announcement: Announcement, callback: (Boolean) -> Unit) {}
    open fun deleteMy(announcement: Announcement){}
    open fun getUserAnnouncement(userId: String, id: String, callback: (Announcement?) -> Unit){}
    open fun setComplain(uId: String, aId:String, cId: String){}
}