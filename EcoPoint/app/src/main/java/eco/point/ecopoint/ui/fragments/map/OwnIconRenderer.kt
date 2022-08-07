package eco.point.ecopoint.ui.fragments.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class OwnIconRenderer(
        context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<PointItem>?
) : DefaultClusterRenderer<PointItem>(context, map, clusterManager) {
    override fun onBeforeClusterItemRendered(item: PointItem, markerOptions: MarkerOptions) {
        markerOptions.icon(item.marker.icon)
        markerOptions.anchor(item.marker.anchorU, item.marker.anchorV)
    }
}