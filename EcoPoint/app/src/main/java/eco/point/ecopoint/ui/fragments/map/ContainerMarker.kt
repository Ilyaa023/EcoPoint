package eco.point.ecopoint.ui.fragments.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eco.point.domain.models.Point
import eco.point.ecopoint.R

class ContainerMarker(container: Point, val context: Context): PointItem(container) {
    init {
        marker = MarkerOptions().position(
            LatLng(container.latitude, container.longitude)
        )
            .title(container.address)
            .icon(containerBitmapDescriptor())
            .anchor(0.5f, 0.5f)
    }

    private fun containerBitmapDescriptor(): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, R.drawable.ic_container)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}