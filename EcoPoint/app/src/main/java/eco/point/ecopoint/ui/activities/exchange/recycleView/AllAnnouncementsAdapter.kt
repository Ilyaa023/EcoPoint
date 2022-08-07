package eco.point.ecopoint.ui.activities.exchange.recycleView

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eco.point.domain.models.Announcement
import eco.point.ecopoint.R

class AllAnnouncementsAdapter(private val ants: ArrayList<Announcement>, private val activity: Activity)
    : RecyclerView.Adapter<AllAnnouncementsVIewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllAnnouncementsVIewHolder =
        AllAnnouncementsVIewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.my_announcement_item, parent, false), activity)

    override fun onBindViewHolder(holder: AllAnnouncementsVIewHolder, position: Int) {
        holder.bind(ants[position])
    }

    override fun getItemCount(): Int = ants.size

}