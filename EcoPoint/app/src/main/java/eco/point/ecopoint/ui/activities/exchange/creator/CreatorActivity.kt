package eco.point.ecopoint.ui.activities.exchange.creator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.models.Announcement
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.ActivityCreatorBinding

class CreatorActivity : AppCompatActivity() {
    private lateinit var caVM: CreatorActivityViewModel
    private lateinit var binding: ActivityCreatorBinding
    private var announcementId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = intent.extras
        announcementId = try { arguments!!.getString("id") }catch (e: Exception){ null }

        binding = ActivityCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        caVM = ViewModelProvider(
            this,
            CreatorViewModelFactory(LocalUserRepository(applicationContext), announcementId)
        )[CreatorActivityViewModel::class.java]
        initTagButtons()
        initEditTexts()
        initButtons()
        initErrors()
        announcementId?.let{initData()}
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initTagButtons(){
        caVM.plasticState.observe(this){
            binding.plasticImage.background =
                getDrawable(if (it) R.drawable.ic_plastic_active else R.drawable.ic_plastic_idle)
        }
        caVM.glassState.observe(this){
            binding.glassImage.background =
                getDrawable(if (it) R.drawable.ic_glass_active else R.drawable.ic_glass_idle)
        }
        caVM.metalState.observe(this){
            binding.metalImage.background =
                getDrawable(if (it) R.drawable.ic_metal_active else R.drawable.ic_metal_idle)
        }
        caVM.paperState.observe(this){
            binding.paperImage.background =
                getDrawable(if (it) R.drawable.ic_paper_active else R.drawable.ic_paper_idle)
        }
        caVM.foodState.observe(this){
            binding.foodImage.background =
                getDrawable(if (it) R.drawable.ic_food_active else R.drawable.ic_food_idle)
        }
        caVM.batteryState.observe(this){
            binding.batteryImage.background =
                getDrawable(if (it) R.drawable.ic_battery_active else R.drawable.ic_battery_idle)
        }
        binding.plasticImage.setOnClickListener { caVM.changePlasticState() }
        binding.glassImage.setOnClickListener { caVM.changeGlassState() }
        binding.metalImage.setOnClickListener { caVM.changeMetalState() }
        binding.paperImage.setOnClickListener { caVM.changePaperState() }
        binding.foodImage.setOnClickListener { caVM.changeFoodState() }
        binding.batteryImage.setOnClickListener { caVM.changeBatteryState() }
    }
    private fun initButtons(){
        binding.pasteEmail.setOnClickListener { binding.creatorEmail.setText(caVM.getEmail()) }
        binding.participantRgroup.setOnCheckedChangeListener { _, i ->
            when(i){
                binding.buyRbutt.id -> caVM.participant = Announcement.BUYER
                binding.sellRbutt.id -> caVM.participant = Announcement.SELLER
            }
        }
        binding.createButton.setOnClickListener { _ ->
            caVM.createAnnouncement{
                when (it) {
                    caVM.SUCCESS -> {
                        Toast.makeText(
                            this@CreatorActivity,
                            getString(R.string.announcement_creator_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    caVM.ERROR -> {
                        Toast.makeText(
                            this@CreatorActivity,
                            getString(R.string.announcement_creator_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun initEditTexts(){
        caVM.setUnits(binding.creatorUnits.text.toString())
        binding.createTitle.addTextChangedListener {caVM.setTitle(it.toString())}
        binding.createDescription.addTextChangedListener {caVM.setDescription(it.toString())}
        binding.creatorCost.addTextChangedListener {caVM.setCost(it.toString())}
        binding.creatorUnits.addTextChangedListener {caVM.setUnits(it.toString())}
        binding.creatorEmail.addTextChangedListener {caVM.setEmail(it.toString())}
        binding.creatorVk.addTextChangedListener {caVM.setVk(it.toString())}
        binding.creatorTg.addTextChangedListener {caVM.setTg(it.toString())}
        binding.creatorPhone.addTextChangedListener {caVM.setTel(it.toString())}
    }
    private fun initErrors(){
        caVM.connectError.observe(this){
            binding.connectionMsgErr
                .setTextColor(getColor(if (it) R.color.dark_green else R.color.red))
        }
        caVM.tagError.observe(this){
            binding.tagMsgErr
                .setTextColor(getColor(if (it) R.color.dark_green else R.color.red))
        }
        caVM.participantError.observe(this){
            binding.participantRgroup
                .setBackgroundColor(getColor(if (it) R.color.white else R.color.red))
        }
        caVM.emailError.observe(this){
            binding.creatorEmail
                .setBackgroundColor(getColor(if (it) R.color.white else R.color.red))
        }
        caVM.vkError.observe(this){
            binding.creatorVk
                .setBackgroundColor(getColor(if (it) R.color.white else R.color.red))
        }
        caVM.tgError.observe(this){
            binding.creatorTg
                .setBackgroundColor(getColor(if (it) R.color.white else R.color.red))
        }
        caVM.telError.observe(this){
            binding.creatorPhone
                .setBackgroundColor(getColor(if (it) R.color.white else R.color.red))
        }
        caVM.costError.observe(this){
            binding.creatorCost
                .setBackgroundColor(getColor(if (it) R.color.white else R.color.red))
        }
        caVM.titleError.observe(this){
            binding.titleError.visibility = if (it) View.GONE else View.VISIBLE
        }
        caVM.descriptionError.observe(this){
            binding.descriptionError.visibility = if (it) View.GONE else View.VISIBLE
        }
        caVM.allError.observe(this){
            if (!it && announcementId != null) Toast.makeText(this@CreatorActivity, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
        }
    }
    private fun initData(){
        caVM.completeData.observe(this){
            if(it!!){
                val ant = caVM.getData()
                with (binding) {
                    createTitle.setText(ant.title)
                    createDescription.setText(ant.description)
                    buyRbutt.isChecked = ant.participant == Announcement.BUYER
                    sellRbutt.isChecked = ant.participant == Announcement.SELLER
                    creatorCost.setText(ant.cost.toString())
                    creatorUnits.setText(ant.units)
                    creatorEmail.setText(ant.eMail)
                    creatorVk.setText(ant.vkLink)
                    creatorTg.setText(ant.tgLink)
                    creatorPhone.setText(ant.telephone)
                }
            }
        }
    }
}