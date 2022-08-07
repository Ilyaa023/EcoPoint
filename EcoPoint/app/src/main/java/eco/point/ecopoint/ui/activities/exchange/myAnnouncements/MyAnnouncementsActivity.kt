package eco.point.ecopoint.ui.activities.exchange.myAnnouncements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import eco.point.ecopoint.databinding.ActivityMyAnnouncementsBinding
import eco.point.ecopoint.ui.activities.exchange.IRefresh
import eco.point.ecopoint.ui.activities.exchange.myAnnouncements.main.SectionsPagerAdapter

class MyAnnouncementsActivity : AppCompatActivity(), IRefresh {

    private lateinit var binding: ActivityMyAnnouncementsBinding
    private lateinit var maVM: MyAnnouncementsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        maVM = ViewModelProvider(this)[MyAnnouncementsViewModel::class.java]
        loadAnnouncements()
    }
    private fun loadAnnouncements(){
        maVM.getAnnouncements{
            val sectionsPagerAdapter = SectionsPagerAdapter(
                this@MyAnnouncementsActivity,
                supportFragmentManager,
                it,
                iRefresh = this@MyAnnouncementsActivity
            )
            val viewPager: ViewPager = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = binding.tabs
            tabs.setupWithViewPager(viewPager)
        }
    }

    override fun onRefresh() {
        loadAnnouncements()
    }
}