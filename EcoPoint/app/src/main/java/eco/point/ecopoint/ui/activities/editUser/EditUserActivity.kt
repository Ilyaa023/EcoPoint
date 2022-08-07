package eco.point.ecopoint.ui.activities.editUser

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.data.firebase.googleAuth.GoogleSignInImpl
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.DataUseCases.auth.skiped.GetSkiped
import eco.point.domain.DataUseCases.user.city.GetUserCity
import eco.point.domain.DataUseCases.user.name.GetUserName
import eco.point.domain.DataUseCases.user.name.SetUserName
import eco.point.domain.models.User
import eco.point.domain.models.enums.UserKeys
import eco.point.ecopoint.R
import eco.point.ecopoint.databinding.ActivityEditUserBinding
import eco.point.ecopoint.ui.activities.authorization.AuthorizationActivity

class EditUserActivity: AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val height = resources.displayMetrics.heightPixels
        var firstHeight: Int? = null
        var secondHeight: Int? = null
        binding.firstHalfLayout.post {
            firstHeight = binding.firstHalfLayout.height
            secondHeight?.let { setHeight(firstHeight!!, it, height) }
        }
        binding.secondHalfLayout.post {
            secondHeight = binding.secondHalfLayout.height
            firstHeight?.let { setHeight(firstHeight!!, it, height) }
        }
//        val sumHeight = binding.firstHalfLayout.measuredHeight + binding.secondHalfLayout.height
//        Log.e("TAG", "onCreate: ${sumHeight}", )
//        if (sumHeight < height) {
//            val lp = binding.secondHalfLayout.layoutParams
//            binding.secondHalfLayout.translationY = (height - sumHeight).toFloat()
//        }
        binding.backButt.setOnClickListener {
            finish()
        }
        binding.NameEdit.text = SpannableStringBuilder(GetUserName(LocalUserRepository(this)).execute())
        binding.CityEdit.text = SpannableStringBuilder(GetUserCity(LocalUserRepository(this)).execute())

        binding.CityEdit.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.Cities))
        )
        binding.saveButt.setOnClickListener {
            this@EditUserActivity.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            val bCity = checkCity(binding.CityEdit.text.toString())
            val bName = checkName(binding.NameEdit.text.toString())
            if (bCity && bName){
                if (!GetSkiped(LocalUserRepository(this)).execute()) {
                    FBUserRepository().setData(UserKeys.NAME, User(name = binding.NameEdit.text.toString()))
                    FBUserRepository().setData(UserKeys.CITY, User(city = binding.CityEdit.text.toString()))
                }
            }
            if (bCity)
                binding.errtextView4.visibility = View.GONE
            else
                binding.errtextView4.visibility = View.VISIBLE
            if (bName)
                binding.errtextView5.visibility = View.GONE
            else
                binding.errtextView5.visibility = View.VISIBLE
        }
        if (GetSkiped(LocalUserRepository(this)).execute()) {
            binding.EMLL.visibility = View.GONE
            binding.deleteAccountButton.visibility = View.GONE
            binding.signOutButton.text = getString(R.string.edit_user_butt_sign_in)
            binding.signOutButton.setOnClickListener {
                startActivity(Intent(this@EditUserActivity, AuthorizationActivity::class.java))
                finish()
            }
        } else{
            val userEmail = FBUserRepository().getEmail()
            binding.EMailEdit.text = userEmail
            binding.deleteAccountButton.setOnClickListener{
                val dialog = AlertDialog.Builder(this@EditUserActivity).create()
                dialog.window!!.setDimAmount(0f)
                dialog.show()
                dialog.setContentView(R.layout.layout_for_delete_confirmation)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val no = dialog.findViewById<Button>(R.id.alert_continue_with_local)
                val yes = dialog.findViewById<Button>(R.id.alert_continue_with_cloud)
                no!!.setOnClickListener { dialog.dismiss() }
                yes!!.setOnClickListener {
                    GoogleSignInImpl(this).signOut()
                    val rep = FBUserRepository()
                    rep.setData(UserKeys.NAME, User())
                    rep.setData(UserKeys.CITY, User())
                    rep.setData(UserKeys.BIRTH, User())
                    rep.setData(UserKeys.BONUSES, User())
                    rep.setData(UserKeys.LEVEL, User())
                    rep.setData(UserKeys.GARBAGE_COUNTER, User())
                    rep.setData(UserKeys.RATING, User())
                    dialog.dismiss()
                    LocalUserRepository(this).deleteAll()
                    startActivity(Intent(this@EditUserActivity, AuthorizationActivity::class.java))
                    finish()
                }
            }
            binding.signOutButton.setOnClickListener {
                GoogleSignInImpl(this).signOut()
                LocalUserRepository(this).deleteAll()
                startActivity(Intent(this@EditUserActivity, AuthorizationActivity::class.java))
                finish()
            }
        }
    }

    private fun checkCity(city: String): Boolean =
        eco.point.domain.DataUseCases.user.city.SetUserCity(
            repository = LocalUserRepository(this), cities = resources.getStringArray(R.array.Cities)
        ).execute(city)
    private fun checkName(name: String): Boolean =
        SetUserName(
            repository = LocalUserRepository(this)
        ).execute(name = name)

    private fun setHeight(first: Int, second: Int, height: Int){
        if (first + second < height) {
            binding.secondHalfLayout.translationY = (height - (first + second)).toFloat()
            val lp = binding.scrollingLayout.layoutParams
            lp.height = height
            binding.scrollingLayout.layoutParams = lp
        }
    }
}