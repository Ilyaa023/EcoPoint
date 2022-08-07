package eco.point.ecopoint.ui.fragments.learning.viewPagerFragments

import android.animation.TimeInterpolator
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.FragmentPlMarkingsBinding
import kotlin.concurrent.thread
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlMarkingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlMarkingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    private lateinit var binding: FragmentPlMarkingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlMarkingsBinding.inflate(inflater, container, false)
        binding.plMark1.layoutParams.width = resources.displayMetrics.widthPixels
        binding.plMark2.layoutParams.width = resources.displayMetrics.widthPixels
        binding.plMark3.layoutParams.width = resources.displayMetrics.widthPixels
        binding.plMark4.layoutParams.width = resources.displayMetrics.widthPixels
        binding.plMark5.layoutParams.width = resources.displayMetrics.widthPixels
        binding.plMark6.layoutParams.width = resources.displayMetrics.widthPixels
        binding.plMark7.layoutParams.width = resources.displayMetrics.widthPixels
        val visibleView = when(param1){
            1 -> binding.plMark1
            2 -> binding.plMark2
            3 -> binding.plMark3
            4 -> binding.plMark4
            5 -> binding.plMark5
            6 -> binding.plMark6
            7 -> binding.plMark7
            else -> null
        }
        visibleView?.let { visibleView.visibility = View.VISIBLE }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlMarkingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(num: Int) = PlMarkingsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, num)
            }
        }
    }
}

class PlMarkingsAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        PlMarkingsFragment.newInstance(position + 1)
}