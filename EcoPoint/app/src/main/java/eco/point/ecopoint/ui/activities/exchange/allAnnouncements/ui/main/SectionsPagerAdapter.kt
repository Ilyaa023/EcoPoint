package eco.point.ecopoint.ui.activities.exchange.allAnnouncements.ui.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import eco.point.domain.models.Announcement
import eco.point.ecopoint.R
import eco.point.ecopoint.ui.activities.exchange.IRefresh

private val TAB_TITLES = arrayOf(
    R.string.announcements_tab_buy, R.string.announcements_tab_sell
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
        private val context: Context,
        fm: FragmentManager,
        private val ants: HashMap<Boolean, ArrayList<Announcement>>,
        private val iRefresh: IRefresh
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = PlaceholderFragment.newInstance(ants, position, iRefresh)

    override fun getPageTitle(position: Int): CharSequence =
         context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 2
}