package eco.point.ecopoint.ui.activities.articles.tmp

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.ActivityPrepPlBinding

class PrepPlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrepPlBinding
    private val steps = ArrayList<RadioButton>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pics = arrayOf(
            getDrawable(R.drawable.ic_pl_prep_wash)!!,
            getDrawable(R.drawable.ic_pl_prep_label)!!,
            getDrawable(R.drawable.ic_pl_prep_remove)!!,
            getDrawable(R.drawable.ic_pl_prep_smooth)!!,
            getDrawable(R.drawable.ic_pl_prep_took)!!,
        )
        val texts = arrayOf(
            getString(R.string.prep_pl_text_first),
            getString(R.string.prep_pl_text_second),
            getString(R.string.prep_pl_text_third),
            getString(R.string.prep_pl_text_fourth),
            getString(R.string.prep_pl_text_fifth)
        )
        binding = ActivityPrepPlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE
        binding.stepsGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                val currStep = steps.indexOf(findViewById(p1))
                binding.contentView.animate()
                    .alpha(0f)
                    .setListener(object : Animator.AnimatorListener{
                        override fun onAnimationStart(p0: Animator?) {}
                        override fun onAnimationCancel(p0: Animator?) {}
                        override fun onAnimationRepeat(p0: Animator?) {}
                        override fun onAnimationEnd(p0: Animator?) {
                            binding.contentView.setImageDrawable(pics[steps.indexOf(findViewById(binding.stepsGroup.checkedRadioButtonId))])
                            binding.contentView.animate()
                                .alpha(1f)
                                .setListener(null)
                                .duration = 150
                        }
                    })
                    .duration = 150
                binding.textView.text = texts[steps.indexOf(findViewById(binding.stepsGroup.checkedRadioButtonId))]
                changeRBColors()
            }
        })
        for (num in 0 until 5){
            steps.add(num, RadioButton(this))
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(0,10,0, 10)
            steps[num].layoutParams = lp
            steps[num].gravity = Gravity.CENTER_VERTICAL
            steps[num].setPadding(16, 0, 0, 0)
            steps[num].setTextColor(getColor(R.color.green))
            steps[num].buttonDrawable = getDrawable(R.drawable.ic_radiobutton_green)
            binding.stepsGroup.addView(steps[num])
        }
        steps[0].isChecked = true
        steps[0].text = getString(R.string.prep_pl_first)
        steps[1].text = getString(R.string.prep_pl_second)
        steps[2].text = getString(R.string.prep_pl_third)
        steps[3].text = getString(R.string.prep_pl_fourth)
        steps[4].text = getString(R.string.prep_pl_fifth)

        binding.leftButton2.setOnClickListener {
            val currStep = steps.indexOf(findViewById(binding.stepsGroup.checkedRadioButtonId))
            if (currStep > 0)
                steps[currStep - 1].isChecked = true
        }

        binding.rightButton2.setOnClickListener {
            val currStep = steps.indexOf(findViewById(binding.stepsGroup.checkedRadioButtonId))
            if (currStep < steps.size - 1)
                steps[currStep + 1].isChecked = true
        }
    }

    private fun changeRBColors(){
        for (rb in steps){
            if (rb.isChecked) {
                rb.buttonDrawable = getDrawable(R.drawable.ic_radiobutton_dark_green)
                rb.setTextColor(getColor(R.color.dark_green))
            }
            else {
                rb.buttonDrawable = getDrawable(R.drawable.ic_radiobutton_green)
                rb.setTextColor(getColor(R.color.green))
            }
        }
    }
}