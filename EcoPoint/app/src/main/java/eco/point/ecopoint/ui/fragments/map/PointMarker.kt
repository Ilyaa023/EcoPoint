package eco.point.ecopoint.ui.fragments.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eco.point.domain.models.Point
import eco.point.ecopoint.R

class PointMarker(point: Point, val context: Context): PointItem(point) {
    init {
        marker = MarkerOptions().position(
            LatLng(point.latitude, point.longitude))
            .title(point.address)
            .icon(pointBitmapDescriptor())
            .anchor(0.5f, 0.5f)
    }

    private fun pointBitmapDescriptor(): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, R.drawable.ic_point)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}