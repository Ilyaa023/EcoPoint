package eco.point.domain.models

import java.util.*
import kotlin.collections.ArrayList

class ArticleType1(
        var name: String = "defName",
        var id:  String = "defID_${GregorianCalendar().timeInMillis}",
        var mainPic: ByteArray? = null,
        var steps: ArrayList<String> = ArrayList<String>(),
        var pictures: ArrayList<ByteArray?> = ArrayList<ByteArray?>(),
        var texts: ArrayList<String> = ArrayList<String>(),
        var public: Boolean = false
){
}