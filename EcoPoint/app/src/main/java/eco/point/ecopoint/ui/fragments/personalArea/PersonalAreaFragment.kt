package eco.point.ecopoint.ui.fragments.personalArea

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import eco.point.data.storage.LocalUserRepository
import eco.point.ecopoint.R
import eco.point.ecopoint.ui.activities.editUser.EditUserActivity
import eco.point.ecopoint.databinding.FragmentPersonalAreaBinding
import eco.point.ecopoint.ui.activities.authorization.AuthorizationActivity
import eco.point.ecopoint.ui.activities.exchange.AnnouncementsActivity
import java.io.File

class PersonalAreaFragment : Fragment() {

    private lateinit var binding: FragmentPersonalAreaBinding
    private lateinit var galleryResultHandler: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalAreaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val paVM = ViewModelProvider(this)[PersonalAreaViewModel::class.java]

        galleryResultHandler = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == RESULT_OK) {
                val selectedImage: Uri? = it.data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        this@PersonalAreaFragment.activity?.contentResolver,
                        selectedImage
                    )
                    binding.Avatar.setImageBitmap(bitmap)
                    paVM.saveBmpInStore(bitmap)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        val cachePath = File(Environment.getExternalStorageDirectory()
                .absolutePath + "/DCIM/EcoPoint/UserPic.jpg")
        if (cachePath.exists())
        try{
            binding.Avatar.setImageBitmap(
                BitmapFactory.decodeStream(
                    cachePath.toURL().openConnection().getInputStream()
                )
            )
        } catch (e: Exception){}
        binding.EditButt.setOnClickListener {
            startActivity(Intent(this@PersonalAreaFragment.context, EditUserActivity::class.java))
        }
        binding.tgBotButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                                 Uri.parse("https://t.me/ecopointapp_bot")))
        }
        binding.Avatar.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            galleryResultHandler.launch(photoPickerIntent)
        }

        paVM.getName(LocalUserRepository(requireContext())){
            binding.Name.text = it
        }

        paVM.getLevel(LocalUserRepository(requireContext())){
            binding.lvlButtCounter.text = it.toString()
        }

        paVM.getBonuses(LocalUserRepository(requireContext())){
            binding.lvlButtComplete.text = it.toString()
        }
        binding.bannedAnnouncementsButton.setOnClickListener {
            if (paVM.getSkiped(LocalUserRepository(requireContext()))){
                Snackbar.make(
                    binding.root,
                    getString(R.string.personal_area_error_my_announcements),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.personal_area_butt_login)){
                    startActivity(Intent(requireContext(), AuthorizationActivity::class.java))
                    requireActivity().finish()
                }.setActionTextColor(resources.getColor(R.color.green, null))
                    .show()
            } else
                startActivity(Intent(requireContext(), AnnouncementsActivity::class.java).apply { putExtra("isBanned", true) })
        }
        binding.allAnnouncementsButton.setOnClickListener {
            startActivity(Intent(requireContext(), AnnouncementsActivity::class.java))
        }
        return root
    }
}