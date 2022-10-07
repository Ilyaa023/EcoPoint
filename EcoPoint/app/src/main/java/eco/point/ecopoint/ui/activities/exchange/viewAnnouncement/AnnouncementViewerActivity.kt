package eco.point.ecopoint.ui.activities.exchange.viewAnnouncement

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import eco.point.data.firebase.database.exchange.FBAnnouncementsRepository
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.domain.DataUseCases.exchange.announcements.DeleteMyAnnouncement
import eco.point.domain.DataUseCases.exchange.announcements.GetUserAnnouncement
import eco.point.domain.models.Announcement
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.ActivityAnnouncementViewerBinding
import eco.point.ecopoint.ui.activities.exchange.creator.CreatorActivity

class AnnouncementViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnnouncementViewerBinding
    private lateinit var avVM: AnnouncementViewerViewModel
    private var aId: String? = null
    private var uId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementViewerBinding.inflate(layoutInflater)
        val arguments = intent.extras
        arguments!!.getString("id")?.let { aId = it }
        arguments.getString("uId")?.let { uId = it }
        arguments.getBoolean("Reportable")?.let{
            binding.reportButton.visibility = if (it && aId != FBUserRepository().uid) View.VISIBLE else View.GONE
            binding.myButtons.visibility = if (!it && uId == FBUserRepository().uid) View.VISIBLE else View.GONE
            binding.reportButton.setOnClickListener {
                val et = EditText(this@AnnouncementViewerActivity)
                val alert = AlertDialog.Builder(this).create()
                alert.setTitle(getString(R.string.announcement_viewer_butt_report))
                alert.setMessage(getString(R.string.announcement_viewer_report_message))
                alert.setView(et)
                alert.setButton(
                    AlertDialog.BUTTON_NEGATIVE, getString(R.string.announcement_viewer_butt_ok)
                ) { _, _ ->
                    if (aId != null && aId != "" || uId != null && uId != "")
                        avVM.sendReport(uId!!, aId!!, et.text.toString())
                }
                alert.setButton(
                    AlertDialog.BUTTON_POSITIVE, getString(R.string.announcement_viewer_butt_cancel)
                ) { _, _ ->
                    alert.dismiss()
                }
                alert.show()
            }
            binding.annEdit.setOnClickListener{
                startActivity(Intent(this, CreatorActivity::class.java).apply { putExtra("id", aId) })
            }
            binding.annDelete.setOnClickListener{
                GetUserAnnouncement(FBAnnouncementsRepository()).execute(uId.toString(), aId.toString()){ ant ->
                    DeleteMyAnnouncement(FBAnnouncementsRepository()).execute(ant!!)
                    finish()
                }
            }
        }
        setContentView(binding.root)
        avVM = ViewModelProvider(this)[AnnouncementViewerViewModel::class.java]
        if (aId != null && aId != "" || uId != null && uId != "")
            avVM.getAnnouncement(uId!!, aId!!){
                if (it != null){
                    with(binding) {
                        annTitle.text = it.title
                        annParticipant.text =
                            if (it.participant == Announcement.BUYER)
                                getString(R.string.announcement_buy)
                            else
                                getString(R.string.announcement_sell)
                        annNameCity.text = "${it.creator.name}, ${it.creator.city}"
                        annDate.text = avVM.getDateString(it.dateTime, this@AnnouncementViewerActivity)
                        annDescription.text = it.description
                        annPlasticImage.visibility = avVM.getPlasticVisibility(it.tag)
                        annGlassImage.visibility = avVM.getGlassVisibility(it.tag)
                        annMetalImage.visibility = avVM.getMetalVisibility(it.tag)
                        annPaperImage.visibility = avVM.getPaperVisibility(it.tag)
                        annFoodImage.visibility = avVM.getFoodVisibility(it.tag)
                        annBatteryImage.visibility = avVM.getBatteryVisibility(it.tag)
                        annCostUnits.text = "${it.cost}, ${it.units}"
//                        annVk.text = it.vkLink
                        annVk.visibility = avVM.getConnectVisibility(it.vkLink)
                        annVk.setOnClickListener { _ ->
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(avVM.getCorrectLink(it.vkLink!!))))
                        }
//                        annTg.text = it.tgLink
                        annTg.visibility = avVM.getConnectVisibility(it.tgLink)
                        annTg.setOnClickListener { _ ->
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(avVM.getCorrectLink(it.tgLink!!))))
                        }
                        annEmail.text = it.eMail
                        annEmail.visibility = avVM.getConnectVisibility(it.eMail)
                        annPhone.text = it.telephone
                        annPhone.visibility = avVM.getConnectVisibility(it.telephone)
                        Linkify.addLinks(annEmail, Linkify.EMAIL_ADDRESSES)
                        Linkify.addLinks(annPhone, Linkify.PHONE_NUMBERS)
                    }
                } else {
                    Toast.makeText(this@AnnouncementViewerActivity, getString(R.string.fail), Toast.LENGTH_LONG).show()
                }
            }
    }
}