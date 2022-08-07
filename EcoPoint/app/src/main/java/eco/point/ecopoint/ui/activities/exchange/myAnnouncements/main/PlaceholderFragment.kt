package eco.point.ecopoint.ui.activities.exchange.myAnnouncements.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import eco.point.domain.models.Announcement
import eco.point.ecopoint.databinding.FragmentMyAnnouncementsBinding
import eco.point.ecopoint.ui.activities.exchange.IRefresh
import eco.point.ecopoint.ui.activities.exchange.creator.CreatorActivity
import eco.point.ecopoint.ui.activities.exchange.recycleView.AnnouncementsAdapter

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(
        private val ants: ArrayList<Announcement>?,
        private val publicAnts: Boolean,
        private val iRefresh: IRefresh
        ) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentMyAnnouncementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyAnnouncementsBinding.inflate(inflater, container, false)
        if (publicAnts){
            binding.createButton.visibility = View.VISIBLE
            binding.createButton.setOnClickListener {
                startActivity(Intent(requireContext(), CreatorActivity::class.java))
            }
        } else {
            binding.createButton.visibility = View.GONE
        }
        ants?.let {
            binding.announcementsRecycler.setHasFixedSize(false)
            binding.announcementsRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.announcementsRecycler.adapter = AnnouncementsAdapter(it, requireActivity())
        }

        val root = binding.root
        binding.refreshMyAnnouncements.setOnRefreshListener {
            binding.refreshMyAnnouncements.isRefreshing = false
            iRefresh.onRefresh()
        }
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(ants: HashMap<Boolean, ArrayList<Announcement>>, sectionNumber: Int, iRefresh: IRefresh): PlaceholderFragment {
            val key = sectionNumber == 0
            return PlaceholderFragment(ants[key], key, iRefresh).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}