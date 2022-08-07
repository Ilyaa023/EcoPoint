package eco.point.ecopoint.ui.activities.articles.tmp

import android.animation.Animator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import eco.point.ecopoint.databinding.ActivityPlMarkingsBinding
import eco.point.ecopoint.ui.fragments.learning.viewPagerFragments.PlMarkingsAdapter
import java.lang.Math.log10
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin


class PlMarkingsActivity : FragmentActivity() {
    private lateinit var binding: ActivityPlMarkingsBinding

    //private var isTouch = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlMarkingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.plPager.adapter = PlMarkingsAdapter(this)
        thread {
            Thread.sleep(700)
            runOnUiThread {
                binding.plPager.animate().translationX(150f).setInterpolator {
                    2 * log10(it.pow(4)) * it.pow(1.5f) + 0.07f * sin(3 * it) * sin(15f * it)
                }.duration = 750
            }
        }
//        binding.scrollMarks.setOnScrollChangeListener{ view: View, l: Int, t: Int, oldL: Int, oldT: Int ->
//            if (!isTouch && abs(l - oldL) < 5) {
//                val sX = binding.scrollMarks.scrollX
//                val width = resources.displayMetrics.widthPixels
//                var mark = sX / width
//                val remain = sX % width
//                if (remain > width * 0.5)
//                    mark++
//                binding.scrollerLayout.animate()
//                    .translationX(sX - mark * width.toFloat())
//                    .setDuration(500)
//                    .setInterpolator(LinearInterpolator())
//                    .setListener(null)
//            }
//        }
//
//        binding.plMark1.setOnTouchListener (this)
//        binding.plMark2.setOnTouchListener (this)
//        binding.plMark3.setOnTouchListener (this)
//        binding.plMark4.setOnTouchListener (this)
//        binding.plMark5.setOnTouchListener (this)
//        binding.plMark6.setOnTouchListener (this)
//        binding.plMark7.setOnTouchListener (this)
    }

//
//    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//        Log.e("TAG", "onCreate: event ${p1?.action}")
//        if (p1?.action == MotionEvent.ACTION_DOWN) isTouch = true
//        if (p1?.action == MotionEvent.ACTION_UP || p1?.action == MotionEvent.ACTION_CANCEL) isTouch = false
//        return true
//    }
}