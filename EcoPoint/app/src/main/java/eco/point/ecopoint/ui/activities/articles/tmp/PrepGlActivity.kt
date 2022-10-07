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
import eco.point.ecopoint.databinding.ActivityPrepGlBinding

class PrepGlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrepGlBinding
    private val steps = ArrayList<RadioButton>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pics = arrayOf(
            getDrawable(R.drawable.ic_prep_gl_first)!!,
            getDrawable(R.drawable.ic_prep_gl_second)!!,
            getDrawable(R.drawable.ic_pl_prep_took)!!,
        )
        val texts = arrayOf(
            "",
            getString(R.string.prep_gl_text_second),
            getString(R.string.prep_gl_text_third)
        )
        binding = ActivityPrepGlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE
        binding.stepsGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
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
        for (num in 0 until 3){
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
        steps[0].text = getString(R.string.prep_gl_first)
        steps[1].text = getString(R.string.prep_gl_second)
        steps[2].text = getString(R.string.prep_gl_third)

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