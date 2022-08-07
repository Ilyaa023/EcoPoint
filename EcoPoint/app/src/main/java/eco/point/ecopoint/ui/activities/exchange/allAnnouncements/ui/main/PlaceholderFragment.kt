package eco.point.ecopoint.ui.activities.exchange.allAnnouncements.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import eco.point.domain.models.Announcement
import eco.point.ecopoint.databinding.FragmentAllAnnouncementsBinding
import eco.point.ecopoint.databinding.FragmentMyAnnouncementsBinding
import eco.point.ecopoint.ui.activities.exchange.IRefresh
import eco.point.ecopoint.ui.activities.exchange.recycleView.AllAnnouncementsAdapter
import eco.point.ecopoint.ui.activities.exchange.recycleView.AnnouncementsAdapter

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(
        private val ants: ArrayList<Announcement>?,
        private val iRefresh: IRefresh
) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentAllAnnouncementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllAnnouncementsBinding.inflate(inflater, container, false)
        ants?.let {
            binding.allAnnouncementsRecycle.setHasFixedSize(false)
            binding.allAnnouncementsRecycle.layoutManager = LinearLayoutManager(requireContext())
            binding.allAnnouncementsRecycle.adapter = AllAnnouncementsAdapter(it, requireActivity())
        }
        val root = binding.root
        binding.refreshAllAnnouncements.setOnRefreshListener {
            binding.refreshAllAnnouncements.isRefreshing = false
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
            return PlaceholderFragment(ants[key], iRefresh).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}