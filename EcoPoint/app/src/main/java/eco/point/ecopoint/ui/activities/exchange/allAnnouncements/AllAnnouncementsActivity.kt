package eco.point.ecopoint.ui.activities.exchange.allAnnouncements

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.ExchangeKeys
import eco.point.domain.models.enums.PointKeys
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.ActivityAllAnnouncementsBinding
import eco.point.ecopoint.ui.activities.exchange.IRefresh
import eco.point.ecopoint.ui.activities.exchange.allAnnouncements.ui.main.SectionsPagerAdapter

class AllAnnouncementsActivity : AppCompatActivity(), IRefresh {

    private lateinit var binding: ActivityAllAnnouncementsBinding
    private lateinit var aaVM: AllAnnouncementsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        aaVM = ViewModelProvider(this)[AllAnnouncementsViewModel::class.java]
        loadAnnouncements()

        binding.leftTypesArrowAnts.setOnClickListener {
            binding.antsScrollView.fullScroll(View.FOCUS_LEFT)
        }
        binding.rightTypesArrowAnts.setOnClickListener {
            binding.antsScrollView.fullScroll(View.FOCUS_RIGHT)
        }

        binding.buttPlasticAnts.setOnClickListener {
            binding.buttPlasticAnts.background =
                ResourcesCompat.getDrawable(
                    resources,
                    if(aaVM.setAnnouncementsVisibility(ExchangeKeys.PLASTIC.key){
                        showAnnouncements(it)
                        }) R.drawable.ic_plastic_active
                    else R.drawable.ic_plastic_idle,
                    null
                )
        }
        binding.buttGlassAnts.setOnClickListener {
            binding.buttGlassAnts.background =
                ResourcesCompat.getDrawable(
                    resources,
                    if(aaVM.setAnnouncementsVisibility(ExchangeKeys.GLASS.key){
                            showAnnouncements(it)
                        }) R.drawable.ic_glass_active
                    else R.drawable.ic_glass_idle,
                    null
                )
        }
        binding.buttPaperAnts.setOnClickListener {
            binding.buttPaperAnts.background =
                ResourcesCompat.getDrawable(
                    resources,
                    if(aaVM.setAnnouncementsVisibility(ExchangeKeys.PAPER.key){
                            showAnnouncements(it)
                        }) R.drawable.ic_paper_active
                    else R.drawable.ic_paper_idle,
                    null
                )
        }
        binding.buttMetalAnts.setOnClickListener {
            binding.buttMetalAnts.background =
                ResourcesCompat.getDrawable(
                    resources,
                    if(aaVM.setAnnouncementsVisibility(ExchangeKeys.METAL.key){
                            showAnnouncements(it)
                        }) R.drawable.ic_metal_active
                    else R.drawable.ic_metal_idle,
                    null
                )
        }
        binding.buttFoodAnts.setOnClickListener {
            binding.buttFoodAnts.background =
                ResourcesCompat.getDrawable(
                    resources,
                    if(aaVM.setAnnouncementsVisibility(ExchangeKeys.FOOD.key){
                            showAnnouncements(it)
                        }) R.drawable.ic_food_active
                    else R.drawable.ic_food_idle,
                    null
                )
        }
        binding.buttBatteryAnts.setOnClickListener {
            binding.buttBatteryAnts.background =
                ResourcesCompat.getDrawable(
                    resources,
                    if (aaVM.setAnnouncementsVisibility(ExchangeKeys.BATTERY.key){
                            showAnnouncements(it)
                        }) R.drawable.ic_battery_active
                    else R.drawable.ic_battery_idle,
                    null
                )
        }

    }

    private fun loadAnnouncements(){
        aaVM.getAnnouncements{
            showAnnouncements(it)
        }
    }

    private fun showAnnouncements(ants: HashMap<Boolean, ArrayList<Announcement>>){
        binding.viewPager.adapter = SectionsPagerAdapter(
            this@AllAnnouncementsActivity,
            supportFragmentManager,
            ants,
            iRefresh = this@AllAnnouncementsActivity
        )
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onRefresh() {
        loadAnnouncements()
    }
}