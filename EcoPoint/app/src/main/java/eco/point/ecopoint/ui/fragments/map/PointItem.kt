package eco.point.ecopoint.ui.fragments.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import eco.point.domain.models.Point

abstract class PointItem(
        point: Point,
): ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String

    var marker = MarkerOptions()

    override fun getPosition(): LatLng {
        return position
    }
    override fun getTitle(): String? {
        return title
    }
    override fun getSnippet(): String? {
        return snippet
    }
    init {
        position = LatLng(point.latitude, point.longitude)
        this.title = point.address
        this.snippet = point.description
    }
}